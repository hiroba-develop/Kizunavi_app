# Architecture Decision Records (ADR)

本ディレクトリには、KizuNavi の**アーキテクチャ上の重要な意思決定**を記録した ADR を置きます。

## 目的

- 採用技術・インフラ方針の**背景とトレードオフ**を後から追えるようにする
- テンプレートを複製したプロダクトでも、判断の根拠を共有できるようにする

## フォーマット

各 ADR は [MADR](https://adr.github.io/madr/) に準拠した次のセクションを持ちます。

- **Context** … 判断が必要になった理由・制約
- **Decision** … 採用した方針（バージョン・設定ファイルへの参照を含む）
- **Consequences** … メリット / デメリット・運用上の注意
- **Alternatives Considered** … 検討したが採用しなかった案
- **References** … リポジトリ内パス・外部リンク

## メタデータ

各ファイル先頭に次を記載します。

- `Status`: 通常 `Accepted`（廃止時は `Deprecated` と代替 ADR へのリンク）
- `Date`: 記録日（YYYY-MM-DD）
- `Deciders`: チーム名など（任意）

## インデックス

| ID | タイトル |
|----|----------|
| [0001](0001-adr-process.md) | ADR の運用プロセス |
| [0002](0002-monorepo-layout.md) | モノレポ構成（backend / frontend） |
| [0003](0003-backend-language-framework.md) | バックエンド言語・フレームワーク（Java 21 / Spring Boot） |
| [0004](0004-backend-database-strategy.md) | データベース戦略（Oracle RDS / dev・prod プロファイル / ddl-auto） |
| [0005](0005-auth-jwt-cookie.md) | 認証（JWT + HttpOnly リフレッシュ Cookie） |
| [0006](0006-api-contract-openapi.md) | API 契約と OpenAPI（swagger.yaml / SpringDoc / Generator） |
| [0007](0007-backend-libraries.md) | バックエンド補助ライブラリ（Lombok / Validation / Actuator / SES） |
| [0008](0008-backend-testing.md) | バックエンドテスト戦略 |
| [0009](0009-frontend-foundation.md) | フロントエンド基盤（React / Vite / TypeScript） |
| [0010](0010-frontend-ui-styling.md) | UI とスタイリング（Tailwind / shadcn / Radix） |
| [0011](0011-frontend-data-layer.md) | データ層（TanStack Query / Zustand / Axios） |
| [0012](0012-frontend-routing-forms.md) | ルーティングとフォーム（React Router / RHF / Zod） |
| [0013](0013-frontend-api-codegen.md) | フロント API 型生成（openapi-typescript-codegen） |
| [0014](0014-code-quality.md) | コード品質（ESLint / Prettier） |
| [0015](0015-container-strategy.md) | コンテナ戦略（Docker / Vite preview） |
| [0016](0016-port-numbering-policy.md) | ポート採番と台帳運用 |
| [0017](0017-aws-runtime-ecs-fargate.md) | AWS ランタイム（ECS on Fargate）**(Deprecated)** |
| [0018](0018-aws-network-topology.md) | AWS ネットワーク（VPC / ALB / Route53 / ACM） |
| [0019](0019-aws-data-layer.md) | AWS データ層（RDS Oracle 等） |
| [0020](0020-aws-secrets-management.md) | シークレット管理（Secrets Manager） |
| [0021](0021-aws-observability.md) | 可観測性（CloudWatch / Container Insights） |
| [0022](0022-aws-cicd-iac.md) | CI/CD と IaC（GitHub Actions / ECR / Terraform）**(Deprecated)** |
| [0025](0025-aws-runtime-ec2-docker.md) | AWS ランタイム（EC2 + Docker） |
| [0026](0026-cicd-without-iac.md) | CI/CD 方針（GitHub Actions / ECR、IaC は見送り） |
| [0023](0023-login-db-schema.md) | ログイン機能 DB 設計（小規模向け） |
| [0024](0024-testing-strategy.md) | 単体テスト戦略（C0 / JUnit 5 / Vitest） |

## 関連ドキュメント

- 運用手順: [docs/ops/](../ops/)
- ポート台帳（正）: [Google スプレッドシート](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0)／ルール: [docs/port-registry.md](../port-registry.md)
