# ADR-0009: フロントエンド基盤（React / Vite / TypeScript）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

SPA のビルドツールと型安全性のバランスを取り、テンプレート利用者にとって学習コストが過大にならないようにする。

## Decision

- **React 18** + **TypeScript**（[`frontend/package.json`](../../frontend/package.json)）。
- **Vite 6.x** をビルド・開発サーバーに採用（[`frontend/vite.config.ts`](../../frontend/vite.config.ts)）。
- **開発サーバーポート**: `5173`（明示）。**API プロキシ**: `/api` → `http://localhost:8080`（バックエンドポートは `BACKEND_PORT` と揃えてローカルでは `.env` や手動変更で対応可能）。
- **TypeScript 設定**: `strict` 有効、`noUncheckedIndexedAccess` 等の追加厳格化（[`frontend/tsconfig.json`](../../frontend/tsconfig.json)）。
- **パスエイリアス**: `@` → `src/`。

## Consequences

- Positive: 高速な HMR、モダンな ES モジュール開発体験。
- Negative: Vite とバックのポートがズレるとプロキシ失敗 → [ポート台帳スプレッドシート](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0)・[`docs/port-registry.md`](../port-registry.md) と `.env` の運用で緩和。

## Alternatives Considered

- **Next.js**: SSR/SSG は強力だが、本テンプレートは **BFF なしの SPA + REST** を前提 → 初期範囲外。
- **Create React App**: メンテナンス状況を踏まえ Vite を採用。

## References

- [`frontend/README.md`](../../frontend/README.md)
