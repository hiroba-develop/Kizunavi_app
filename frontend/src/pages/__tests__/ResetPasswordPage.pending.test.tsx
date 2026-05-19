import { screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { ResetPasswordPage } from '@/pages/ResetPasswordPage'
import { renderWithProviders } from '@/test/test-utils'

const verifyState = vi.hoisted(() => ({
  data: { valid: true } as { valid?: boolean },
  isLoading: false,
  isError: false,
}))

vi.mock('@/hooks/useAuth', () => ({
  useVerifyResetToken: () => verifyState,
  useResetPassword: () => ({
    mutate: vi.fn(),
    isPending: true,
  }),
}))

describe('ResetPasswordPage pending', () => {
  it('送信中はボタンを無効化する', () => {
    renderWithProviders(<ResetPasswordPage />, {
      route: '/reset-password?token=abc',
    })

    expect(screen.getByRole('button', { name: 'パスワードを再設定' })).toBeDisabled()
  })
})
