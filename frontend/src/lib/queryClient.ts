import { QueryClient } from '@tanstack/react-query'

/**
 * アプリ共通の React Query クライアント。
 *
 * - クエリは 5 分間 fresh、30 分でガベージコレクト
 * - 401 ではリトライしない（インターセプタ側でリフレッシュまたはログアウト）
 * - ミューテーションはリトライしない
 */
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
      gcTime: 1000 * 60 * 30,
      retry: (failureCount, error) => {
        if ((error as { response?: { status: number } })?.response?.status === 401) {
          return false
        }
        return failureCount < 3
      },
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: false,
    },
  },
})
