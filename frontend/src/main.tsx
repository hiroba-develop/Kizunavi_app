import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from 'react-router-dom'
import { OpenAPI } from '@/api'
import { queryClient } from '@/lib/queryClient'
import { router } from '@/routes'
import { Toaster } from '@/components/ui/toaster'
import { AuthBootstrap } from '@/components/auth/AuthBootstrap'
import '@/styles/globals.css'

OpenAPI.BASE = import.meta.env.VITE_API_BASE_URL || ''
OpenAPI.WITH_CREDENTIALS = true
OpenAPI.CREDENTIALS = 'include'

/**
 * React アプリケーションのマウントポイント。
 *
 * `StrictMode`・React Query・ルータ・トースターをルートにラップして描画する。
 * `AuthBootstrap` は起動時の silent refresh を担い、完了するまでルータを描画しない。
 */
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthBootstrap>
        <RouterProvider router={router} />
      </AuthBootstrap>
      <Toaster />
    </QueryClientProvider>
  </StrictMode>,
)
