import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { renderHook, waitFor } from '@testing-library/react'
import type { ReactNode } from 'react'
import { MemoryRouter } from 'react-router-dom'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { ApiError, Role } from '@/api'
import {
  useFirstLogin,
  useForgotPassword,
  useLogin,
  useLogout,
  useResetPassword,
  useVerifyResetToken,
} from '@/hooks/useAuth'
import { useAuthStore } from '@/store/useAuthStore'

const {
  isAxiosErrorMock,
  navigateMock,
  toastMock,
  loginMock,
  forgotPasswordMock,
  verifyResetTokenMock,
  resetPasswordMock,
  firstLoginMock,
  getMock,
  postMock,
} = vi.hoisted(() => ({
  isAxiosErrorMock: vi.fn<(error: unknown) => boolean>(),
  navigateMock: vi.fn(),
  toastMock: vi.fn(),
  loginMock: vi.fn(),
  forgotPasswordMock: vi.fn(),
  verifyResetTokenMock: vi.fn(),
  resetPasswordMock: vi.fn(),
  firstLoginMock: vi.fn(),
  getMock: vi.fn(),
  postMock: vi.fn(),
}))

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => navigateMock,
  }
})

vi.mock('@/hooks/use-toast', () => ({
  useToast: () => ({ toast: toastMock }),
}))

vi.mock('@/api', async () => {
  const actual = await vi.importActual<typeof import('@/api')>('@/api')
  return {
    ...actual,
    AuthService: {
      login: loginMock,
      forgotPassword: forgotPasswordMock,
      verifyResetToken: verifyResetTokenMock,
      resetPassword: resetPasswordMock,
      firstLogin: firstLoginMock,
    },
  }
})

vi.mock('@/lib/axios', () => ({
  apiClient: {
    get: getMock,
    post: postMock,
  },
}))

vi.mock('axios', async () => {
  const actual = await vi.importActual<typeof import('axios')>('axios')
  return {
    ...actual,
    isAxiosError: (error: unknown) => isAxiosErrorMock(error),
  }
})

function createApiError(status: number, body: { message?: string }) {
  return new ApiError(
    { method: 'POST', url: '/test' },
    { url: '/test', ok: false, status, statusText: 'Error', body },
    'Error'
  )
}

function wrapper({ children }: { children: ReactNode }) {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false }, mutations: { retry: false } },
  })
  return (
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>{children}</MemoryRouter>
    </QueryClientProvider>
  )
}

