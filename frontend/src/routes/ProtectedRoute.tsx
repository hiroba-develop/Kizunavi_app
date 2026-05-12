import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuthStore } from '@/store/useAuthStore'

/**
 * 認証済みユーザーのみ子ルートを表示するラッパー。
 *
 * 未認証の場合は `/login` へリダイレクトし、元のパスを `state.from` に保存する。
 */
export function ProtectedRoute() {
  const { isAuthenticated } = useAuthStore()
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  return <Outlet />
}
