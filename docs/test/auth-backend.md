# 認証関連 単体テスト一覧（バックエンド）

- **最終更新**: 2026-05-19
- **指標**: C0（命令網羅）— 対象クラスは **行（Lines）100%** を目標とする
- **方針**: [ADR-0024](../adr/0024-testing-strategy.md)、[テスト入門ガイド](../guides/testing-guide.md)

## 対象範囲

| 種別 | パス |
|------|------|
| 対象 | `AuthService`, `EmailService`, `FirstLoginPasswordSupport` |
| 関連 | `TenantLoginSupport`（`refreshToken` のテナント判定で利用） |
| 対象外 | `*Api.java`, `dto/**`, `AuthController`（委譲のみ）, JWT フィルタ全体 |

## カバレッジ状況（最終確認: 2026-05-19）

| クラス | 行 | 命令 | 備考 |
|--------|-----|------|------|
| `EmailService` | 100% | 100% | — |
| `FirstLoginPasswordSupport` | 100% | 100% | — |
| `AuthService` | 100% | 99% | 未カバーは主に `orElseThrow` ラムダ等 |

```bash
cd backend
./gradlew test jacocoTestReport
# com.kizunavi.service / com.kizunavi.auth を JaCoCo HTML で確認
```

## テストファイル一覧

| テストクラス | 対象 |
|-------------|------|
| [AuthServiceTest.java](../../backend/src/test/java/com/kizunavi/service/AuthServiceTest.java) | `AuthService` |
| [EmailServiceTest.java](../../backend/src/test/java/com/kizunavi/service/EmailServiceTest.java) | `EmailService` |
| [FirstLoginPasswordSupportTest.java](../../backend/src/test/java/com/kizunavi/auth/FirstLoginPasswordSupportTest.java) | `FirstLoginPasswordSupport` |
| [TenantLoginSupportTest.java](../../backend/src/test/java/com/kizunavi/auth/TenantLoginSupportTest.java) | `TenantLoginSupport`（認証フロー依存） |

---

## `AuthService`

I/O は `AuthenticationManager`, `UserRepository`, `RefreshTokenRepository`, `PasswordResetTokenRepository`, `LoginAttemptRepository`, `JwtTokenProvider`, `EmailService` を Mockito で差し替え。

### `login`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 異常系 | 認証成功後にユーザーが見つからない場合は RuntimeException |
| 正常系 | 成功時にトークンを発行しログイン試行を記録する |
| 回帰 | BadCredentialsException 時に失敗回数を記録して再スローする |
| 異常系 | DisabledException 時に失敗理由を記録する |
| 異常系 | LockedException 時に失敗理由を記録する |
| 異常系 | その他の AuthenticationException 時に失敗を記録する |

### `refreshToken`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 異常系 | null のとき InvalidTokenException |
| 異常系 | 空文字のとき InvalidTokenException |
| 異常系 | JWT 無効のとき InvalidTokenException |
| 異常系 | DB に無いとき InvalidTokenException |
| 異常系 | 期限切れのとき InvalidTokenException |
| 異常系 | メール不一致のとき InvalidTokenException |
| 異常系 | テナント付きユーザー取得に失敗したとき InvalidTokenException |
| 異常系 | テナント無効のとき InvalidTokenException |
| 正常系 | 成功時にトークンをローテーションする |

### `logout`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | ユーザーが存在するときリフレッシュトークンを失効する |
| 境界 | ユーザーが存在しないときも例外なく完了する |

### `forgotPassword`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | ユーザー存在時にトークン発行とメール送信を行う |
| セキュリティ | ユーザー不存在でも success を返す |
| 境界 | メール送信失敗時も success を返す（ログのみ） |

### `verifyResetToken`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 異常系 | 無効トークンは valid=false |
| 異常系 | 空トークンはメッセージを返す |
| 異常系 | 期限切れトークンは期限切れメッセージ |
| 異常系 | 未使用かつ有効期限内だが検証不可のとき使用済みメッセージ |
| 正常系 | 有効トークンは valid=true |

### `resetPassword`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | 成功時にパスワード更新とトークン失効を行う |
| 異常系 | 無効トークンは InvalidTokenException |

### `firstLogin`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | 仮パスワード一致時に新パスワードを保存する |
| 異常系 | 初回設定済みユーザーは BadRequestException |
| 異常系 | 新パスワードが仮パスワードと同一の場合は BadRequestException |
| セキュリティ | 仮パスワード不一致時は InvalidTokenException |
| セキュリティ | ユーザー不存在時は InvalidTokenException |

### `toUserResponse`（static）

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | エンティティを UserResponse にマッピングする |

---

## `EmailService`

`SesClient` をモック。Spring コンテキストは起動しない。

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| I/O 隔離 | enabled=false のとき送信せず return する |
| I/O 隔離 | enabled=true かつ SesClient なしのとき warn して return する |
| 正常系 | enabled=true かつ送信成功 |
| 異常系 | SesException 時は RuntimeException をスローする |
| 正常系 | sendWelcomeEmail は sendEmail を呼び出す |
| 正常系 | sendPasswordResetEmail は sendEmail を呼び出す |
| 境界 | コンストラクタ: enabled=true かつ client 空で warn |

---

## `FirstLoginPasswordSupport`

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 境界 | user が null のときは false |
| 境界 | lastPasswordChangedAt が null のときは false |
| 正常系 | センチネル時刻のときは true |
| 正常系 | 通常の変更日時のときは false |
| 境界 | pendingPasswordChangeTimestamp はセンチネル値を返す |

---

## `TenantLoginSupport`（認証依存）

`AuthService.refreshToken` のテナント可否判定で使用。

| 観点 | テスト（@DisplayName） |
|------|------------------------|
| 正常系 | 従業員・顧客未紐づけユーザーはログイン可能 |
| 正常系 | 顧客・従業員とも del_flg=0 ならログイン可能 |
| 異常系 | 顧客 del_flg=1 ならログイン不可 |
| 異常系 | 従業員 del_flg=1 ならログイン不可 |

---

## 変更履歴

| 日付 | 内容 |
|------|------|
| 2026-05-19 | 初版（認証 C0 単体テスト一式） |
