import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuthStore } from '@/store/useAuthStore'

/**
 * 未認証ユーザーのみ子ルートを表示するラッパー（ログイン／登録画面用）。
 *
 * 既に認証済みの場合は、`location.state.from` があればそのパス、なければ `/dashboard` へ送る。
 */
export function PublicRoute() {
  const { isAuthenticated } = useAuthStore()
  const location = useLocation()

  /** ログイン後に戻るパス（保護ルートから遷移してきた場合のみ `state` に入る）。 */
  const from = (location.state as { from?: Location })?.from?.pathname || '/dashboard'

  if (isAuthenticated) {
    return <Navigate to={from} replace />
  }

  return <Outlet />
}
