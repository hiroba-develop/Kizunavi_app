import { useEffect, type ReactNode } from 'react'
import axios from 'axios'
import { useAuthStore } from '@/store/useAuthStore'

/** バックエンド API のベース URL（Vite 環境変数、未設定時は相対パス）。 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || ''

/**
 * StrictMode による effect 二重実行で、サーバー側の
 * リフレッシュトークンローテーションが二度走らないようにするモジュールフラグ。
 */
let bootstrapStarted = false

/** 単体テスト用: モジュールスコープのブートストラップフラグをリセットする */
export function resetAuthBootstrapForTests() {
  bootstrapStarted = false
}

/**
 * 起動時にリフレッシュ Cookie を用いた silent refresh を行い、
 * 認証状態を復元する境界コンポーネント。
 *
 * - 成功時: `setAccessToken` でメモリ上のアクセストークンを復元する。
 * - 失敗時: `clearAuth` でストアと永続化された `user` を破棄する。
 * - 完了するまで子要素は描画せず、ローディング表示を返す。
 *
 * これにより `accessToken` / `isAuthenticated` を localStorage に
 * 永続化せずとも、ページリロード後の認証セッションを継続できる。
 */
export function AuthBootstrap({ children }: { children: ReactNode }) {
  const isBootstrapping = useAuthStore((s) => s.isBootstrapping)
  const setAccessToken = useAuthStore((s) => s.setAccessToken)
  const setBootstrapped = useAuthStore((s) => s.setBootstrapped)
  const clearAuth = useAuthStore((s) => s.clearAuth)

  useEffect(() => {
    if (bootstrapStarted) {
      return
    }
    bootstrapStarted = true

    const bootstrap = async () => {
      try {
        const response = await axios.post<{ token?: string }>(
          `${API_BASE_URL}/api/auth/refresh`,
          {},
          { withCredentials: true }
        )
        const token = response.data?.token
        if (token) {
          setAccessToken(token)
        } else {
          clearAuth()
        }
      } catch {
        clearAuth()
      } finally {
        setBootstrapped()
      }
    }

    void bootstrap()
  }, [setAccessToken, setBootstrapped, clearAuth])

  if (isBootstrapping) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div
          role="status"
          aria-live="polite"
          className="text-sm text-muted-foreground"
        >
          読み込み中...
        </div>
      </div>
    )
  }

  return <>{children}</>
}
