# ADR-0016: ポート採番と台帳運用

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

テンプレートを複製して複数プロダクトを同一マシンで開発する際、**デフォルトの 5173 / 8080 が衝突**する。

## Decision

- **ベースポート**: フロント（ホスト）**5173**、バック（ホスト）**8080**（テンプレート本体の既定）。
- **複製プロダクト**: 同一規則で **+1 ずつインクリメント**（例: 5174/8081、5175/8082）。
- **台帳（正）**: [ポート採番用 Google スプレッドシート](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0) でプロダクト名と割当を管理し、衝突を防ぐ。[`docs/port-registry.md`](../port-registry.md) はルール・手順・スプレッドシートへのリンクを載せる。
- **設定の反映**:
  - Docker: ルート `.env` の `FRONTEND_PORT` / `BACKEND_PORT`（[`docker-compose.yml`](../../docker-compose.yml)）。
  - Compose 経由のバックエンド: `CORS_ALLOWED_ORIGINS` / `APP_FRONTEND_BASE_URL` は未指定時 **`http://localhost:${FRONTEND_PORT}`** に連動。
  - **ローカル（非 Docker）フロント**: `vite.config.ts` のプロキシ先が `8080` 固定のため、バックを **8081** 等に変えた場合は **vite の proxy も手動で合わせる**（または今後 env 化する改善余地あり）。

## Consequences

- Positive: 衝突回避がスプレッドシートと `.env` で明確。非開発者も閲覧しやすい。
- Negative: Vite プロキシと `application.yml` の `server.port` は **完全自動連動していない** → 台帳と README に注意書きを残す。

## Alternatives Considered

- **環境変数だけで全ツールを自動同期**: 実装コストが高い → 当面は台帳 + 主要箇所のパラメータ化。

## References

- [ポート採番台帳（Google スプレッドシート）](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0)
- [`.env.example`](../../.env.example)
- [`docs/port-registry.md`](../port-registry.md)
