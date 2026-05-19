# ADR-0024: 単体テスト戦略（C0 / JUnit 5 / Vitest）

- Status: Accepted
- Date: 2026-04-24
- Deciders: KizuNavi チーム

## Context

テンプレートを複製して開発する際、次の課題がある。

- テストの目的と範囲が曖昧で、実装者ごとに「どこまで書くか」の解釈がずれる
- 初学者メンバーにとって、テスト観点・命名・実行方法が統一されていない
- バックエンドとフロントエンドでツールや運用が分断されると学習コストが上がる

一方で、現時点ではまず回帰防止の土台を作ることが優先であり、結合テストや E2E まで一度に拡張すると運用負荷が高い。

## Decision

1. 本テンプレートのテストスコープは **単体テスト** に限定し、カバレッジ指標は **C0（命令網羅）** を採用する。
2. C0 カバレッジは当面 **レポート出力のみ** とし、閾値でビルドを fail させない。
3. バックエンドは `spring-boot-starter-test`（JUnit 5 / AssertJ / Mockito）を基本とし、JaCoCo でカバレッジレポートを生成する。
4. フロントエンドは **Vitest + React Testing Library + jsdom** を採用する。Vitest は Jest 互換 API を持つため、学習内容は Jest にも転用可能とする。
5. テスト対象は次を優先する。
   - バックエンド: `service` / `util` の業務ロジック
   - フロントエンド: `lib` の純粋関数、`hooks`、`store`
6. 生成コードはテスト対象およびカバレッジ集計対象から除外する。
   - `backend/src/main/java/com/kizunavi/dto/**`
   - `backend/src/main/java/com/kizunavi/controller/*Api.java`
   - `frontend/src/api/**`
7. **テスト台帳**を `docs/test/` でドメイン別に管理する。実装・単体テストの変更時は台帳を同じ PR で更新し、観点ラベル・ケース名・カバレッジ状況をコードと整合させる（初版: 認証は [auth-backend.md](../test/auth-backend.md) / [auth-frontend.md](../test/auth-frontend.md)）。

## Consequences

- Positive:
  - 最小限のテスト運用から始められ、初学者でも参加しやすい
  - リファクタ時の安全網を早期に確保できる
  - バック・フロントで「単体テスト + C0」の共通言語を持てる
- Negative:
  - 閾値を設けないため、カバレッジが低い状態でも CI を通過する
  - 結合テスト/E2E の品質担保は別施策が必要

## Alternatives Considered

- 初期から C0 の閾値（70%/80%）を必須化:
  - 品質統制は強いが、初学者オンボーディング初期に失敗体験が増えるため今回は見送る。
- フロントを Jest で統一:
  - 実現可能だが、Vite + ESM との設定コストが上がるため今回は Vitest を採用する。
- 単体テストと同時に E2E まで標準化:
  - スコープが広く導入コストが高いため段階導入とする。

## References

- [docs/adr/0008-backend-testing.md](0008-backend-testing.md)
- [backend/build.gradle](../../backend/build.gradle)
- [frontend/package.json](../../frontend/package.json)
- [docs/guides/testing-guide.md](../guides/testing-guide.md)
- [docs/test/README.md](../test/README.md)（テスト台帳インデックス）
