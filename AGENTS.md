# Agent 向けガイド（KizuNavi）

このリポジトリで作業する AI / エージェントは、次を守ってください。

1. **応答言語**: ユーザー向けの説明・コメント・ドキュメントは **日本語** を基本とする。
2. **詳細ルール**: [`.cursor/rules/`](.cursor/rules/) の `.mdc` を領域に応じて参照する（常時適用の概要は `00-project-overview.mdc`）。
3. **API 契約**: 変更の単一ソースはルートの **`swagger.yaml`**。フロントは `npm run generate:api`、バックエンドは `./gradlew openApiGenerate` と `copyGeneratedSources`。生成コードは手編集しない。
4. **DB**: **Oracle（AWS RDS）** のみ。H2 等のインメモリ DBは使わない。プロファイルは `dev` / `prod` のみ。
5. **設計変更**: 方針が変わる場合は [`docs/adr/README.md`](docs/adr/README.md) を確認し、必要なら **新規 ADR** を追加する。
6. **ポート・複製**: [`docs/port-registry.md`](docs/port-registry.md) を参照する。
7. **単体テスト台帳**: 実施項目・観点は [`docs/test/README.md`](docs/test/README.md) で管理する。認証のテスト／実装変更時は [`auth-backend.md`](docs/test/auth-backend.md) / [`auth-frontend.md`](docs/test/auth-frontend.md) を同じ PR で更新する（[ADR-0024](docs/adr/0024-testing-strategy.md)、[`60-testing.mdc`](.cursor/rules/60-testing.mdc)）。
