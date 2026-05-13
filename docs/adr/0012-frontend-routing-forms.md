# ADR-0012: ルーティングとフォーム（React Router / RHF / Zod）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

認証ガード付きの SPA で、ルート定義とフォーム検証を一貫したパターンで扱う。

## Decision

- **ルーティング**: **React Router v6** の **Data Router**（`createBrowserRouter` + `RouterProvider`、[`frontend/src/routes/index.tsx`](../../frontend/src/routes/index.tsx)）。
- **ガード**: `ProtectedRoute` / `PublicRoute` で認証状態に応じてリダイレクト。
- **フォーム**: **React Hook Form** + **Zod** + `@hookform/resolvers` の `zodResolver`（ログイン・サインアップ・プロフィール等）。

## Consequences

- Positive: スキーマ駆動のバリデーションで型と UI エラーを揃えやすい。
- Negative: Zod スキーマと OpenAPI 生成型の**二重定義**になりうる → 可能なら生成型から zod を派生させる等の改善余地あり。

## Alternatives Considered

- **React Router 以外（TanStack Router）**: 型安全ルートは魅力だが、エコシステムと学習コストのバランスで現状は React Router。

## References

- [`frontend/src/routes/`](../../frontend/src/routes/)
- [`frontend/src/components/auth/`](../../frontend/src/components/auth/)
