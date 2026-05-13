# AWS Secrets Manager 設定ガイド

このドキュメントでは、AWS Secrets Manager を使用してアプリケーションの機密情報（DB パスワード、JWT シークレット、AWS アクセスキー等）を安全に管理する方法を説明します。

## 目次

1. [概要と導入メリット](#1-概要と導入メリット)
2. [AWS Secrets Manager でのシークレット作成](#2-aws-secrets-manager-でのシークレット作成)
3. [IAM ポリシーの設定](#3-iam-ポリシーの設定)
4. [EC2 + Docker での利用（本テンプレートの前提）](#4-ec2--docker-での利用本テンプレートの前提)
5. [Spring Cloud AWS による直接統合](#5-spring-cloud-aws-による直接統合)
6. [ローカル開発との併用](#6-ローカル開発との併用)
7. [シークレットのローテーション](#7-シークレットのローテーション)
8. [トラブルシューティング](#8-トラブルシューティング)
9. [（参考）ECS タスク定義でのシークレット注入](#9-参考ecs-タスク定義でのシークレット注入)

---

## 1. 概要と導入メリット

### 1.1 現在の構成と課題

現在、機密情報は `.env` ファイルで管理されています。

```bash
# .env（Git 管理外）
JWT_SECRET=your-256-bit-secret-key-here...
DB_PASSWORD=template_app_pass0426
AWS_ACCESS_KEY=AKIAXXXXXXXXXXXXXXXX
AWS_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

**課題:**

| 課題 | 影響 |
|------|------|
| `.env` ファイルの手動管理 | 環境ごとに手作業でコピー・編集が必要 |
| シークレットの共有 | Slack やメールでの共有はセキュリティリスク |
| ローテーション | パスワード変更時にすべての環境で手動更新が必要 |
| 監査 | 誰がいつシークレットにアクセスしたか追跡できない |

### 1.2 Secrets Manager の導入メリット

| メリット | 説明 |
|----------|------|
| 一元管理 | AWS コンソールまたは CLI で全環境のシークレットを管理 |
| アクセス制御 | IAM ポリシーで細かく制御 |
| 自動ローテーション | Lambda を使った定期的なパスワード変更 |
| 監査ログ | CloudTrail で全アクセスを記録 |
| 暗号化 | KMS による保存時暗号化 |

### 1.3 管理対象のシークレット

本プロジェクトで Secrets Manager に移行する対象は以下のとおりです。

| 環境変数 | 用途 | 機密レベル |
|----------|------|-----------|
| `DB_URL` | Oracle RDS 接続 URL | 中 |
| `DB_USERNAME` | データベースユーザー名 | 中 |
| `DB_PASSWORD` | データベースパスワード | **高** |
| `JWT_SECRET` | JWT 署名キー | **高** |
| `AWS_ACCESS_KEY` | SES 用アクセスキー | **高** |
| `AWS_SECRET_KEY` | SES 用シークレットキー | **高** |

> **注意**: `SPRING_PROFILES_ACTIVE` や `HIKARI_MAX_POOL_SIZE` のような非機密の設定値は Secrets Manager に含めず、環境変数やパラメータストアで管理するのが一般的です。

---

## 2. AWS Secrets Manager でのシークレット作成

### 2.1 シークレットの命名規則

環境ごとにプレフィックスを付けて管理します。

```
kizuNavi/{環境}/{シークレット種別}
```

**例:**

| シークレット名 | 用途 |
|---------------|------|
| `kizuNavi/dev/db-credentials` | 開発環境の DB 接続情報 |
| `kizuNavi/prod/db-credentials` | 本番環境の DB 接続情報 |
| `kizuNavi/dev/app-secrets` | 開発環境のアプリ秘密情報 |
| `kizuNavi/prod/app-secrets` | 本番環境のアプリ秘密情報 |

### 2.2 AWS CLI でのシークレット作成

#### DB 接続情報

```bash
# 開発環境
aws secretsmanager create-secret \
  --name "kizuNavi/dev/db-credentials" \
  --description "KizuNavi 開発環境 DB 接続情報" \
  --secret-string '{
    "DB_URL": "jdbc:oracle:thin:@//dev-db.xxxxxxxxxxxx.ap-northeast-1.rds.amazonaws.com:1521/ORCL",
    "DB_USERNAME": "template_app",
    "DB_PASSWORD": "dev-password-here"
  }' \
  --region ap-northeast-1

# 本番環境
aws secretsmanager create-secret \
  --name "kizuNavi/prod/db-credentials" \
  --description "KizuNavi 本番環境 DB 接続情報" \
  --secret-string '{
    "DB_URL": "jdbc:oracle:thin:@//prod-db.xxxxxxxxxxxx.ap-northeast-1.rds.amazonaws.com:1521/ORCL",
    "DB_USERNAME": "template_app",
    "DB_PASSWORD": "prod-password-here"
  }' \
  --region ap-northeast-1
```

#### アプリケーション秘密情報

```bash
# 開発環境
aws secretsmanager create-secret \
  --name "kizuNavi/dev/app-secrets" \
  --description "KizuNavi 開発環境アプリ秘密情報" \
  --secret-string '{
    "JWT_SECRET": "dev-jwt-secret-must-be-at-least-32-characters-long",
    "AWS_ACCESS_KEY": "AKIAXXXXXXXXXXXXXXXX",
    "AWS_SECRET_KEY": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  }' \
  --region ap-northeast-1

# 本番環境
aws secretsmanager create-secret \
  --name "kizuNavi/prod/app-secrets" \
  --description "KizuNavi 本番環境アプリ秘密情報" \
  --secret-string '{
    "JWT_SECRET": "prod-jwt-secret-must-be-at-least-32-characters-long",
    "AWS_ACCESS_KEY": "AKIAXXXXXXXXXXXXXXXX",
    "AWS_SECRET_KEY": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  }' \
  --region ap-northeast-1
```

### 2.3 AWS マネジメントコンソールでの作成

1. **AWS マネジメントコンソール** にログイン
2. **Secrets Manager** サービスに移動
3. **新しいシークレットを保存する** をクリック
4. **シークレットのタイプ**: 「その他のシークレット」を選択
5. キー/値のペアを入力:

| キー | 値 (例) |
|------|---------|
| `DB_URL` | `jdbc:oracle:thin:@//your-rds-endpoint:1521/ORCL` |
| `DB_USERNAME` | `template_app` |
| `DB_PASSWORD` | `your-secure-password` |

6. **シークレットの名前**: `kizuNavi/prod/db-credentials`
7. **説明**: `KizuNavi 本番環境 DB 接続情報`
8. **保存** をクリック

### 2.4 シークレットの更新

```bash
aws secretsmanager update-secret \
  --secret-id "kizuNavi/prod/db-credentials" \
  --secret-string '{
    "DB_URL": "jdbc:oracle:thin:@//new-endpoint:1521/ORCL",
    "DB_USERNAME": "template_app",
    "DB_PASSWORD": "new-password-here"
  }' \
  --region ap-northeast-1
```

### 2.5 シークレットの確認

```bash
# シークレットの値を取得
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/prod/db-credentials" \
  --region ap-northeast-1 \
  --query 'SecretString' \
  --output text | jq .

# シークレットの一覧を表示
aws secretsmanager list-secrets \
  --filter Key=name,Values=kizuNavi \
  --region ap-northeast-1
```

---

## 3. IAM ポリシーの設定

### 3.1 シークレット読み取り用ポリシー

アプリケーションが Secrets Manager からシークレットを取得するための IAM ポリシーを作成します。

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowGetSecrets",
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": [
        "arn:aws:secretsmanager:ap-northeast-1:123456789012:secret:kizuNavi/prod/*"
      ]
    }
  ]
}
```

> **注意**: `123456789012` は実際の AWS アカウント ID に置き換えてください。

**環境ごとにアクセスを分離する場合:**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowGetDevSecrets",
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": [
        "arn:aws:secretsmanager:ap-northeast-1:123456789012:secret:kizuNavi/dev/*"
      ],
      "Condition": {
        "StringEquals": {
          "aws:RequestedRegion": "ap-northeast-1"
        }
      }
    }
  ]
}
```

### 3.2 ポリシーの作成手順

1. **IAM** コンソールに移動
2. **ポリシー** → **ポリシーを作成**
3. **JSON** タブで上記の JSON を貼り付け
4. ポリシー名: `KizuNavi-SecretsManager-ReadOnly`
5. **ポリシーを作成**

### 3.3 KMS カスタムキーを使用する場合

Secrets Manager でカスタム KMS キーを使用している場合は、KMS の復号権限も必要です。

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowGetSecrets",
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": [
        "arn:aws:secretsmanager:ap-northeast-1:123456789012:secret:kizuNavi/prod/*"
      ]
    },
    {
      "Sid": "AllowDecryptWithKMS",
      "Effect": "Allow",
      "Action": [
        "kms:Decrypt"
      ],
      "Resource": [
        "arn:aws:kms:ap-northeast-1:123456789012:key/your-kms-key-id"
      ]
    }
  ]
}
```

---

## 4. EC2 + Docker での利用（本テンプレートの前提）

本テンプレートは **EC2 上で `docker compose`** を動かすことを前提とします。ECS のタスク定義 `secrets` は使わず、**起動前に Secrets Manager から値を取得し、環境変数として Docker に渡す**運用とします（詳細は [ADR-0020](../adr/0020-aws-secrets-management.md)、[docs/ops/ec2-new-product-release.md](../ops/ec2-new-product-release.md)）。

### 4.1 EC2 インスタンスプロファイルの設定

1. IAM ロールを作成し、セクション 3 のシークレット読み取りポリシーをアタッチする。
2. EC2 インスタンスプロファイルにロールを関連付ける。
3. EC2 上に **AWS CLI** と **jq** をインストールする（Amazon Linux 2023 なら `dnf install -y jq aws-cli` 等）。

### 4.2 シークレットを環境ファイルに書き出す（docker compose 向け）

`/opt/myproduct/load-secrets.sh` の例（**権限 700**、`root` または `docker` グループのみ実行可とする運用を推奨）:

```bash
#!/bin/bash
set -euo pipefail
REGION="ap-northeast-1"
ENV="prod"
OUT="/run/myproduct.compose.env"
umask 077

