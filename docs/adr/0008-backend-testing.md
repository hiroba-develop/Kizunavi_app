# ADR-0008: バックエンドテスト戦略

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

回帰防止とリファクタリングの安全性のため、テストスタックと現状ギャップを明示する。

## Decision

- **スタック**: `spring-boot-starter-test`（JUnit 5 / AssertJ / Mockito 等）、`spring-security-test`、`junit-platform-launcher`（[`backend/build.gradle`](../../backend/build.gradle)）。
- **方針**: サービス層は単体テスト、API は `@WebMvcTest` または `@SpringBootTest` + `MockMvc` / TestRestTemplate を推奨。
- **現状ギャップ**: `src/test` に本格的なテストクラスが未整備の場合がある。**プロダクト化時に最低限の認証・ユーザ API のテストを追加**することを推奨。
- **Testcontainers**: テンプレートでは**未導入**。Oracle 統合テストが必要なら別途依存と CI リソースを追加。

## Consequences

- Positive: Spring 公式スタックで学習コストが低い。
- Negative: DB 依存テストは **実 RDS またはテスト用 Oracle** が前提となる。テスト用 DB の用意・クリーンアップ方針が必要。

## Alternatives Considered

- **Testcontainers をテンプレート既定に含める**: CI 時間・複雑さが増える → 必要プロダクトのみ採用。

## References

- [`backend/build.gradle`](../../backend/build.gradle)（`dependencies` の test 系）
