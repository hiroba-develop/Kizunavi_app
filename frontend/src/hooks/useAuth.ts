import { useMutation, useQuery } from '@tanstack/react-query'
import { isAxiosError } from 'axios'
import { useNavigate } from 'react-router-dom'
import { apiClient } from '@/lib/axios'
import { useAuthStore, type User } from '@/store/useAuthStore'
import { useToast } from '@/hooks/use-toast'
import { ApiError, AuthService, Role } from '@/api'
import type {
  FirstLoginRequest,
  ForgotPasswordRequest,
  LoginRequest,
  ResetPasswordRequest,
  SimpleStatusResponse,
} from '@/api'
import type { UserResponse } from '@/types/auth'

/**
 * ログイン API を呼び出すミューテーションフック。
 */
export function useLogin() {
  const navigate = useNavigate()
  const { setAccessToken, setUser } = useAuthStore()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: LoginRequest) => AuthService.login(data),
    onSuccess: async (data, variables) => {
      setAccessToken(data.token)

      const partialUser: User = {
        id: 0,
        email: variables.email,
        name: data.name,
        role: Role.ROLE_USER,
      }
      setUser(partialUser)

      try {
        const userResponse = await apiClient.get<UserResponse>('/api/users/me', {
          headers: {
            Authorization: `Bearer ${data.token}`,
          },
        })
        const user: User = {
          id: userResponse.data.id!,
          email: userResponse.data.email!,
          name: userResponse.data.name ?? data.name,
          role: userResponse.data.role!,
        }
        setUser(user)
      } catch {
        // 認証は成功しているため /users/me 失敗時もログイン継続
      }

      toast({
        title: 'ログイン成功',
        description: 'ダッシュボードにリダイレクトします',
      })
      navigate('/dashboard')
    },
    onError: (error: unknown) => {
      if (error instanceof ApiError && error.status === 423) {
        toast({
          variant: 'destructive',
          title: 'アカウントがロックされています',
          description:
            typeof error.body?.message === 'string'
              ? error.body.message
              : 'しばらくしてから再度お試しください',
        })
        return
      }
      if (isAxiosError(error) && error.response?.status === 423) {
        toast({
          variant: 'destructive',
          title: 'アカウントがロックされています',
          description:
            (error.response.data as { message?: string })?.message ??
            'しばらくしてから再度お試しください',
        })
        return
      }
      toast({
        variant: 'destructive',
        title: 'ログイン失敗',
        description: 'メールアドレスまたはパスワードが正しくありません',
      })
    },
  })
}

/**
 * パスワード再発行メール送信 API を呼び出すミューテーションフック。
 */
export function useForgotPassword() {
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (
      data: ForgotPasswordRequest
    ): Promise<SimpleStatusResponse> => AuthService.forgotPassword(data),
    onError: (error: unknown) => {
      const description =
        error instanceof ApiError && typeof error.body?.message === 'string'
          ? error.body.message
          : 'メールの送信に失敗しました。しばらくしてから再度お試しください。'
      toast({
        variant: 'destructive',
        title: '送信に失敗しました',
        description,
      })
    },
  })
}

/**
 * パスワードリセットトークンの有効性を検証するクエリフック。
 */
export function useVerifyResetToken(token: string | null) {
  return useQuery({
    queryKey: ['password-reset', 'verify', token],
    queryFn: () => AuthService.verifyResetToken(token!),
    enabled: !!token,
    retry: false,
  })
}

/**
 * パスワード再設定 API を呼び出すミューテーションフック。
 */
export function useResetPassword() {
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: ResetPasswordRequest): Promise<SimpleStatusResponse> =>
      AuthService.resetPassword(data),
    onSuccess: () => {
      toast({
        title: 'パスワードを再設定しました',
        description: '新しいパスワードでログインしてください。',
      })
    },
    onError: (error: unknown) => {
      const description =
        error instanceof ApiError && typeof error.body?.message === 'string'
          ? error.body.message
          : 'パスワードの再設定に失敗しました。リンクの有効期限をご確認ください。'
      toast({
        variant: 'destructive',
        title: '再設定に失敗しました',
        description,
      })
    },
  })
}

/**
 * 初回パスワード設定 API を呼び出すミューテーションフック。
 */
export function useFirstLogin() {
  const navigate = useNavigate()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: FirstLoginRequest): Promise<SimpleStatusResponse> =>
      AuthService.firstLogin(data),
    onSuccess: () => {
      toast({
        title: 'パスワードを設定しました',
        description: '新しいパスワードでログインしてください。',
      })
      navigate('/login')
    },
    onError: (error: unknown) => {
      let description = '仮パスワードまたはメールアドレスが正しくありません'
      if (error instanceof ApiError) {
        if (typeof error.body?.message === 'string') {
          description = error.body.message
        }
      } else if (isAxiosError(error) && error.response?.data) {
        const data = error.response.data as { message?: string }
        if (typeof data.message === 'string') {
          description = data.message
        }
      }
      toast({
        variant: 'destructive',
        title: '設定に失敗しました',
        description,
      })
    },
  })
}

/**
 * ログアウト API を呼び出し、ローカル認証状態を破棄するミューテーションフック。
 */
export function useLogout() {
  const navigate = useNavigate()
  const { logout, accessToken } = useAuthStore()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async () => {
      // リフレッシュ Cookie の失効には API 呼び出しが必須（ローカル clear のみでは不十分）
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
