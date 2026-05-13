# ADR-0023: ログイン機能 DB 設計（小規模向け）

- Status: Accepted
- Date: 2026-04-23
- Deciders: KizuNavi チーム

## Context

現行実装では、`users` テーブルに `refresh_token` を直接保持しており、次の課題がある。

- リフレッシュトークンの平文保存リスク
- 複数デバイス/ブラウザ同時ログインへの対応余地が小さい
- ログイン試行監査とアカウントロックの土台が弱い

一方で、対象プロダクトは小規模であり、運用コストを増やす過剰設計は避けたい。

## Decision

1. ログイン機能の DB は次の 4 テーブル構成とする。
   - `users`
   - `refresh_tokens`
   - `login_attempts`
   - `password_reset_tokens`
2. `users.role` は既存どおり enum 文字列（`ROLE_USER` / `ROLE_ADMIN`）で保持し、ロールマスタ/中間テーブルは導入しない。
3. アクセストークンは DB 保存しない。リフレッシュトークンのみ `refresh_tokens` に保存し、平文ではなく SHA-256 ハッシュで管理する。
4. リフレッシュローテーションは「旧行を `revoked_at` 更新 + 新行 INSERT」の単純方式を採用し、親子チェーンや `jti` 管理は採用しない。
5. `users.failed_login_count` と `users.locked_until`、および `login_attempts` により一時ロックを実装可能にする。
6. DDL は `backend/src/main/resources/db/oracle/login_schema.sql` を基準とする。
7. マイグレーションツール（Flyway / Liquibase）は導入しない。`ddl-auto` と手動 DDL 反映で運用する。
8. Oracle 19c SE2 前提とし、Partitioning / In-Memory / Advanced Compression など EE 専用機能は使わない。
9. 物理 FK 制約・CHECK 制約は設けない。テーブル間の参照整合性および値の妥当性検証はアプリケーション層で担保する。
10. スキーマユーザーは `template_app`。全オブジェクトは `template_app.テーブル名` の形式で作成する。
11. テーブルは `TEMPLATE_TABLE` 表領域、インデックスは `TEMPLATE_INDEX` 表領域に配置する。
12. インデックス名の接頭辞は `idx_` とする。

## Consequences

- Positive:
  - 小規模向けに実装・運用コストを抑えつつ、平文トークン保存を解消できる
  - リフレッシュトークンのサーバ側失効（ログアウト、強制無効化）が可能
  - ログイン試行の監査とアカウントロックの基盤を確保できる
- Negative:
  - リフレッシュ再利用検知（盗難トークン再提示の系列追跡）は実装しないため、検知粒度は限定的
  - マイグレーションツール未導入のため、DDL 適用手順の運用統制が必要
  - 物理 FK・CHECK 制約非採用のため、孤立レコードや不正値の検出・排除はアプリケーション層または定期バッチで対応する必要がある

## Alternatives Considered

- ロールを `roles` + `user_roles` へ正規化:
  - 小規模では過剰。現状の enum ロールで要件を満たすため却下。
- リフレッシュ再利用検知の親子チェーン + `jti`:
  - セキュリティ上は有効だが、実装複雑度に対し小規模要件では過剰なため却下。
- マイグレーションツール（Flyway / Liquibase）導入:
  - 現行運用方針と合わないため、今回スコープでは却下。
- 物理 FK 制約の採用:
  - 運用上の柔軟性（データ修正・バッチ処理）を優先し、整合性担保はアプリ層に委ねる方針のため却下。
- CHECK 制約の採用:
  - 値の妥当性検証はアプリケーション層（Bean Validation / サービス層）で行う方針のため却下。

## Entity 方針（実装時）

以下はコード実装時の変更方針。今回 ADR では方針のみ定義する。

### User.java

| 現状 | 方針 |
| --- | --- |
| `id` | `userId`（カラム `user_id`）へ名称統一 |
| `password` | `passwordHash`（カラム `password_hash`）へ変更 |
| `refreshToken` | 削除（`refresh_tokens` へ移行） |
| `role` | 維持（`@Enumerated(EnumType.STRING)`） |
| なし | `failedLoginCount` 追加 |
| なし | `lockedUntil` 追加 |
| なし | `lastLoginAt` 追加 |
| なし | `lastPasswordChangedAt` 追加 |
| なし | `@Version version` 追加 |

### 新規エンティティ

| エンティティ | 主なフィールド | 関連 |
| --- | --- | --- |
| `RefreshToken` | `tokenId`, `tokenHash`, `issuedAt`, `expiresAt`, `revokedAt`, `userAgent`, `ipAddress` | `@ManyToOne User` |
| `LoginAttempt` | `attemptId`, `email`, `succeeded`, `failureReason`（**`String`**。CHECK 非採用のため enum ではなく文字列で保持し、アプリ層で検証）, `ipAddress`, `attemptedAt` | `@ManyToOne User(nullable = true)` |
| `PasswordResetToken` | `tokenId`, `tokenHash`, `expiresAt`, `usedAt`, `createdAt`, `requesterIp` | `@ManyToOne User` |

## References

- [docs/adr/0004-backend-database-strategy.md](0004-backend-database-strategy.md)
- [docs/adr/0005-auth-jwt-cookie.md](0005-auth-jwt-cookie.md)
- [docs/db/login-schema.md](../db/login-schema.md)
- [backend/src/main/resources/db/oracle/login_schema.sql](../../backend/src/main/resources/db/oracle/login_schema.sql)
