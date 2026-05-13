# ADR-0010: UI とスタイリング（Tailwind CSS / shadcn / Radix）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

管理画面・業務アプリでよくある「コンポーネントの一貫性」と「カスタマイズ性」を両立する UI 方針が必要。

## Decision

- **Tailwind CSS 3.x** をユーティリティファーストのスタイル基盤とする（[`frontend/tailwind.config.ts`](../../frontend/tailwind.config.ts)）。
- **shadcn/ui パターン**: Radix プリミティブ + `class-variance-authority` + `tailwind-merge` / `clsx`（[`frontend/components.json`](../../frontend/components.json)、`src/components/ui/`）。
- **アイコン**: `lucide-react`。
- **グローバル CSS**: `src/styles/globals.css`（CSS 変数でテーマトークン）。

## Consequences

- Positive: アクセシビリティの土台（Radix）、デザイン変更が Tailwind クラスで完結しやすい。
- Negative: ユーティリティクラスが増えると可読性が下がる → 複雑 UI はコンポーネント分割を推奨。

## Alternatives Considered

- **MUI / Chakra**: 完成度は高いが、bundle サイズとテーマロックインのトレードオフ → shadcn はコード所有型で好まれる場合が多い。

## References

- [`frontend/components.json`](../../frontend/components.json)
