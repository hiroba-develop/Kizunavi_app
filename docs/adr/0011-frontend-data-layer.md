# ADR-0011: データ層（TanStack Query / Zustand / Axios）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

サーバー状態とクライアント認証状態を分け、キャッシュ・再取得・401 時のリフレッシュを整理する。

## Decision

- **サーバー状態**: **TanStack Query (React Query) v5**（[`frontend/src/lib/queryClient.ts`](../../frontend/src/lib/queryClient.ts)、`main.tsx` で Provider）。
- **認証などのクライアント状態**: **Zustand** + `persist`（[`frontend/src/store/useAuthStore.ts`](../../frontend/src/store/useAuthStore.ts)）。アクセストークンは永続化から除外する方針。
- **HTTP**: **Axios** インスタンス（[`frontend/src/lib/axios.ts`](../../frontend/src/lib/axios.ts)）。`withCredentials: true`、401 時のリフレッシュキュー。
- **ベース URL**: `VITE_API_BASE_URL`（空なら同一オリジン相対パス）。

## Consequences

- Positive: サーバー状態のキャッシュ・ローディング・エラーが宣言的。Cookie 付きリクエストと整合。
- Negative: Query と Store の責務境界を誤ると二重取得や stale 状態が発生 → 認証は Store、リソースは Query に寄せる。

## Alternatives Considered

- **RTK Query のみ**: Redux 依存が増える → 本テンプレートは Zustand の軽さを優先。

## References

- [`frontend/src/hooks/useAuth.ts`](../../frontend/src/hooks/useAuth.ts)
- [`frontend/src/hooks/useUser.ts`](../../frontend/src/hooks/useUser.ts)
