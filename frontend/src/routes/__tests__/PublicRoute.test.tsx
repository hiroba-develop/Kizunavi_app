import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { createMemoryRouter, RouterProvider } from 'react-router-dom'
import { PublicRoute } from '@/routes/PublicRoute'
import { useAuthStore } from '@/store/useAuthStore'

describe('PublicRoute', () => {
  it('未認証のとき子ルートを表示する', () => {
    useAuthStore.getState().clearAuth()

    const router = createMemoryRouter(
      [
        {
          element: <PublicRoute />,
          children: [
            { path: '/login', element: <div data-testid="login-page">login</div> },
          ],
        },
      ],
      { initialEntries: ['/login'] }
    )
    render(<RouterProvider router={router} />)

    expect(screen.getByTestId('login-page')).toBeInTheDocument()
  })

  it('認証済みかつログイン画面のときダッシュボードへリダイレクトする', () => {
    useAuthStore.getState().setAccessToken('token')

    const router = createMemoryRouter(
      [
        {
          element: <PublicRoute />,
          children: [
            { path: '/login', element: <div>login</div> },
            {
              path: '/dashboard',
              element: <div data-testid="dashboard">dashboard</div>,
            },
          ],
        },
      ],
      { initialEntries: ['/login'] }
    )
    render(<RouterProvider router={router} />)

    expect(screen.getByTestId('dashboard')).toBeInTheDocument()
  })

  it('認証済みでも初回ログイン画面はリダイレクトしない', () => {
    useAuthStore.getState().setAccessToken('token')

    const router = createMemoryRouter(
      [
        {
          element: <PublicRoute />,
          children: [
            {
              path: '/first-login',
              element: <div data-testid="first-login">first</div>,
            },
          ],
        },
      ],
      { initialEntries: ['/first-login'] }
    )
    render(<RouterProvider router={router} />)

    expect(screen.getByTestId('first-login')).toBeInTheDocument()
  })

  it('state.from があるとき指定パスへリダイレクトする', () => {
    useAuthStore.getState().setAccessToken('token')

    const router = createMemoryRouter(
      [
        {
          element: <PublicRoute />,
          children: [
            { path: '/login', element: <div>login</div> },
            { path: '/custom', element: <div data-testid="custom">custom</div> },
          ],
        },
      ],
      {
        initialEntries: [
          {
            pathname: '/login',
            state: { from: { pathname: '/custom' } },
          },
        ],
      }
    )
    render(<RouterProvider router={router} />)

    expect(screen.getByTestId('custom')).toBeInTheDocument()
  })
})
