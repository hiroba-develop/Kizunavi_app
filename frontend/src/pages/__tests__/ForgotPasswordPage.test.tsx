import { fireEvent, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { ForgotPasswordPage } from '@/pages/ForgotPasswordPage'
import { renderWithProviders } from '@/test/test-utils'

const mutateAsyncMock = vi.fn()
const toastMock = vi.fn()

vi.mock('@/hooks/useAuth', () => ({
  useForgotPassword: () => ({
    mutateAsync: mutateAsyncMock,
    isPending: false,
  }),
}))

vi.mock('@/hooks/use-toast', () => ({
  useToast: () => ({ toast: toastMock }),
}))

describe('ForgotPasswordPage', () => {
  beforeEach(() => {
    mutateAsyncMock.mockReset()
    toastMock.mockReset()
    mutateAsyncMock.mockResolvedValue({ responseStatus: 'success' })
  })

  it('メール入力フォームを表示する', () => {
    renderWithProviders(<ForgotPasswordPage />)

    expect(screen.getByText('パスワード再設定')).toBeInTheDocument()
    expect(screen.getByLabelText('メールアドレス')).toBeInTheDocument()
  })

  it('無効なメールのときバリデーションエラーを表示する', async () => {
    const user = userEvent.setup()
    const { container } = renderWithProviders(<ForgotPasswordPage />)

    const emailInput = screen.getByLabelText('メールアドレス')
    await user.type(emailInput, 'invalid')
    fireEvent.submit(container.querySelector('form')!)

    expect(await screen.findByText('有効なメールアドレスを入力してください')).toBeInTheDocument()
  })

  it('送信成功時に完了画面と toast を表示する', async () => {
    const user = userEvent.setup()
    renderWithProviders(<ForgotPasswordPage />)

    await user.type(screen.getByLabelText('メールアドレス'), 'user@example.com')
    await user.click(screen.getByRole('button', { name: 'リセットリンクを送信' }))

    await waitFor(() => {
      expect(screen.getByText('リセットリンクの送信を受け付けました')).toBeInTheDocument()
      expect(screen.getByText(/user@example.com/)).toBeInTheDocument()
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({ title: '送信を受け付けました' })
      )
    })
  })
})
