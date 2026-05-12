import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { renderHook, waitFor } from '@testing-library/react'
import type { ReactNode } from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { Role } from '@/api'
import { useCurrentUser, useUpdateUser } from '@/hooks/useUser'
import { useAuthStore } from '@/store/useAuthStore'

const { getMock, putMock, toastMock } = vi.hoisted(() => ({
  getMock: vi.fn(),
  putMock: vi.fn(),
  toastMock: vi.fn(),
}))

vi.mock('@/lib/axios', () => ({
  apiClient: {
    get: getMock,
    put: putMock,
  },
}))

vi.mock('@/hooks/use-toast', () => ({
  useToast: () => ({
    toast: toastMock,
  }),
}))

describe('useUser hooks', () => {
  beforeEach(() => {
    getMock.mockReset()
    putMock.mockReset()
    toastMock.mockReset()
    useAuthStore.getState().clearAuth()
    useAuthStore.getState().setAccessToken('access-token')
  })

  afterEach(() => {
    useAuthStore.getState().clearAuth()
  })

  it('useCurrentUser: 取得成功時にストアへユーザーを反映する', async () => {
    getMock.mockResolvedValue({
      data: {
        id: 1,
        email: 'user@example.com',
        name: '田中太郎',
        role: Role.ROLE_USER,
      },
    })

    const queryClient = new QueryClient()
    const wrapper = ({ children }: { children: ReactNode }) => (
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    )

    renderHook(() => useCurrentUser(), { wrapper })

    await waitFor(() => {
      expect(useAuthStore.getState().user).toMatchObject({
        id: 1,
        email: 'user@example.com',
        name: '田中太郎',
        role: Role.ROLE_USER,
      })
    })
  })

  it('useUpdateUser: 失敗時に destructive toast を表示する', async () => {
    putMock.mockRejectedValue({
      response: {
        data: {
          message: '更新エラー',
        },
      },
    })

    const queryClient = new QueryClient()
    const wrapper = ({ children }: { children: ReactNode }) => (
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    )

    const { result } = renderHook(() => useUpdateUser(), { wrapper })
    result.current.mutate({ name: '変更後ユーザー名' })

    await waitFor(() => {
      expect(toastMock).toHaveBeenCalledWith(
        expect.objectContaining({
          variant: 'destructive',
          title: '更新失敗',
          description: '更新エラー',
        })
      )
    })
  })
})
