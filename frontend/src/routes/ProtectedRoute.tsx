import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuthStore } from '@/store/useAuthStore'

/**
 * 認証済みユーザーのみ子ルートを表示するラッパー。
 *
 * アクセストークンが無い場合は `/login` へリダイレクトし、元のパスを `state.from` に保存する。
 */
export function ProtectedRoute() {
  const accessToken = useAuthStore((s) => s.accessToken)
  const location = useLocation()

  if (!accessToken) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  return <Outlet />
}
