import { fireEvent, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { FirstLoginPage } from '@/pages/FirstLoginPage'
import { renderWithProviders } from '@/test/test-utils'

const mutateMock = vi.fn()

vi.mock('@/hooks/useAuth', () => ({
  useFirstLogin: () => ({
    mutate: mutateMock,
    isPending: false,
  }),
}))

describe('FirstLoginPage', () => {
  beforeEach(() => {
    mutateMock.mockReset()
  })

  it('初回パスワード設定フォームを表示する', () => {
    renderWithProviders(<FirstLoginPage />)

    expect(screen.getByText('初回パスワード設定')).toBeInTheDocument()
    expect(screen.getByLabelText('メールアドレス')).toBeInTheDocument()
    expect(screen.getByLabelText('仮パスワード')).toBeInTheDocument()
    expect(screen.getByRole('link', { name: 'パスワード再設定' })).toHaveAttribute(
      'href',
      '/forgot-password'
    )
  })

  it('バリデーションエラーを表示する', async () => {
    const { container } = renderWithProviders(<FirstLoginPage />)

    fireEvent.submit(container.querySelector('form')!)

    expect(await screen.findByText('有効なメールアドレスを入力してください')).toBeInTheDocument()
    expect(screen.getByText('仮パスワードを入力してください')).toBeInTheDocument()
    expect(screen.getByText('パスワードは8文字以上で入力してください')).toBeInTheDocument()
  })

  it('パスワード不一致のときエラーを表示する', async () => {
    const user = userEvent.setup()
    renderWithProviders(<FirstLoginPage />)

    await user.type(screen.getByLabelText('メールアドレス'), 'user@example.com')
    await user.type(screen.getByLabelText('仮パスワード'), 'TempPass001')
    await user.type(screen.getByLabelText('新しいパスワード'), 'NewPass123')
    await user.type(screen.getByLabelText('新しいパスワード（確認）'), 'Mismatch12')
    await user.click(screen.getByRole('button', { name: 'パスワードを設定' }))

    expect(await screen.findByText('パスワードが一致しません')).toBeInTheDocument()
  })

  it('送信成功時に完了画面を表示する', async () => {
    mutateMock.mockImplementation((_data, options) => {
      options?.onSuccess?.()
    })
    const user = userEvent.setup()
    renderWithProviders(<FirstLoginPage />)

    await user.type(screen.getByLabelText('メールアドレス'), 'user@example.com')
    await user.type(screen.getByLabelText('仮パスワード'), 'TempPass001')
    await user.type(screen.getByLabelText('新しいパスワード'), 'NewPass123')
    await user.type(screen.getByLabelText('新しいパスワード（確認）'), 'NewPass123')
    await user.click(screen.getByRole('button', { name: 'パスワードを設定' }))

    await waitFor(() => {
      expect(screen.getByText('パスワードを設定しました')).toBeInTheDocument()
      expect(screen.getByRole('link', { name: 'ログイン画面へ' })).toHaveAttribute('href', '/login')
    })
    expect(mutateMock).toHaveBeenCalledWith(
      {
        email: 'user@example.com',
        tempPassword: 'TempPass001',
        newPassword: 'NewPass123',
      },
      expect.objectContaining({ onSuccess: expect.any(Function) })
    )
  })
})
