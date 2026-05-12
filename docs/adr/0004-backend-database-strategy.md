# ADR-0004: データベース戦略（Oracle RDS / ddl-auto）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

本番および開発の一貫した検証のため、**常に Oracle（AWS RDS）** に接続する方針にした。オフライン用のインメモリ DB（H2）は保守コストと実環境乖離のため採用しない。

## Decision

- **DB 製品**: **Oracle** のみ。JDBC は `ojdbc11`、Hibernate は `OracleDialect`（[`backend/build.gradle`](../../backend/build.gradle)、[`application.yml`](../../backend/src/main/resources/application.yml)）。
- **Spring プロファイルは `dev` と `prod` の 2 種類**:
  - **`dev`**: 自 PC（`bootRun` / Docker Compose）と **AWS dev EC2** で共通。**接続先は dev 環境の RDS**（同一向き先）。
  - **`prod`**: **AWS prod EC2** のみ。**接続先は prod 環境の RDS**（スキーマ・エンドポイントは dev と別）。
- **接続情報**: 環境変数 `DB_URL` / `DB_USERNAME` / `DB_PASSWORD`（`.env` または EC2 上の起動スクリプト / `docker compose` 経由）。
- **スキーマ変更**: **JPA `ddl-auto`** — `dev` では既定 **`update`**、`prod` では既定 **`validate`**（[`application-dev.yml`](../../backend/src/main/resources/application-dev.yml)、[`application-prod.yml`](../../backend/src/main/resources/application-prod.yml)）。**Flyway / Liquibase は未導入**。
- **H2**: **依存関係・設定・コードから完全削除**（テストでも使用しない）。

## Consequences

- Positive: ローカルとクラウドで **同一 DB 製品・同一 dev データ**に近い動作確認ができる。H2 特有の方言差がない。
- Negative: **オフライン開発不可**。全開発者が dev RDS へ到達できるネットワーク・認証が必須。`ddl-auto: update` は共有スキーマでの衝突リスクがあるため、運用ルールで補うか、後からマイグレーション導入を検討する。

## Alternatives Considered

- **H2 + local プロファイル（旧方式）**: 手軽だが本番乖離が大きい → **廃止**。
- **PostgreSQL 一本化**: Oracle 前提の要件と合わない場合がある → 本テンプレートは Oracle のまま。
- **Flyway / Liquibase**: 本番品質のスキーマ管理に有効 → 別 ADR で導入を検討。

## References

- [`docs/aws-rds-oracle-setup.md`](../aws-rds-oracle-setup.md)
- [`backend/build.gradle`](../../backend/build.gradle)（`ojdbc11` のみ）
