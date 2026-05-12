import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios'
import { useAuthStore } from '@/store/useAuthStore'

/** バックエンド API のベース URL（Vite 環境変数、未設定時は相対パス）。 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || ''

/**
 * アプリ全体で共有する Axios インスタンス。
 *
 * - リクエスト: `Authorization: Bearer` にストアのアクセストークンを付与
 * - レスポンス: 401 時にリフレッシュ Cookie でトークン更新を試み、キューに溜まったリクエストを再送
 */
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = useAuthStore.getState().accessToken
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/** リフレッシュ API を多重実行しないためのフラグ。 */
let isRefreshing = false
/**
 * リフレッシュ完了待ちのリクエストを保持するキュー。
 * 各要素は新トークンで再試行する `resolve` / 失敗時の `reject` を持つ。
 */
let failedQueue: Array<{
  resolve: (token: string) => void
  reject: (error: unknown) => void
}> = []

/**
 * 待機中のリクエストキューを一括で解決または拒否する。
 *
 * @param error 失敗時に各 `reject` に渡すエラー（成功時は未使用でよい）
 * @param token 新アクセストークン。`null` の場合はすべて `reject` する
 */
const processQueue = (error: unknown, token: string | null) => {
  failedQueue.forEach((prom) => {
    if (token) {
      prom.resolve(token)
    } else {
      prom.reject(error)
    }
  })
  failedQueue = []
}

apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({
            resolve: (token: string) => {
              originalRequest.headers.Authorization = `Bearer ${token}`
              resolve(apiClient(originalRequest))
            },
            reject,
          })
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      if (!useAuthStore.getState().isAuthenticated) {
        isRefreshing = false
        useAuthStore.getState().logout()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      try {
        const response = await axios.post(
          `${API_BASE_URL}/api/auth/refresh`,
          {},
          { withCredentials: true }
        )

        const { accessToken } = response.data
        useAuthStore.getState().setAccessToken(accessToken)

        processQueue(null, accessToken)

        originalRequest.headers.Authorization = `Bearer ${accessToken}`
        return apiClient(originalRequest)
      } catch (refreshError) {
        processQueue(refreshError, null)
        useAuthStore.getState().logout()
        window.location.href = '/login'
        return Promise.reject(refreshError)
      } finally {
        isRefreshing = false
      }
    }

    return Promise.reject(error)
  }
)

/** {@link apiClient} のデフォルトエクスポート（同一インスタンス）。 */
export default apiClient
