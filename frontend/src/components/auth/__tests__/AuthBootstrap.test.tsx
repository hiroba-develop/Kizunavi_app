import { render, screen, waitFor } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { AuthBootstrap, resetAuthBootstrapForTests } from '@/components/auth/AuthBootstrap'
import { Role } from '@/api'
import { useAuthStore } from '@/store/useAuthStore'

const postMock = vi.hoisted(() => vi.fn())

vi.mock('axios', () => ({
  default: {
    post: postMock,
  },
}))

describe('AuthBootstrap', () => {
  beforeEach(() => {
    postMock.mockReset()
    resetAuthBootstrapForTests()
    useAuthStore.getState().clearAuth()
    useAuthStore.setState({ isBootstrapping: true })
  })

  it('ブートストラップ中は読み込み表示を出す', () => {
    postMock.mockReturnValue(new Promise(() => {}))

    render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    expect(screen.getByRole('status')).toHaveTextContent('読み込み中...')
    expect(screen.queryByTestId('child')).not.toBeInTheDocument()
  })

  it('refresh 成功時に accessToken を復元する', async () => {
    postMock.mockResolvedValue({ data: { token: 'restored-token' } })

    render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    await waitFor(() => {
      expect(screen.getByTestId('child')).toBeInTheDocument()
      expect(useAuthStore.getState().accessToken).toBe('restored-token')
      expect(useAuthStore.getState().isBootstrapping).toBe(false)
    })
    expect(postMock).toHaveBeenCalledWith(
      '/api/auth/refresh',
      {},
      expect.objectContaining({ withCredentials: true })
    )
  })

  it('token が無い応答のとき clearAuth する', async () => {
    useAuthStore.getState().setUser({
      id: 1,
      email: 'user@example.com',
      name: 'user',
      role: Role.ROLE_USER,
    })
    postMock.mockResolvedValue({ data: {} })

    render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    await waitFor(() => {
      expect(screen.getByTestId('child')).toBeInTheDocument()
      expect(useAuthStore.getState().accessToken).toBeNull()
      expect(useAuthStore.getState().user).toBeNull()
    })
  })

  it('二重マウント時は refresh API を再実行しない', async () => {
    postMock.mockResolvedValue({ data: { token: 'token' } })

    const { unmount } = render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    await waitFor(() => {
      expect(screen.getByTestId('child')).toBeInTheDocument()
    })
    const callCount = postMock.mock.calls.length
    unmount()

    useAuthStore.setState({ isBootstrapping: true })
    render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    await new Promise((resolve) => setTimeout(resolve, 50))
    expect(postMock.mock.calls.length).toBe(callCount)
  })

  it('refresh 失敗時に clearAuth する', async () => {
    useAuthStore.getState().setAccessToken('old')
    postMock.mockRejectedValue(new Error('refresh failed'))

    render(
      <AuthBootstrap>
        <div data-testid="child">child</div>
      </AuthBootstrap>
    )

    await waitFor(() => {
      expect(screen.getByTestId('child')).toBeInTheDocument()
      expect(useAuthStore.getState().accessToken).toBeNull()
    })
  })
})
