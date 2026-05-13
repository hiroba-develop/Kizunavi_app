# ADR-0002: モノレポ構成（backend / frontend）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

フルスタックの雛形として、API と SPA を**同一リポジトリ**で管理するか、別リポジトリにするかを決める必要がある。

## Decision

- **単一リポジトリ（モノレポ）**とする。
- ルートに [`docker-compose.yml`](../../docker-compose.yml) を置き、`backend/` と `frontend/` を同時に起動できるようにする。
- **API 契約**はリポジトリルートの [`swagger.yaml`](../../swagger.yaml) を単一ソースとし、バックエンド・フロントの双方から参照する。

## Consequences

- Positive: バージョン整合（OpenAPI と実装）を取りやすい。クローン1回で全体を再現できる。
- Negative: リポジトリが肥大化しうる。権限分離（フロントのみ触るメンバー）には Git の path ベース権限や CODEOWNERS で補う。

## Alternatives Considered

- **マルチリポ（backend / frontend 分割）**: チーム分業には有利だが、テンプレートの複製コストと契約同期コストが増える → テンプレート用途では却下。

## References

- [README.md](../../README.md)
- [ADR-0006](0006-api-contract-openapi.md)
