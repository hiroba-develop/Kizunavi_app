import { screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { ForgotPasswordPage } from '@/pages/ForgotPasswordPage'
import { renderWithProviders } from '@/test/test-utils'

vi.mock('@/hooks/useAuth', () => ({
  useForgotPassword: () => ({
    mutateAsync: vi.fn(),
    isPending: true,
  }),
}))

vi.mock('@/hooks/use-toast', () => ({
  useToast: () => ({ toast: vi.fn() }),
}))

describe('ForgotPasswordPage pending', () => {
  it('送信中はボタンを無効化する', () => {
    renderWithProviders(<ForgotPasswordPage />)

    expect(screen.getByRole('button', { name: 'リセットリンクを送信' })).toBeDisabled()
  })
})
