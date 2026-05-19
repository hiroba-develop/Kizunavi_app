import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuthStore } from '@/store/useAuthStore'

/** 認証済みでも表示を許可する公開パス（パスワード関連フロー）。 */
const AUTH_FLOW_PATHS = ['/reset-password', '/forgot-password', '/first-login'] as const

/**
 * 未認証ユーザーのみ子ルートを表示するラッパー（ログイン／パスワード関連画面用）。
 *
 * ログイン済みの場合は `/dashboard` へ送るが、パスワード再設定・初回設定など
 * メールリンクから遷移する画面ではリダイレクトしない。
 */
export function PublicRoute() {
  const accessToken = useAuthStore((s) => s.accessToken)
  const location = useLocation()

  const isAuthFlowRoute = AUTH_FLOW_PATHS.some((path) =>
    location.pathname.startsWith(path)
  )

  /** ログイン後に戻るパス（保護ルートから遷移してきた場合のみ `state` に入る）。 */
  const from =
    (location.state as { from?: Location })?.from?.pathname || '/dashboard'

  // ログイン画面のみ「既に認証済みならダッシュボードへ」。パスワード系はメールリンクから開くため除外。
  if (accessToken && !isAuthFlowRoute && location.pathname === '/login') {
    return <Navigate to={from} replace />
  }

  return <Outlet />
}
