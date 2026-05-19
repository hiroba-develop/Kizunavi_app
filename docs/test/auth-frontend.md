# 認証関連 単体テスト一覧（フロントエンド）

- **最終更新**: 2026-05-19
- **指標**: C0（命令網羅）— 対象ファイルは **命令（Statements）・行（Lines）100%** を目標とする
- **方針**: [ADR-0024](../adr/0024-testing-strategy.md)、[テスト入門ガイド](../guides/testing-guide.md)

## 対象範囲

| 種別 | パス |
|------|------|
| フック | `hooks/useAuth.ts` |
| 画面 | `pages/FirstLoginPage.tsx`, `ForgotPasswordPage.tsx`, `ResetPasswordPage.tsx` |
| コンポーネント | `components/auth/LoginForm.tsx`, `AuthBootstrap.tsx` |
| ルート | `routes/PublicRoute.tsx` |
| ストア | `store/useAuthStore.ts`（認証状態） |
| 対象外 | `src/api/**`（生成コード）, `LoginPage.tsx`（薄いラッパ） |

## カバレッジ状況（最終確認: 2026-05-19）

認証対象ファイルをまとめて確認した結果:

| 区分 | Statements | Lines | 備考 |
|------|------------|-------|------|
| 認証対象一式 | 100% | 100% | `useAuth` の分岐網羅（C1）は一部未達の枝あり |

```bash
cd frontend
npm run test:coverage

# 認証ファイルのみ（例）
npx vitest run --coverage \
  --coverage.include="src/hooks/useAuth.ts" \
  --coverage.include="src/pages/FirstLoginPage.tsx" \
  --coverage.include="src/pages/ForgotPasswordPage.tsx" \
  --coverage.include="src/pages/ResetPasswordPage.tsx" \
  --coverage.include="src/components/auth/LoginForm.tsx" \
  --coverage.include="src/components/auth/AuthBootstrap.tsx" \
  --coverage.include="src/routes/PublicRoute.tsx"
```

## テストファイル一覧

| テストファイル | 対象 |
|---------------|------|
| [useAuth.test.tsx](../../frontend/src/hooks/__tests__/useAuth.test.tsx) | `useAuth` 各フック |
| [useAuthStore.test.ts](../../frontend/src/store/__tests__/useAuthStore.test.ts) | `useAuthStore` |
| [FirstLoginPage.test.tsx](../../frontend/src/pages/__tests__/FirstLoginPage.test.tsx) | `FirstLoginPage` |
| [FirstLoginPage.pending.test.tsx](../../frontend/src/pages/__tests__/FirstLoginPage.pending.test.tsx) | 送信中 UI |
| [ForgotPasswordPage.test.tsx](../../frontend/src/pages/__tests__/ForgotPasswordPage.test.tsx) | `ForgotPasswordPage` |
| [ForgotPasswordPage.pending.test.tsx](../../frontend/src/pages/__tests__/ForgotPasswordPage.pending.test.tsx) | 送信中 UI |
| [ResetPasswordPage.test.tsx](../../frontend/src/pages/__tests__/ResetPasswordPage.test.tsx) | `ResetPasswordPage` |
| [ResetPasswordPage.pending.test.tsx](../../frontend/src/pages/__tests__/ResetPasswordPage.pending.test.tsx) | 送信中 UI |
| [LoginForm.test.tsx](../../frontend/src/components/auth/__tests__/LoginForm.test.tsx) | `LoginForm` |
| [LoginForm.pending.test.tsx](../../frontend/src/components/auth/__tests__/LoginForm.pending.test.tsx) | 送信中 UI |
| [AuthBootstrap.test.tsx](../../frontend/src/components/auth/__tests__/AuthBootstrap.test.tsx) | `AuthBootstrap` |
| [PublicRoute.test.tsx](../../frontend/src/routes/__tests__/PublicRoute.test.tsx) | `PublicRoute` |
| [test-utils.tsx](../../frontend/src/test/test-utils.tsx) | `renderWithProviders`（共通） |

**モック方針**: ページ・フォームは `@/hooks/useAuth` をモック。フックテストは `@/api` の `AuthService`, `@/lib/axios`, `react-router-dom` の `useNavigate`, `@/hooks/use-toast` をモック。

---

## `useAuth`（hooks）

