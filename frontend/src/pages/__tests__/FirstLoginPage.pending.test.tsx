import { screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { FirstLoginPage } from '@/pages/FirstLoginPage'
import { renderWithProviders } from '@/test/test-utils'

vi.mock('@/hooks/useAuth', () => ({
  useFirstLogin: () => ({
    mutate: vi.fn(),
    isPending: true,
  }),
}))

describe('FirstLoginPage pending', () => {
  it('送信中はボタンを無効化する', () => {
    renderWithProviders(<FirstLoginPage />)

    expect(screen.getByRole('button', { name: 'パスワードを設定' })).toBeDisabled()
  })
})
