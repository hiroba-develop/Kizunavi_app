# 新規プロダクトを EC2 + Docker にリリースする手順

本リポジトリを **テンプレートとして複製**したあと、初めて AWS 上に **単一 EC2 インスタンス**で `docker compose` により公開するまでの推奨フローです。HTTPS 終端は **ALB**、イメージは **ECR**、デプロイは **手動 SSH**（`docker compose pull && up -d`）を想定します。

設計方針は [ADR-0025](../adr/0025-aws-runtime-ec2-docker.md)、CI と IaC の方針は [ADR-0026](../adr/0026-cicd-without-iac.md) を参照してください。

---

## 1. リポジトリの複製とポート採番

1. GitHub でテンプレートから新リポジトリを作成（または fork / clone 後にリモート差し替え）。
2. [ポート採番台帳（Google スプレッドシート）](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0) で **未使用の連番**（5173+N / 8080+N）を確保し、新規行を追加する。ルールは [docs/port-registry.md](../port-registry.md) を参照。
3. ルート `.env`（`.env.example` をコピー）で以下を設定:
   - `SPRING_PROFILES_ACTIVE`（自 PC / AWS EC2 では通常 `dev`（検証）または `prod`（本番））
   - `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` と `AWS_*`（RDS・SES は **dev / prod いずれも必須**）
   - `FRONTEND_PORT` / `BACKEND_PORT`
   - `CORS_ALLOWED_ORIGINS` / `APP_FRONTEND_BASE_URL`（通常は `http://localhost:<FRONTEND_PORT>`）
4. **ローカル非 Docker** でバックポートを 8080 以外にした場合は、`frontend/vite.config.ts` の `proxy` の `target` も合わせて変更（[ADR-0016](../adr/0016-port-numbering-policy.md)）。

5. 台帳に **プロダクト名・ポート・備考** を追記し、PR でマージ。

---

## 2. AWS アカウントと環境の決定

- **dev / stg / prod** のいずれにデプロイするか決定。
- アカウント戦略: **プロダクトごとアカウント分離** vs **環境ごとアカウント** — 組織ポリシーに従う（[aws-infrastructure-best-practices.md](aws-infrastructure-best-practices.md) 参照）。

---

## 3. Secrets Manager の登録

1. [aws-secrets-manager-setup.md](../aws-secrets-manager-setup.md) に従い、`myproduct/{env}/...` 形式でシークレットを作成。
2. 必須例: DB 接続、`JWT_SECRET`。SES を使う場合は送信元検証済みドメインと IAM。
3. **本番の JWT は十分な長さのランダム値**にし、Git に含めない。

---

## 4. ネットワーク（VPC / サブネット / SG）

1. **ALB 用パブリックサブネット**（2 AZ 以上）。
2. **アプリ用 EC2 を置くプライベートサブネット**（NAT Gateway 経由で ECR / Secrets に到達できるようにする）。
3. **RDS 用プライベートサブネット**（別 SG で 1521 をアプリ EC2 の SG からのみ許可）。
4. セキュリティグループは **最小ポート**（ALB: 443 インバウンド、アプリ EC2: ALB からのターゲットポートのみ、SSH は運用方針に応じて限定）。

---

## 5. EC2 インスタンスのプロビジョニング

1. **AMI**: Amazon Linux 2023 等、組織標準の Linux を選択。
2. **IAM インスタンスプロファイル**をアタッチし、少なくとも以下を許可（最小権限で分割してもよい）:
   - ECR: `ecr:GetAuthorizationToken`、対象リポジトリへの `BatchGetImage` 等（pull）
   - Secrets Manager: `secretsmanager:GetSecretValue`（対象 ARN に限定）
   - SES 送信に必要な権限（キーをコンテナに載せない運用とする場合）
3. **ユーザーデータ（cloud-init）** で Docker のインストールを開始してもよい（§6 と重複しないよう整理）。
4. **キーペアまたは Systems Manager Session Manager**: 本手順は **SSH 手動デプロイ**前提のため、SSH 鍵または SSM でのシェルアクセスを確保。

---

## 6. Docker / docker compose のインストール

EC2 に SSH したうえで（Amazon Linux 2023 の例）:

