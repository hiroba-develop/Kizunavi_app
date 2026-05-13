# ADR-0006: API 契約と OpenAPI（swagger.yaml / SpringDoc / Generator）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

フロントとバックの型・パスを同期するため、**契約ファースト**か**実装ファースト**か、およびドキュメント生成の手段を決める。

## Decision

- **契約の単一ソース**: リポジトリルートの [`swagger.yaml`](../../swagger.yaml)（OpenAPI 3.0.3）。
- **ランタイムドキュメント**: **SpringDoc OpenAPI**（`/v3/api-docs`、Swagger UI `/swagger-ui.html`）。設定は [`application.yml`](../../backend/src/main/resources/application.yml) の `springdoc.*`。
- **バックエンドコード生成**: Gradle **OpenAPI Generator** プラグイン（[`backend/build.gradle`](../../backend/build.gradle)）で `AuthApi` / `UsersApi` 等のインターフェース・DTO を生成可能。
- **フロント型・クライアント生成**: **openapi-typescript-codegen**（[`frontend/package.json`](../../frontend/package.json) の `generate:api`）。出力は主に `frontend/src/api/`。

## Consequences

- Positive: フロント・バックで同じ YAML を参照でき、レビューしやすい。
- Negative: **生成インターフェースと手書きコントローラの二重管理**になりうる。将来は `@Controller` が生成 `*Api` を実装する形への統一を推奨。

## Alternatives Considered

- **springdoc のみで YAML を生成し Git 管理しない**: PR レビューで契約差分が見えにくい → テンプレートでは YAML を正とする方針を採用。

## References

- [`swagger.yaml`](../../swagger.yaml)
- [ADR-0013](0013-frontend-api-codegen.md)
