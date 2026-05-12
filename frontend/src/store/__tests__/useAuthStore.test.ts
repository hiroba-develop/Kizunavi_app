import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Role } from '@/api'
import { useAuthStore } from '@/store/useAuthStore'

describe('useAuthStore', () => {
  beforeEach(() => {
    localStorage.clear()
    useAuthStore.setState({
      accessToken: null,
      user: null,
      isAuthenticated: false,
      isBootstrapping: true,
    })
  })

  afterEach(() => {
    useAuthStore.getState().clearAuth()
  })

  it('setAccessToken で認証済み状態になる', () => {
    useAuthStore.getState().setAccessToken('access-token')

    const state = useAuthStore.getState()
    expect(state.accessToken).toBe('access-token')
    expect(state.isAuthenticated).toBe(true)
  })

  it('setUser でユーザー情報を保持する', () => {
    useAuthStore.getState().setUser({
      id: 1,
      email: 'user@example.com',
      name: 'テストユーザー',
      role: Role.ROLE_USER,
    })

    expect(useAuthStore.getState().user).toEqual({
      id: 1,
      email: 'user@example.com',
      name: 'テストユーザー',
      role: Role.ROLE_USER,
    })
  })

  it('setBootstrapped で起動中フラグを下ろす', () => {
    expect(useAuthStore.getState().isBootstrapping).toBe(true)

    useAuthStore.getState().setBootstrapped()

    expect(useAuthStore.getState().isBootstrapping).toBe(false)
  })

  it('logout で認証情報を全てクリアする', () => {
    const state = useAuthStore.getState()
    state.setAccessToken('access-token')
    state.setUser({
      id: 1,
      email: 'user@example.com',
      name: 'テストユーザー',
      role: Role.ROLE_USER,
    })

    state.logout()

    const after = useAuthStore.getState()
    expect(after.accessToken).toBeNull()
    expect(after.user).toBeNull()
    expect(after.isAuthenticated).toBe(false)
  })

  it('localStorage には accessToken / isAuthenticated / isBootstrapping を永続化しない', () => {
    useAuthStore.getState().setAccessToken('access-token')
    useAuthStore.getState().setUser({
      id: 1,
      email: 'user@example.com',
      name: 'テストユーザー',
      role: Role.ROLE_USER,
    })
    useAuthStore.getState().setBootstrapped()

    const raw = localStorage.getItem('auth-storage')
    expect(raw).not.toBeNull()
    const persisted = JSON.parse(raw as string) as {
      state: Record<string, unknown>
    }

    expect(persisted.state).toHaveProperty('user')
    expect(persisted.state).not.toHaveProperty('accessToken')
    expect(persisted.state).not.toHaveProperty('isAuthenticated')
    expect(persisted.state).not.toHaveProperty('isBootstrapping')
  })
})
