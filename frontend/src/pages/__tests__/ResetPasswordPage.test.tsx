import { fireEvent, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { ResetPasswordPage } from '@/pages/ResetPasswordPage'
import { renderWithProviders } from '@/test/test-utils'

const mutateMock = vi.fn()

const searchParamsMock = vi.hoisted(() =>
  vi.fn(() => [new URLSearchParams('token=abc')] as const)
)

const verifyState = vi.hoisted(() => ({
  data: undefined as { valid?: boolean; message?: string } | undefined,
  isLoading: false,
  isError: false,
}))

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useSearchParams: () => searchParamsMock(),
  }
})

vi.mock('@/hooks/useAuth', () => ({
  useVerifyResetToken: () => verifyState,
  useResetPassword: () => ({
    mutate: mutateMock,
    isPending: false,
  }),
}))

describe('ResetPasswordPage', () => {
  beforeEach(() => {
    mutateMock.mockReset()
    searchParamsMock.mockReturnValue([new URLSearchParams('token=abc')])
    verifyState.data = undefined
    verifyState.isLoading = false
    verifyState.isError = false
  })

  it('トークンなしのとき無効リンクを表示する', () => {
    searchParamsMock.mockReturnValue([new URLSearchParams('')])
    renderWithProviders(<ResetPasswordPage />, { route: '/reset-password' })

    expect(screen.getByText('無効なリンクです')).toBeInTheDocument()
    expect(screen.getByRole('link', { name: 'パスワード再設定リンクを再送信' })).toHaveAttribute(
      'href',
      '/forgot-password'
    )
  })

  it('検証中はローディングを表示する', () => {
    verifyState.isLoading = true

    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    expect(screen.getByText('リンクを確認しています…')).toBeInTheDocument()
  })

  it('検証エラー時にメッセージを表示する', () => {
    verifyState.isError = true

    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    expect(screen.getByText('リンクを利用できません')).toBeInTheDocument()
    expect(
      screen.getByText('このリンクは無効、期限切れ、または使用済みです。')
    ).toBeInTheDocument()
  })

  it('無効トークン応答時に API メッセージを表示する', () => {
    verifyState.data = { valid: false, message: '期限切れです' }

    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    expect(screen.getByText('期限切れです')).toBeInTheDocument()
  })

  it('有効トークン時にフォームを表示しバリデーションする', async () => {
    verifyState.data = { valid: true }
    const user = userEvent.setup()

    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    await user.click(screen.getByRole('button', { name: 'パスワードを再設定' }))

    expect(await screen.findByText('パスワードは8文字以上で入力してください')).toBeInTheDocument()
  })

  it('パスワード不一致のときエラーを表示する', async () => {
    verifyState.data = { valid: true }
    const user = userEvent.setup()
    const { container } = renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    await user.type(screen.getByLabelText('新しいパスワード'), 'NewPass123')
    await user.type(screen.getByLabelText('新しいパスワード（確認）'), 'Mismatch12')
    fireEvent.submit(container.querySelector('form')!)

    expect(await screen.findByText('パスワードが一致しません')).toBeInTheDocument()
  })

  it('送信成功時に完了画面を表示する', async () => {
    verifyState.data = { valid: true }
    mutateMock.mockImplementation((_data, options) => {
      options?.onSuccess?.()
    })
    const user = userEvent.setup()

    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    await user.type(screen.getByLabelText('新しいパスワード'), 'NewPass123')
    await user.type(screen.getByLabelText('新しいパスワード（確認）'), 'NewPass123')
    await user.click(screen.getByRole('button', { name: 'パスワードを再設定' }))

    await waitFor(() => {
      expect(screen.getByText('パスワードを再設定しました')).toBeInTheDocument()
    })
    expect(mutateMock).toHaveBeenCalledWith(
      { token: 'abc', newPassword: 'NewPass123' },
      expect.objectContaining({ onSuccess: expect.any(Function) })
    )
  })
})