```bash
sudo dnf update -y
sudo dnf install -y docker
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user

# docker compose プラグイン（環境によりパッケージ名が異なる場合あり）
sudo dnf install -y docker-compose-plugin
docker compose version
```

再ログイン後、`docker ps` が **sudo なし**で動くことを確認。

---

## 7. ECR からの初回 pull 動作確認

1. [Amazon ECR 構築・運用ガイド](../aws-ecr-setup.md) に従いリポジトリと OIDC push ロールを用意。
2. EC2 上で:

```bash
aws sts get-caller-identity
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.ap-northeast-1.amazonaws.com
```

3. テストとして `docker pull <ECR_URI>:<タグ>` が成功することを確認。

---

## 8. systemd と docker compose による常駐起動

### 8.1 アプリ配置ディレクトリ

例: `/opt/myproduct` に以下を配置する運用とする。

- ルートの `docker-compose.yml`（および必要なら `compose.override` は使わない）
- Secrets を環境変数に流し込むシェル（**機密をファイルに残さない**運用を推奨）

### 8.2 シークレット取得スクリプト（例）

EC2 の起動時またはサービス起動直前に、`aws secretsmanager get-secret-value` で JSON を取得し、`export` する。詳細は [aws-secrets-manager-setup.md](../aws-secrets-manager-setup.md) の「EC2 での利用」を参照。

### 8.3 systemd ユニット（概念例）

- `ExecStartPre=` でシークレット読み込みスクリプト
- `ExecStart=/usr/bin/docker compose up -d`
- `ExecStop=/usr/bin/docker compose down`
- `WorkingDirectory=/opt/myproduct`

再起動試験（`sudo reboot`）後もコンテナが上がることを確認。

### 8.4 ログ（CloudWatch）

コンテナの `logging` に **awslogs** ドライバを指定するか、CloudWatch Agent でファイルまたは stdout を転送（[ADR-0021](../adr/0021-aws-observability.md)）。

---

## 9. ALB・ACM・Route 53

1. **ACM** で証明書を発行（リージョン: **ALB と同じ**）。
2. **ALB** を作成し、リスナー **443** で証明書を紐づける。HTTP(80) は 443 へリダイレクト推奨。
3. **ターゲットグループ**（例）:

| 名前 | ポート | ヘルスチェック |
|------|--------|----------------|
| backend | **8080** | `HTTP` `/actuator/health` |
| frontend | **5173** | `HTTP` `/` |

4. **リスナールール**: `Path /api/*` → バックエンド TG、デフォルト → フロント TG。
5. **Route 53** でホストゾーンに ALB へのエイリアスレコードを作成。
6. メール内リンク用に **`APP_FRONTEND_BASE_URL`** を本番 URL に設定（起動スクリプトまたは compose の環境変数）。

---

## 10. 日常のデプロイ（手動 SSH）

1. 開発者が **`git tag vX.Y.Z`** を push（または main の運用に合わせる）。
2. **GitHub Actions** が OIDC で **ECR にイメージを push**（ワークフローはプロダクトで追加。[ADR-0026](../adr/0026-cicd-without-iac.md)）。
3. 運用者が EC2 に **SSH** し、以下を実行:

```bash
cd /opt/myproduct
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.ap-northeast-1.amazonaws.com
docker compose pull
docker compose up -d
```

4. ALB ターゲットが **healthy** であることを確認。

---

## 11. リリース前チェックリスト

- [ ] ALB ヘルス: バック `/actuator/health`、フロント `/`
- [ ] ログイン・リフレッシュ Cookie（SameSite / Secure / ドメイン）
- [ ] RDS 接続（プールサイズ、タイムアウト）
- [ ] SES 送信（サンドボックス解除済みか）
- [ ] CloudWatch アラーム（ALB 5xx、ターゲット不健全、EC2 StatusCheck）
- [ ] バックアップ（RDS スナップショット保持期間）

---

## 参考

- [aws-infrastructure-best-practices.md](aws-infrastructure-best-practices.md)
- [Amazon ECR 構築・運用ガイド](../aws-ecr-setup.md)
- [ADR 一覧](../adr/README.md)
