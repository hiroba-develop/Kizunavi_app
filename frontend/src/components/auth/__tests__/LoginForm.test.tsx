import { screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { LoginForm } from '@/components/auth/LoginForm'
import { renderWithProviders } from '@/test/test-utils'

const mutateMock = vi.fn()

vi.mock('@/hooks/useAuth', () => ({
  useLogin: () => ({
    mutate: mutateMock,
    isPending: false,
  }),
}))

describe('LoginForm', () => {
  beforeEach(() => {
    mutateMock.mockReset()
  })

  it('default variant で shadcn ボタンを表示する', () => {
    renderWithProviders(<LoginForm />)

    expect(screen.getByRole('button', { name: 'ログイン' })).toBeInTheDocument()
    expect(screen.queryByRole('link', { name: 'パスワードをお忘れですか？' })).not.toBeInTheDocument()
  })

  it('brand variant でリンクとブランドスタイルを表示する', () => {
    renderWithProviders(<LoginForm variant="brand" />)

    expect(screen.getByPlaceholderText('kizunavi@hiroba1931.co.jp')).toBeInTheDocument()
    expect(screen.getByRole('link', { name: 'パスワードをお忘れですか？' })).toHaveAttribute(
      'href',
      '/forgot-password'
    )
    expect(screen.getByRole('link', { name: '初回ログイン（仮パスワードの変更）' })).toHaveAttribute(
      'href',
      '/first-login'
    )
  })

  it('バリデーションエラーを表示する', async () => {
    const user = userEvent.setup()
    renderWithProviders(<LoginForm />)

    await user.click(screen.getByRole('button', { name: 'ログイン' }))

    expect(await screen.findByText('有効なメールアドレスを入力してください')).toBeInTheDocument()
    expect(screen.getByText('パスワードを入力してください')).toBeInTheDocument()
  })

  it('送信時に login.mutate を呼ぶ', async () => {
    const user = userEvent.setup()
    renderWithProviders(<LoginForm />)

    await user.type(screen.getByLabelText('メールアドレス'), 'user@example.com')
    await user.type(screen.getByLabelText('パスワード'), 'secret')
    await user.click(screen.getByRole('button', { name: 'ログイン' }))

    await waitFor(() => {
      expect(mutateMock).toHaveBeenCalledWith({
        email: 'user@example.com',
        password: 'secret',
      })
    })
  })
})