| フック | 観点 | テスト（it） |
|--------|------|-------------|
| `useLogin` | 正常系 | 成功時にユーザー情報を反映しダッシュボードへ遷移する |
| `useLogin` | 境界 | /users/me 失敗時もログインを継続する |
| `useLogin` | 異常系 | ApiError 423 でメッセージなしのときデフォルト説明を表示する |
| `useLogin` | 異常系 | ApiError 423 のときロック toast を表示する |
| `useLogin` | 異常系 | Axios 423 でメッセージなしのときデフォルト説明を表示する |
| `useLogin` | 異常系 | Axios 423 のときロック toast を表示する |
| `useLogin` | 異常系 | 一般エラー時にログイン失敗 toast を表示する |
| `useForgotPassword` | 異常系 | ApiError 時にカスタムメッセージを toast する |
| `useForgotPassword` | 異常系 | その他エラー時にデフォルトメッセージを toast する |
| `useVerifyResetToken` | 正常系 | token があるとき検証 API を呼ぶ |
| `useResetPassword` | 正常系 | 成功時に toast を表示する |
| `useResetPassword` | 異常系 | ApiError 時にカスタムメッセージを toast する |
| `useResetPassword` | 異常系 | その他エラー時にデフォルトメッセージを toast する |
| `useFirstLogin` | 正常系 | 成功時に toast 表示とログイン画面へ遷移する |
| `useFirstLogin` | 異常系 | ApiError 時にカスタムメッセージを toast する |
| `useFirstLogin` | 異常系 | Axios エラー時にメッセージを toast する |
| `useFirstLogin` | 異常系 | その他エラー時にデフォルトメッセージを toast する |
| `useLogout` | 正常系 | accessToken ありのとき logout API を呼ぶ |
| `useLogout` | I/O 隔離 | accessToken なしのとき API を呼ばずログアウトする |
| `useLogout` | 境界 | API 失敗時もローカル状態を破棄する |

---

## `useAuthStore`（store）

| 観点 | テスト（it） |
|------|-------------|
| 正常系 | setAccessToken で認証済み状態になる |
| 正常系 | setUser でユーザー情報を保持する |
| 正常系 | setBootstrapped で起動中フラグを下ろす |
| 正常系 | logout で認証情報を全てクリアする |
| セキュリティ | localStorage には accessToken / isAuthenticated / isBootstrapping を永続化しない |

---

## `FirstLoginPage`

`useFirstLogin` をモック。Zod バリデーションと完了 UI を検証。

| 観点 | テスト（it） |
|------|-------------|
| UI | 初回パスワード設定フォームを表示する |
| UI | バリデーションエラーを表示する |
| UI | パスワード不一致のときエラーを表示する |
| 正常系 | 送信成功時に完了画面を表示する |
| UI | 送信中はボタンを無効化する（pending） |

---

## `ForgotPasswordPage`

`useForgotPassword` / `useToast` をモック。

| 観点 | テスト（it） |
|------|-------------|
| UI | メール入力フォームを表示する |
| UI | 無効なメールのときバリデーションエラーを表示する |
| 正常系 | 送信成功時に完了画面と toast を表示する |
| UI | 送信中はボタンを無効化する（pending） |

---

## `ResetPasswordPage`

`useVerifyResetToken` / `useResetPassword` をモック。`useSearchParams` はテストで差し替え可能。

| 観点 | テスト（it） |
|------|-------------|
| UI | トークンなしのとき無効リンクを表示する |
| UI | 検証中はローディングを表示する |
| UI | 検証エラー時にメッセージを表示する |
| UI | 無効トークン応答時に API メッセージを表示する |
| UI | 有効トークン時にフォームを表示しバリデーションする |
| UI | パスワード不一致のときエラーを表示する |
| 正常系 | 送信成功時に完了画面を表示する |
| UI | 送信中はボタンを無効化する（pending） |

---

## `LoginForm`

`useLogin` をモック。

| 観点 | テスト（it） |
|------|-------------|
| UI | default variant で shadcn ボタンを表示する |
| UI | brand variant でリンクとブランドスタイルを表示する |
| UI | バリデーションエラーを表示する |
| 正常系 | 送信時に login.mutate を呼ぶ |
| UI | default / brand variant で送信中はボタンを無効化する（pending） |

---

## `AuthBootstrap`

`axios.post` をモック。`resetAuthBootstrapForTests()` でモジュールフラグをリセット。

| 観点 | テスト（it） |
|------|-------------|
| UI | ブートストラップ中は読み込み表示を出す |
| 正常系 | refresh 成功時に accessToken を復元する |
| 異常系 | token が無い応答のとき clearAuth する |
| 境界 | 二重マウント時は refresh API を再実行しない |
| 異常系 | refresh 失敗時に clearAuth する |

---

## `PublicRoute`

`useAuthStore` の `accessToken` を直接操作。`createMemoryRouter` でルートを構成。

| 観点 | テスト（it） |
|------|-------------|
| 正常系 | 未認証のとき子ルートを表示する |
| 正常系 | 認証済みかつログイン画面のときダッシュボードへリダイレクトする |
| 正常系 | 認証済みでも初回ログイン画面はリダイレクトしない |
| 正常系 | state.from があるとき指定パスへリダイレクトする |

---

## 変更履歴

| 日付 | 内容 |
|------|------|
| 2026-05-19 | 初版（認証 C0 単体テスト一式） |
