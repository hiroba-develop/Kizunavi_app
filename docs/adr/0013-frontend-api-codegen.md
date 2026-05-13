# ADR-0013: フロント API 型生成（openapi-typescript-codegen）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

OpenAPI 契約から TypeScript の型とサービス呼び出しを生成する手段を統一する。リポジトリ内に **`openapitools.json`**（OpenAPI Generator CLI 想定）と **`npm run generate:api`**（openapi-typescript-codegen）の二系統が存在しうる。

## Decision

- **正の生成コマンド**: `npm run generate:api` — **openapi-typescript-codegen**、入力はルートの [`swagger.yaml`](../../swagger.yaml)、出力は **`frontend/src/api/`**（[`frontend/package.json`](../../frontend/package.json)）。
- **ESLint / Prettier**: `src/api/generated` を ignore する記述があるが、**現行出力は `src/api` 直下**の場合がある。生成物は原則 **手編集しない**。
- **`openapitools.json`**: 歴史的・代替パス（`typescript-axios` → `./src/api/generated`）の名残として残っていてもよいが、**新規作業は `generate:api` に統一**する。不要になれば削除を検討。

## Consequences

- Positive: バックエンド契約と型の同期が `swagger.yaml` 更新 + 再生成で完結しやすい。
- Negative: ランタイムの HTTP は `axios.ts` 直書きと生成 `*Service` が併存しうる → 徐々に生成サービスへ寄せるか、方針をプロダクトで固定。

## Alternatives Considered

- **公式 openapi-generator の TypeScript axios のみ**: 設定と出力の扱いが重い場合がある → 現行は openapi-typescript-codegen を採用。

## References

- [`frontend/openapitools.json`](../../frontend/openapitools.json)
- [ADR-0006](0006-api-contract-openapi.md)
