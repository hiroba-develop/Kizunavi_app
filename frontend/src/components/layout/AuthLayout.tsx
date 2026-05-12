import { Outlet } from 'react-router-dom'

/**
 * ログイン／登録など未認証向け画面の共通レイアウト（中央寄せ・最大幅制限）。
 */
export function AuthLayout() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <Outlet />
      </div>
    </div>
  )
}
