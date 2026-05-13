# ADR-0020: シークレット管理（AWS Secrets Manager）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

`.env` や EC2 上の平文環境変数では、**JWT 秘密鍵・DB パスワード・AWS キー**の漏えいリスクとローテーション運用が困難になる。

## Decision

- **AWS Secrets Manager** をシークレットの**正**とする（命名例: `product-name/{env}/database` 等は [`docs/aws-secrets-manager-setup.md`](../aws-secrets-manager-setup.md) を参照）。
- **EC2 + Docker**: 起動スクリプト（**systemd `ExecStartPre`** 等）で `GetSecretValue` し、**環境変数化してから** `docker compose` を起動。IAM は **EC2 インスタンスプロファイル**で付与（アクセスキーをファイルに置かない）。
- **自 PC 開発**: `.env` でよいが、**本番（prod）値をコミットしない**。`dev` 用シークレットも Git に含めない。

## Consequences

- Positive: ローテーションと監査がしやすい。EC2 上でも IAM ロールで取得しやすい。
- Negative: API 呼び出し課金・レイテンシ。起動時取得の失敗時リトライ設計が必要。

## Alternatives Considered

- **Systems Manager Parameter Store（SecureString）**: コスト重視なら選択肢。Secrets Manager はローテーション統合が強い。
- **平文 .env on EC2**: 一時的には許容されうるが、**Secrets Manager へ移行**を推奨。

## References

- [`docs/aws-secrets-manager-setup.md`](../aws-secrets-manager-setup.md)
