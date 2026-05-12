# ADR-0019: AWS データ層（RDS Oracle 等）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

本テンプレートの本番 DB は **Oracle** を想定している。AWS 上の配置と可用性・バックアップ方針を定める。

## Decision

- **Amazon RDS for Oracle**（または組織方針に合わせた Oracle 互換基盤）を推奨。
- **配置**: **プライベートサブネット**、セキュリティグループで **アプリ EC2 の SG から 1521 のみ**許可。
- **可用性**: 本番は **Multi-AZ** を推奨（メンテナンス・障害時の自動フェイルオーバー）。
- **バックアップ**: RDS の自動バックアップ保持期間を設定。リストア手順を Runbook に残す。
- **オブジェクトストレージ**: アプリ要件に応じて **S3**（アップロードファイル等）。本テンプレートの最小構成では必須としない。

## Consequences

- Positive: マネージド運用・スナップショット・暗号化（KMS）と組み合わせやすい。
- Negative: Oracle RDS のライセンス・コスト。開発も **dev 用 RDS** を共有する前提。

## Alternatives Considered

- **Aurora PostgreSQL へ移行**: 長期的コスト・運用は有利な場合があるが、**別プロダクトの意思決定**として ADR を分ける。

## References

- [`docs/aws-rds-oracle-setup.md`](../aws-rds-oracle-setup.md)
- [ADR-0004](0004-backend-database-strategy.md)
