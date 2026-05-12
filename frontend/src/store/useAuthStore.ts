import { create } from 'zustand'
import { persist, createJSONStorage } from 'zustand/middleware'
import { Role } from '@/api'

/**
 * 認証済みユーザーとして画面に表示する最小情報。
 */
export interface User {
  /** バックエンドのユーザー主キー。 */
  id: number
  /** ログイン ID（メールアドレス）。 */
  email: string
  /** 表示名。 */
  name: string
  /** 権限ロール。 */
  role: Role
}

/**
 * 認証ストアの状態およびアクション。
 *
 * セキュリティ方針:
 * - {@link AuthState.accessToken} と {@link AuthState.isAuthenticated} は
 *   localStorage に保存しない（XSS / 改ざん耐性のため）。
 * - 永続化対象は表示用 {@link AuthState.user} のみ。
 * - 認証状態は起動時 silent refresh（`AuthBootstrap`）で復元する。
 */
interface AuthState {
  /** JWT アクセストークン。未ログイン時は `null`。 */
  accessToken: string | null
  /** ログイン中ユーザーの概要。未設定時は `null`。 */
  user: User | null
  /**
   * 認証済みフラグ。`accessToken` の有無と連動するメモリ上の状態で、
   * localStorage には永続化しない（リロード時は false にリセットされる）。
   */
  isAuthenticated: boolean
  /**
   * アプリ起動直後の認証復元（silent refresh）が完了するまで `true`。
   * これも永続化しない。`AuthBootstrap` 完了後に `false` になる。
   */
  isBootstrapping: boolean
  /**
   * アクセストークンを保存し、認証済みフラグを立てる。
   *
   * @param accessToken 新しい JWT
   */
  setAccessToken: (accessToken: string) => void
  /**
   * ユーザー概要を保存する。
   *
   * @param user 設定するユーザー
   */
  setUser: (user: User) => void
  /** 起動時の認証復元処理が完了したことを記録する。 */
  setBootstrapped: () => void
  /** トークンとユーザーを破棄し未認証にする。 */
  logout: () => void
  /** `logout` と同等の認証クリア（明示的リセット用）。 */
  clearAuth: () => void
}

/**
 * 認証状態を保持する Zustand ストア。
 *
 * 永続化対象は {@link AuthState.user} のみ。`accessToken` / `isAuthenticated` /
 * `isBootstrapping` はメモリ上にのみ存在し、リロードで初期値に戻る。
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      accessToken: null,
      user: null,
      isAuthenticated: false,
      isBootstrapping: true,

      setAccessToken: (accessToken) =>
        set({
          accessToken,
          isAuthenticated: true,
        }),

      setUser: (user) =>
        set({
          user,
        }),

      setBootstrapped: () =>
        set({
          isBootstrapping: false,
        }),

      logout: () =>
        set({
          accessToken: null,
          user: null,
          isAuthenticated: false,
        }),

      clearAuth: () =>
        set({
          accessToken: null,
          user: null,
          isAuthenticated: false,
        }),
    }),
    {
      name: 'auth-storage',
      storage: createJSONStorage(() => localStorage),
      partialize: (state) => ({
        user: state.user,
      }),
    }
  )
)
