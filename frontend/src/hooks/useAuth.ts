import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { apiClient } from '@/lib/axios'
import { useAuthStore, type User } from '@/store/useAuthStore'
import { useToast } from '@/hooks/use-toast'
import type {
  LoginRequest,
  SignupRequest,
  TokenResponse,
  UserResponse,
} from '@/api'

/**
 * ログイン API を呼び出すミューテーションフック。
 *
 * 成功時は `useAuthStore` にアクセストークンとユーザーを設定し、
 * トースト表示のうえ `/dashboard` へ遷移する。失敗時はエラー用トーストを表示する。
 *
 * @returns `react-query` の `UseMutationResult`。`mutate(data)` に `LoginRequest` を渡して呼び出す。
 */
export function useLogin() {
  const navigate = useNavigate()
  const { setAccessToken, setUser } = useAuthStore()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: LoginRequest): Promise<TokenResponse> => {
      const response = await apiClient.post<TokenResponse>('/api/auth/login', data)
      return response.data
    },
    onSuccess: async (data) => {
      setAccessToken(data.accessToken!)

      try {
        const userResponse = await apiClient.get<UserResponse>('/api/users/me', {
          headers: {
            Authorization: `Bearer ${data.accessToken}`,
          },
        })
        const user: User = {
          id: userResponse.data.id!,
          email: userResponse.data.email!,
          name: userResponse.data.name!,
          role: userResponse.data.role!,
        }
        setUser(user)
      } catch {
        // Silently fail user fetch, authentication is still successful
      }

      toast({
        title: 'ログイン成功',
        description: 'ダッシュボードにリダイレクトします',
      })
      navigate('/dashboard')
    },
    onError: () => {
      toast({
        variant: 'destructive',
        title: 'ログイン失敗',
        description: 'メールアドレスまたはパスワードが正しくありません',
      })
    },
  })
}

/**
 * 新規登録 API を呼び出すミューテーションフック。
 *
 * 成功時はトーストを表示し `/login` へ誘導する。失敗時はサーバーメッセージまたは
 * 汎用文言でトーストを表示する。
 *
 * @returns `UseMutationResult`。`mutate(data)` に `SignupRequest` を渡す。
 */
export function useSignup() {
  const navigate = useNavigate()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: SignupRequest): Promise<UserResponse> => {
      const response = await apiClient.post<UserResponse>('/api/auth/signup', data)
      return response.data
    },
    onSuccess: () => {
      toast({
        title: '登録成功',
        description: 'アカウントが作成されました。ログインしてください。',
      })
      navigate('/login')
    },
    onError: (error: { response?: { data?: { message?: string } } }) => {
      toast({
        variant: 'destructive',
        title: '登録失敗',
        description:
          error.response?.data?.message ||
          'アカウントの作成に失敗しました',
      })
    },
  })
}

/**
 * ログアウト API を呼び出し、ローカル認証状態を破棄するミューテーションフック。
 *
 * アクセストークンがある場合のみ `/api/auth/logout` を叩く。成功・失敗にかかわらず
 * ストアはクリアされ `/login` へ遷移する。
 *
 * @returns `UseMutationResult`。引数なしで `mutate()` を呼び出す。
 */
export function useLogout() {
  const navigate = useNavigate()
  const { logout, accessToken } = useAuthStore()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async () => {
      if (accessToken) {
        await apiClient.post('/api/auth/logout')
      }
    },
    onSuccess: () => {
      logout()
      toast({
        title: 'ログアウト',
        description: 'ログアウトしました',
      })
      navigate('/login')
    },
    onError: () => {
      logout()
      navigate('/login')
    },
  })
}