get_secret() {
  aws secretsmanager get-secret-value \
    --secret-id "$1" \
    --region "$REGION" \
    --query 'SecretString' \
    --output text
}

DB_CREDS=$(get_secret "kizuNavi/${ENV}/db-credentials")
APP_SECRETS=$(get_secret "kizuNavi/${ENV}/app-secrets")

{
  echo "DB_URL=$(echo "$DB_CREDS" | jq -r '.DB_URL')"
  echo "DB_USERNAME=$(echo "$DB_CREDS" | jq -r '.DB_USERNAME')"
  echo "DB_PASSWORD=$(echo "$DB_CREDS" | jq -r '.DB_PASSWORD')"
  echo "JWT_SECRET=$(echo "$APP_SECRETS" | jq -r '.JWT_SECRET')"
  echo "AWS_ACCESS_KEY=$(echo "$APP_SECRETS" | jq -r '.AWS_ACCESS_KEY')"
  echo "AWS_SECRET_KEY=$(echo "$APP_SECRETS" | jq -r '.AWS_SECRET_KEY')"
  echo "SPRING_PROFILES_ACTIVE=${ENV}"
  # 非機密の環境変数は別途 echo するか、compose の environment に記載
} > "$OUT"

chmod 600 "$OUT"
```

`docker-compose.yml` で `env_file: - /run/myproduct.compose.env` を指定するか、`docker compose --env-file /run/myproduct.compose.env up -d` で起動します。**機密ファイルは `/run` に置き再起動で消える**ため、ブート時に必ず `ExecStartPre` で再生成してください。

### 4.3 systemd + docker compose の例

```ini
[Unit]
Description=KizuNavi (docker compose)
After=docker.service network-online.target
Requires=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/myproduct
ExecStartPre=/opt/myproduct/load-secrets.sh
ExecStart=/usr/bin/docker compose --env-file /run/myproduct.compose.env up -d
ExecStop=/usr/bin/docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
```

### 4.4 application.yml との対応

コンテナに渡した環境変数は、`application.yml` のプレースホルダーにそのまま適用されます。

```
Secrets Manager（キー）                  application.yml
──────────────────────                  ──────────────────────────────────
db-credentials.DB_URL            →     ${DB_URL:jdbc:oracle:thin:@...}
db-credentials.DB_USERNAME       →     ${DB_USERNAME:template_app}
db-credentials.DB_PASSWORD       →     ${DB_PASSWORD:template_app_pass0426}
app-secrets.JWT_SECRET           →     ${JWT_SECRET:your-256-bit...}
app-secrets.AWS_ACCESS_KEY       →     ${AWS_ACCESS_KEY:}
app-secrets.AWS_SECRET_KEY       →     ${AWS_SECRET_KEY:}
```

`application.yml` 自体の変更は不要です。

### 4.5 （任意）JAR を直接起動する場合

コンテナを使わず EC2 上で `java -jar` だけ実行する場合は、従来どおりシェルで `export` してから起動します。

```bash
#!/bin/bash
# … §4.2 と同様に export したうえで …
java -jar /opt/app/kizuNavi.jar
```

---

## 5. Spring Cloud AWS による直接統合

アプリケーション内から直接 Secrets Manager を参照する方法です。起動スクリプトでのマッピングが不要になります。

### 5.1 依存関係の追加

`backend/build.gradle` に以下を追加します。

```groovy
dependencies {
    // 既存の依存関係に追加
    implementation 'io.awspring.cloud:spring-cloud-aws-starter-secrets-manager:3.2.1'
}

