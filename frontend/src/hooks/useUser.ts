import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { apiClient } from '@/lib/axios'
import { useAuthStore, type User } from '@/store/useAuthStore'
import { useToast } from '@/hooks/use-toast'
import type { UpdateUserRequest, UserResponse } from '@/types/auth'

/**
 * 認証済みの場合に `/api/users/me` から現在ユーザーを取得するクエリフック。
 *
 * 取得成功時は `useAuthStore` の `user` を同期更新する。
 *
 * @returns `react-query` の `UseQueryResult`。`enabled` は `isAuthenticated` に連動。
 */
export function useCurrentUser() {
  const { isAuthenticated, setUser } = useAuthStore()

  return useQuery({
    queryKey: ['currentUser'],
    queryFn: async (): Promise<UserResponse> => {
      const response = await apiClient.get<UserResponse>('/api/users/me')
      const user: User = {
        id: response.data.id!,
        email: response.data.email!,
        name: response.data.name!,
        role: response.data.role!,
      }
      setUser(user)
      return response.data
    },
    enabled: isAuthenticated,
    staleTime: 1000 * 60 * 5,
  })
}

/**
 * ログインユーザーのプロフィール更新（`PUT /api/users/me`）用ミューテーションフック。
 *
 * 成功時はストアのユーザーと `currentUser` クエリを無効化し、トーストを表示する。
 *
 * @returns `UseMutationResult`。`mutate(data)` に `UpdateUserRequest` を渡す。
 */
export function useUpdateUser() {
  const queryClient = useQueryClient()
  const { setUser } = useAuthStore()
  const { toast } = useToast()

  return useMutation({
    mutationFn: async (data: UpdateUserRequest): Promise<UserResponse> => {
      const response = await apiClient.put<UserResponse>('/api/users/me', data)
      return response.data
    },
    onSuccess: (data) => {
      const user: User = {
        id: data.id!,
        email: data.email!,
        name: data.name!,
        role: data.role!,
      }
      setUser(user)
      queryClient.invalidateQueries({ queryKey: ['currentUser'] })
      toast({
        title: '更新成功',
        description: 'プロフィールが更新されました',
      })
    },
    onError: (error: { response?: { data?: { message?: string } } }) => {
      toast({
        variant: 'destructive',
        title: '更新失敗',
        description:
          error.response?.data?.message || 'プロフィールの更新に失敗しました',
      })
    },
  })
}
