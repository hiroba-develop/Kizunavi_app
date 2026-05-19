import { screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { LoginForm } from '@/components/auth/LoginForm'
import { renderWithProviders } from '@/test/test-utils'

vi.mock('@/hooks/useAuth', () => ({
  useLogin: () => ({
    mutate: vi.fn(),
    isPending: true,
  }),
}))

describe('LoginForm pending', () => {
  it('default variant で送信中はボタンを無効化する', () => {
    renderWithProviders(<LoginForm />)

    expect(screen.getByRole('button', { name: 'ログイン' })).toBeDisabled()
  })

  it('brand variant で送信中はボタンを無効化する', () => {
    renderWithProviders(<LoginForm variant="brand" />)

    expect(screen.getByRole('button', { name: 'ログイン' })).toBeDisabled()
  })
})
