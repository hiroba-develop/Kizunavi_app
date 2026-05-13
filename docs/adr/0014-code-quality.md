# ADR-0014: コード品質（ESLint / Prettier）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

フロントエンドの一貫したスタイルと静的解析により、レビュー負荷とバグを下げる。

## Decision

- **ESLint 9**: フラット設定 [`frontend/eslint.config.js`](../../frontend/eslint.config.js)。`typescript-eslint`、React Hooks / React Refresh プラグイン。
- **Prettier**: [`.prettierrc`](../../frontend/.prettierrc)（セミコロンなし、シングルクォート等）。
- **npm scripts**: `lint` / `lint:fix` / `format`。

## Consequences

- Positive: CI に組み込みやすい。チーム間でフォーマット争いが減る。
- Negative: 生成コードを lint 対象にするとノイズが増える → ignore で除外。

## Alternatives Considered

- **Biome のみ**: 高速だが、既存エコシステムとの移行コスト → 現状は ESLint + Prettier。

## References

- [`frontend/eslint.config.js`](../../frontend/eslint.config.js)