dependencyManagement {
    imports {
        mavenBom 'io.awspring.cloud:spring-cloud-aws-dependencies:3.2.1'
    }
}
```

### 5.2 application.yml の設定

```yaml
spring:
  config:
    import: optional:aws-secretsmanager:kizuNavi/${spring.profiles.active}/db-credentials;kizuNavi/${spring.profiles.active}/app-secrets

  cloud:
    aws:
      secrets-manager:
        region: ap-northeast-1
```

`optional:` プレフィックスを付けることで、Secrets Manager に未到達の環境でも起動エラーを避けられます（本テンプレートの通常起動は **`.env` で RDS/SES を渡す dev プロファイル**を前提とします）。

### 5.3 プロパティ名のマッピング

Secrets Manager のキー名がそのまま Spring のプロパティとして利用できます。

| Secrets Manager キー | 対応するプロパティ |
|---------------------|-------------------|
| `DB_URL` | `${DB_URL}` |
| `DB_USERNAME` | `${DB_USERNAME}` |
| `DB_PASSWORD` | `${DB_PASSWORD}` |
| `JWT_SECRET` | `${JWT_SECRET}` |

### 5.4 この方法のメリット・デメリット

| 項目 | 内容 |
|------|------|
| メリット | 起動スクリプトへの記述が不要 |
| メリット | シークレット更新時にアプリ再起動で反映 |
| デメリット | Spring Cloud AWS への依存が増える |
| デメリット | アプリ起動時に Secrets Manager への通信が発生（起動時間に影響） |

---

## 6. ローカル開発との併用

自 PC（`bootRun`）および Docker Compose では、**まずルートの `.env`** に dev 用の `DB_*` / `JWT_SECRET` / `AWS_*` を記載し、**`SPRING_PROFILES_ACTIVE=dev`** で起動する運用を推奨します。Secrets Manager は **AWS EC2** での参照を主とし、ローカルでは任意です。

### 6.1 環境別のシークレット管理方針

| 起動場所 | シークレット管理方法 | 詳細 |
|----------|---------------------|------|
| **自 PC（bootRun / IDE）** | `.env`（推奨） | ルート `cp .env.example .env` で dev 用 RDS・SES を設定 |
| **Docker Compose** | `.env`（推奨） | Compose が環境変数としてバックエンドコンテナに渡す |
| **AWS dev EC2** | Secrets Manager + 起動スクリプト | `kizuNavi/dev/*` 等 |
| **AWS prod EC2** | Secrets Manager + 起動スクリプト | `kizuNavi/prod/*` 等 |

### 6.2 自 PC での起動（`.env` 必須）

H2 は使用しません。RDS / SES への接続情報がないと起動できません。

```bash
cp .env.example .env
# .env を編集後
cd backend
set -a && source ../.env && set +a   # bash の例
./gradlew bootRun
```

### 6.3 dev 環境の Secrets Manager を参照したい場合

ローカルから dev 環境のシークレットを使いたい場合：

```bash
# AWS CLI でシークレットを取得して .env に書き出し
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/dev/db-credentials" \
  --region ap-northeast-1 \
  --query 'SecretString' \
  --output text | jq -r 'to_entries[] | "\(.key)=\(.value)"' > .env

aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/dev/app-secrets" \
  --region ap-northeast-1 \
  --query 'SecretString' \
  --output text | jq -r 'to_entries[] | "\(.key)=\(.value)"' >> .env

echo "SPRING_PROFILES_ACTIVE=dev" >> .env
```

> **注意**: この方法で生成した `.env` ファイルは `.gitignore` で除外されているため、Git にコミットされません。

---

## 7. シークレットのローテーション

### 7.1 手動ローテーション

```bash
# 新しいパスワードでシークレットを更新
aws secretsmanager update-secret \
  --secret-id "kizuNavi/prod/db-credentials" \
  --secret-string '{
    "DB_URL": "jdbc:oracle:thin:@//prod-db.xxxxxxxxxxxx.ap-northeast-1.rds.amazonaws.com:1521/ORCL",
    "DB_USERNAME": "template_app",
    "DB_PASSWORD": "new-secure-password"
  }' \
  --region ap-northeast-1

# EC2 + Docker の場合: シークレット再取得後にコンテナを再起動
sudo systemctl restart myproduct-compose.service
# または手動で `docker compose up -d --force-recreate`
```

### 7.2 自動ローテーション（RDS）

RDS の DB パスワードを自動ローテーションする設定です。

#### Lambda ローテーション関数の設定

1. **Secrets Manager** コンソールで対象シークレットを選択
2. **ローテーション設定** → **ローテーションを編集**
3. **自動ローテーションを有効にする**
4. ローテーションスケジュール: 例）30日ごと
5. ローテーション関数: **新しい Lambda 関数を作成** を選択
6. Lambda 関数名: `kizuNavi-secret-rotation`

#### ローテーション時の注意事項

| 項目 | 対策 |
|------|------|
| アプリの接続断 | ローテーション中にコネクションプールが新しい認証情報を取得する仕組みが必要 |
| ローテーションの失敗 | CloudWatch Alarm でローテーション失敗を監視 |
| テスト | ステージング環境で事前にローテーションを検証 |

### 7.3 バージョニング

Secrets Manager は自動的にバージョンを管理します。

```bash
# 現在のバージョンを確認
aws secretsmanager describe-secret \
  --secret-id "kizuNavi/prod/db-credentials" \
  --region ap-northeast-1

# 以前のバージョンの値を取得（ロールバック時）
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/prod/db-credentials" \
  --version-stage AWSPREVIOUS \
  --region ap-northeast-1
```

---

## 8. トラブルシューティング

### 8.1 シークレットを取得できない

**エラー:** `AccessDeniedException: User is not authorized to perform: secretsmanager:GetSecretValue`

**対処:**
- IAM ポリシーの `Resource` ARN が正しいか確認
- シークレット名のプレフィックス（`kizuNavi/prod/`）が一致しているか確認
- リージョンが一致しているか確認

```bash
# 権限の確認
aws sts get-caller-identity
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/prod/db-credentials" \
  --region ap-northeast-1
```

### 8.2 EC2 で Secrets を取得できない

**症状:** `load-secrets.sh` が失敗する、`AccessDeniedException`

**対処:**
- **インスタンスプロファイル**に付いた IAM ロールに `secretsmanager:GetSecretValue` があるか確認
- シークレット ARN / 名前が環境と一致しているか確認
- **プライベートサブネット**の EC2 からインターネットに出られない場合は、**NAT Gateway** または **Secrets Manager 用 VPC エンドポイント**（Interface）を検討

```bash
# Secrets Manager 用 VPC エンドポイントの作成例（プライベートサブネットから API へ届かない場合）
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-xxxxxxxxx \
  --service-name com.amazonaws.ap-northeast-1.secretsmanager \
  --vpc-endpoint-type Interface \
  --subnet-ids subnet-xxxxxxxx subnet-yyyyyyyy \
  --security-group-ids sg-xxxxxxxx
```

### 8.3 シークレットの JSON キーが見つからない

**エラー:** コンテナ内の環境変数が空になる

**対処:**
- `load-secrets.sh` の `jq -r '.キー名'` が正しいか確認
- JSON キー名が大文字・小文字を含め正確に一致しているか確認

```bash
# シークレットの内容を確認
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/prod/db-credentials" \
  --region ap-northeast-1 \
  --query 'SecretString' \
  --output text | jq .
```

### 8.4 Spring Cloud AWS で起動に失敗する

**エラー:** `Could not resolve placeholder 'DB_URL'`

**対処:**
- `spring.config.import` に `optional:` プレフィックスが付いているか確認
- AWS 認証情報（`~/.aws/credentials` または環境変数）が設定されているか確認
- 自 PC では `.env` に `DB_URL` 等が設定されているか、または `optional:` で Secrets をスキップできるかを確認

### 8.5 ローテーション後に接続できなくなった

**対処:**
1. シークレットの現在の値を確認

```bash
aws secretsmanager get-secret-value \
  --secret-id "kizuNavi/prod/db-credentials" \
  --region ap-northeast-1
```

2. RDS のパスワードが実際に更新されているか確認
3. 必要に応じて以前のバージョンにロールバック

```bash
aws secretsmanager update-secret-version-stage \
  --secret-id "kizuNavi/prod/db-credentials" \
  --version-stage AWSCURRENT \
  --move-to-version-id <previous-version-id> \
  --remove-from-version-id <current-version-id> \
  --region ap-northeast-1
```

---

## 9. （参考）ECS タスク定義でのシークレット注入

本テンプレートの既定運用は **EC2 + Docker（§4）** です。**ECS on Fargate** でタスク定義の `secrets` フィールドから Secrets Manager を注入する方式は採用しません。別プロジェクトで ECS を使う場合の公式手順は [Specifying sensitive data - Secrets](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/specifying-sensitive-data-secrets.html) を参照してください。

---

## チェックリスト

### 初期セットアップ

- [ ] シークレットの命名規則を決定（`kizuNavi/{env}/{type}`）
- [ ] 各環境のシークレットを Secrets Manager に作成
- [ ] IAM ポリシーを作成し、最小権限の原則に従って設定
- [ ] EC2 インスタンスプロファイルに Secrets Manager 読み取りポリシーをアタッチ
- [ ] プライベートサブネットのみの場合、NAT または Secrets Manager 用 VPC エンドポイントで API に到達できることを確認

### デプロイ方式別

**EC2 + Docker（本テンプレート）:**

- [ ] §4 のとおり `load-secrets.sh` 等で環境変数を生成し、`docker compose` に渡す
- [ ] インスタンスプロファイルに Secrets Manager 読み取り権限を付与
- [ ] EC2 に **jq** と **AWS CLI** が利用可能

**Spring Cloud AWS の場合:**

- [ ] `spring-cloud-aws-starter-secrets-manager` 依存関係を追加
- [ ] `spring.config.import` に `optional:` プレフィックスを設定
- [ ] ローカル開発時に AWS 未接続でも起動できることを確認

### 運用

- [ ] CloudTrail でシークレットアクセスの監査ログを確認
- [ ] ローテーションスケジュールを設定（推奨: 30〜90日ごと）
- [ ] ローテーション失敗時の CloudWatch Alarm を設定
- [ ] `.env` ファイルの機密情報が Git にコミットされていないことを確認

---

## コスト

| 項目 | 料金（東京リージョン） |
|------|----------------------|
| シークレット 1 件あたり | $0.40/月 |
| API コール 10,000 件あたり | $0.05 |

本プロジェクトの場合（2シークレット × 2環境 = 4件）:
- 月額: 約 $1.60 + API コール費用

---

## 参考リンク

- [AWS Secrets Manager ユーザーガイド](https://docs.aws.amazon.com/ja_jp/secretsmanager/latest/userguide/intro.html)
- [ECS でのシークレット管理](https://docs.aws.amazon.com/ja_jp/AmazonECS/latest/developerguide/specifying-sensitive-data-secrets.html)
- [Spring Cloud AWS - Secrets Manager](https://docs.awspring.io/spring-cloud-aws/docs/3.0.0/reference/html/index.html#secrets-manager-integration)
- [シークレットの自動ローテーション](https://docs.aws.amazon.com/ja_jp/secretsmanager/latest/userguide/rotating-secrets.html)
