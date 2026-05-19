import { Navigate, createBrowserRouter } from 'react-router-dom'
import Layout from '@/components/Layout'
import { AuthLayout } from '@/components/layout/AuthLayout'
import { ProtectedRoute } from '@/routes/ProtectedRoute'
import { PublicRoute } from '@/routes/PublicRoute'
import { LoginPage } from '@/pages/LoginPage'
import { ForgotPasswordPage } from '@/pages/ForgotPasswordPage'
import { ResetPasswordPage } from '@/pages/ResetPasswordPage'
import { FirstLoginPage } from '@/pages/FirstLoginPage'
import Dashboard from '@/pages/Dashboard'
import Employees from '@/pages/Employees'
import Surveys from '@/pages/Surveys'
import SurveyAnswer from '@/pages/SurveyAnswer'


/**
 * アプリ全体のブラウザルーター定義。
 *
 * - 未認証向け: `PublicRoute` 配下にログイン・サインアップ（`AuthLayout`）
 * - 認証必須: `ProtectedRoute` 配下に業務画面（`Layout`）
 */
export const router = createBrowserRouter([
  {
    element: <PublicRoute />,
    children: [
      {
        element: <AuthLayout />,
        children: [
          {
            path: '/login',
            element: <LoginPage />,
          },
          {
            path: '/forgot-password',
            element: <ForgotPasswordPage />,
          },
          {
            path: '/reset-password',
            element: <ResetPasswordPage />,
          },
          {
            path: '/first-login',
            element: <FirstLoginPage />,
          },
        ],
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            index: true,
            element: <Navigate to="/dashboard" replace />,
          },
          {
            path: '/dashboard',
            element: <Dashboard />,
          },
          {
            path: '/employees',
            element: <Employees />,
          },
          {
            path: '/surveys',
            element: <Surveys />,
          },
          {
            path: '/survey/:surveyId',
            element: <SurveyAnswer />,
          },
        ],
      },
    ],
  },
])