describe('useAuth hooks', () => {
  beforeEach(() => {
    isAxiosErrorMock.mockReturnValue(false)
    navigateMock.mockReset()
    toastMock.mockReset()
    loginMock.mockReset()
    forgotPasswordMock.mockReset()
    verifyResetTokenMock.mockReset()
    resetPasswordMock.mockReset()
    firstLoginMock.mockReset()
    getMock.mockReset()
    postMock.mockReset()
    useAuthStore.getState().clearAuth()
  })

  afterEach(() => {
    useAuthStore.getState().clearAuth()
  })

  it('useLogin: 成功時にユーザー情報を反映しダッシュボードへ遷移する', async () => {
    loginMock.mockResolvedValue({ token: 'access-token', name: '田中太郎' })
    getMock.mockResolvedValue({
      data: {
        id: 1,
        email: 'user@example.com',
        name: '田中太郎',
        role: Role.ROLE_USER,
      },
    })

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(useAuthStore.getState().accessToken).toBe('access-token')
      expect(useAuthStore.getState().user).toMatchObject({
        id: 1,
        email: 'user@example.com',
        name: '田中太郎',
        role: Role.ROLE_USER,
      })
      expect(navigateMock).toHaveBeenCalledWith('/dashboard')
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ title: 'ログイン成功' })
      )
    })
  })

  it('useLogin: /users/me 失敗時もログインを継続する', async () => {
    loginMock.mockResolvedValue({ token: 'access-token', name: '田中太郎' })
    getMock.mockRejectedValue(new Error('me failed'))

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(useAuthStore.getState().accessToken).toBe('access-token')
      expect(useAuthStore.getState().user?.name).toBe('田中太郎')
      expect(navigateMock).toHaveBeenCalledWith('/dashboard')
    })
  })

  it('useLogin: ApiError 423 でメッセージなしのときデフォルト説明を表示する', async () => {
    loginMock.mockRejectedValue(createApiError(423, {}))

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          description: 'しばらくしてから再度お試しください',
        })
      )
    })
  })

  it('useLogin: ApiError 423 のときロック toast を表示する', async () => {
    loginMock.mockRejectedValue(createApiError(423, { message: 'ロック中' }))

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          variant: 'destructive',
          title: 'アカウントがロックされています',
          description: 'ロック中',
        })
      )
    })
  })

  it('useLogin: Axios 423 でメッセージなしのときデフォルト説明を表示する', async () => {
    isAxiosErrorMock.mockReturnValue(true)
    loginMock.mockRejectedValue({ response: { status: 423, data: {} } })

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          description: 'しばらくしてから再度お試しください',
        })
      )
    })
  })

  it('useLogin: Axios 423 のときロック toast を表示する', async () => {
    const axiosError = {
      response: { status: 423, data: { message: 'しばらくお待ちください' } },
    }
    isAxiosErrorMock.mockReturnValue(true)
    loginMock.mockRejectedValue(axiosError)

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          title: 'アカウントがロックされています',
          description: 'しばらくお待ちください',
        })
      )
    })
  })

  it('useLogin: 一般エラー時にログイン失敗 toast を表示する', async () => {
    loginMock.mockRejectedValue(new Error('failed'))

    const { result } = renderHook(() => useLogin(), { wrapper })
    result.current.mutate({ email: 'user@example.com', password: 'pass' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          variant: 'destructive',
          title: 'ログイン失敗',
        })
      )
    })
  })

  it('useForgotPassword: ApiError 時にカスタムメッセージを toast する', async () => {
    forgotPasswordMock.mockRejectedValue(createApiError(400, { message: '送信不可' }))

    const { result } = renderHook(() => useForgotPassword(), { wrapper })
    result.current.mutate({ email: 'user@example.com' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          title: '送信に失敗しました',
          description: '送信不可',
        })
      )
    })
  })

  it('useForgotPassword: その他エラー時にデフォルトメッセージを toast する', async () => {
    forgotPasswordMock.mockRejectedValue(new Error('network'))

    const { result } = renderHook(() => useForgotPassword(), { wrapper })
    result.current.mutate({ email: 'user@example.com' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          description: 'メールの送信に失敗しました。しばらくしてから再度お試しください。',
        })
      )
    })
  })

  it('useVerifyResetToken: token があるとき検証 API を呼ぶ', async () => {
    verifyResetTokenMock.mockResolvedValue({ valid: true })

    const { result } = renderHook(() => useVerifyResetToken('abc'), { wrapper })

    await waitFor(() => {
      expect(verifyResetTokenMock).toHaveBeenCalledWith('abc')
      expect(result.current.data?.valid).toBe(true)
    })
  })

  it('useResetPassword: 成功時に toast を表示する', async () => {
    resetPasswordMock.mockResolvedValue({ responseStatus: 'success' })

    const { result } = renderHook(() => useResetPassword(), { wrapper })
    result.current.mutate({ token: 't', newPassword: 'NewPass123' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ title: 'パスワードを再設定しました' })
      )
    })
  })

  it('useResetPassword: ApiError 時にカスタムメッセージを toast する', async () => {
    resetPasswordMock.mockRejectedValue(createApiError(400, { message: '期限切れ' }))

    const { result } = renderHook(() => useResetPassword(), { wrapper })
    result.current.mutate({ token: 't', newPassword: 'NewPass123' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ description: '期限切れ' })
      )
    })
  })

  it('useResetPassword: その他エラー時にデフォルトメッセージを toast する', async () => {
    resetPasswordMock.mockRejectedValue(new Error('fail'))

    const { result } = renderHook(() => useResetPassword(), { wrapper })
    result.current.mutate({ token: 't', newPassword: 'NewPass123' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          description: 'パスワードの再設定に失敗しました。リンクの有効期限をご確認ください。',
        })
      )
    })
  })

  it('useFirstLogin: 成功時に toast 表示とログイン画面へ遷移する', async () => {
    firstLoginMock.mockResolvedValue({ responseStatus: 'success' })

    const { result } = renderHook(() => useFirstLogin(), { wrapper })
    result.current.mutate({
      email: 'user@example.com',
      tempPassword: 'temp',
      newPassword: 'newpass12',
    })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ title: 'パスワードを設定しました' })
      )
      expect(navigateMock).toHaveBeenCalledWith('/login')
    })
  })

  it('useFirstLogin: ApiError 時にカスタムメッセージを toast する', async () => {
    firstLoginMock.mockRejectedValue(createApiError(400, { message: '仮パスワード不正' }))

    const { result } = renderHook(() => useFirstLogin(), { wrapper })
    result.current.mutate({
      email: 'user@example.com',
      tempPassword: 'temp',
      newPassword: 'newpass12',
    })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ description: '仮パスワード不正' })
      )
    })
  })

  it('useFirstLogin: Axios エラー時にメッセージを toast する', async () => {
    const axiosError = {
      response: { data: { message: 'axios-msg' } },
    }
    isAxiosErrorMock.mockReturnValue(true)
    firstLoginMock.mockRejectedValue(axiosError)

    const { result } = renderHook(() => useFirstLogin(), { wrapper })
    result.current.mutate({
      email: 'user@example.com',
      tempPassword: 'temp',
      newPassword: 'newpass12',
    })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ description: 'axios-msg' })
      )
    })
  })

  it('useFirstLogin: その他エラー時にデフォルトメッセージを toast する', async () => {
    isAxiosErrorMock.mockReturnValue(false)
    firstLoginMock.mockRejectedValue(new Error('fail'))

    const { result } = renderHook(() => useFirstLogin(), { wrapper })
    result.current.mutate({
      email: 'user@example.com',
      tempPassword: 'temp',
      newPassword: 'newpass12',
    })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          description: '仮パスワードまたはメールアドレスが正しくありません',
        })
      )
    })
  })

  it('useLogout: accessToken ありのとき logout API を呼ぶ', async () => {
    useAuthStore.getState().setAccessToken('token')
    postMock.mockResolvedValue({})

    const { result } = renderHook(() => useLogout(), { wrapper })
    result.current.mutate()

    await waitFor(() => {
      expect(postMock).toHaveBeenCalledWith('/api/auth/logout')
      expect(navigateMock).toHaveBeenCalledWith('/login')
    })
  })

  it('useLogout: accessToken なしのとき API を呼ばずログアウトする', async () => {
    const { result } = renderHook(() => useLogout(), { wrapper })
    result.current.mutate()

    await waitFor(() => {
      expect(postMock).not.toHaveBeenCalled()
      expect(navigateMock).toHaveBeenCalledWith('/login')
    })
  })

  it('useLogout: API 失敗時もローカル状態を破棄する', async () => {
    useAuthStore.getState().setAccessToken('token')
    postMock.mockRejectedValue(new Error('logout failed'))

    const { result } = renderHook(() => useLogout(), { wrapper })
    result.current.mutate()

    await waitFor(() => {
      expect(useAuthStore.getState().accessToken).toBeNull()
      expect(navigateMock).toHaveBeenCalledWith('/login')
    })
  })
})
