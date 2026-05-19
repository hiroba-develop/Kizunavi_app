# KizuNavi

Spring Boot + React フルスタックテンプレートプロジェクトです。

**バックエンドの Spring プロファイルは `dev` と `prod` の 2 種類のみ**です。

- **`dev`**: 自 PC（`bootRun` / Docker Compose）と **AWS dev EC2** で共通。接続先は **dev 環境の AWS RDS Oracle・SES**（同一向き先）。
- **`prod`**: **AWS prod EC2** のみ。接続先は **prod 環境の RDS・SES**（スキーマ・リソースは dev と別）。

インメモリ DB（H2）は使用しません。**DB 接続情報と JWT 署名鍵は AWS Secrets Manager を正**として管理します。事前に [`.env.example`](.env.example) を `.env` にコピーし、`AWS_SECRETS_*` と `AWS_*` を設定してください。

起動場所によるログの詳しさなどは、`.env` の `LOG_LEVEL` / `JPA_SHOW_SQL` / `HIBERNATE_SQL_LOG` で調整します。

## プロジェクト構成

```
Product_Template/
├── backend/          Spring Boot REST API
├── frontend/         React + Vite SPA
└── docker-compose.yml
```

## Docker で起動

### 前提

1. ルートで `cp .env.example .env` を実行し、`SPRING_PROFILES_ACTIVE`（通常は `dev`）、`AWS_SECRETS_*`、`AWS_*` を記入する。
2. RDS Oracle にログイン用テーブルが未適用の場合は、DBA が事前に作成した **`template_app`** ユーザーで [`backend/src/main/resources/db/oracle/login_schema.sql`](backend/src/main/resources/db/oracle/login_schema.sql) を実行する（表領域・ユーザー作成の DDL はファイル先頭の `[実施済み]` コメントを参照。設計の詳細は [`docs/db/login-schema.md`](docs/db/login-schema.md)）。
3. 既定の Compose は **`SPRING_PROFILES_ACTIVE=dev`**（`.env` 未設定時も `docker-compose.yml` のデフォルトで `dev`）。

### 起動方法

| 起動場所 | コマンド | プロファイル |
|---------|---------|--------------|
| 自 PC（Docker） | `docker compose up --build` | `.env` の `SPRING_PROFILES_ACTIVE`（推奨: `dev`） |
| AWS dev EC2 | 起動スクリプト / `docker compose` で `SPRING_PROFILES_ACTIVE=dev` | `dev` |
| AWS prod EC2 | 起動スクリプト / `docker compose` で `SPRING_PROFILES_ACTIVE=prod` | `prod` |

```bash
cp .env.example .env
# .env を編集（Secrets Manager・SES 等）

docker compose up --build
```

| サービス | URL（ポート変更時は `.env` の `FRONTEND_PORT` / `BACKEND_PORT` に合わせる） |
|---------|-----|
| フロントエンド | http://localhost:5173 |
| バックエンド API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## ローカルで起動（自 PC、Docker なし）

### バックエンド

ルートの `.env` をプロセス環境に読み込んでから `bootRun` します（**dev 用 RDS / SES に接続**）。

初回のみ、RDS 上にログイン用オブジェクトが無い場合は [`backend/src/main/resources/db/oracle/login_schema.sql`](backend/src/main/resources/db/oracle/login_schema.sql) を **`template_app`** で実行してから起動する（前提・手順は上記「Docker で起動」の前提 2 と [`docs/db/login-schema.md`](docs/db/login-schema.md) を参照）。

**Windows (PowerShell):**

```powershell
cd backend
Get-Content ..\.env | ForEach-Object {
  if ($_ -match '^\s*#' -or $_ -match '^\s*$') { return }
  if ($_ -match '^(\w+)=(.*)$') {
    [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
  }
}
.\gradlew.bat bootRun
```

**macOS / Linux (bash):**

```bash
cd backend
set -a && source ../.env && set +a
./gradlew bootRun
```

`backend` 直下のみで作業する場合は `cp ../.env.example .env` を `backend/.env` にし、同様に `set -a; source .env` してから `./gradlew bootRun` でも構いません。

### フロントエンド

```bash
cd frontend
npm install
npm run dev
```

詳細はそれぞれの README を参照してください。

- [backend/README.md](backend/README.md)
- [frontend/README.md](frontend/README.md)

## ドキュメント索引

### アーキテクチャ意思決定（ADR）

設計方針の記録は [`docs/adr/`](docs/adr/README.md) にあります（技術スタック・AWS 方針の一覧）。

### テスト戦略

- ADR: [単体テスト戦略（C0 / JUnit 5 / Vitest）](docs/adr/0024-testing-strategy.md)
- 初学者向けガイド: [テスト入門ガイド（単体テスト / C0）](docs/guides/testing-guide.md)

### 運用手順（EC2 / インフラ）

| ドキュメント | 内容 |
|-------------|------|
| [新規プロダクトの EC2 + Docker リリース](docs/ops/ec2-new-product-release.md) | テンプレート複製後の初回 AWS リリース手順 |
| [AWS インフラ整備ベストプラクティス](docs/ops/aws-infrastructure-best-practices.md) | VPC/ALB/監視/シークレット等の総論とチェックリスト |

### ローカルポート採番

テンプレート複製時のポート衝突回避: [ポート採番台帳（Google スプレッドシート）](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0)（割当の正）／手順・ルールは [`docs/port-registry.md`](docs/port-registry.md)

## AWS 設定ガイド

自 PC・Docker・EC2 いずれでも **RDS / SES を利用する前に**、以下を参照してください。

| ドキュメント | 内容 |
|-------------|------|
| [AWS RDS Oracle 接続設定](docs/aws-rds-oracle-setup.md) | RDS Oracle への接続設定、環境変数の設定方法 |
| [AWS SES 設定ガイド](docs/aws-ses-setup.md) | メール送信機能の設定、IAM ポリシーの作成方法 |
| [AWS Secrets Manager 設定](docs/aws-secrets-manager-setup.md) | シークレットの集約、EC2 + Docker での連携例 |
