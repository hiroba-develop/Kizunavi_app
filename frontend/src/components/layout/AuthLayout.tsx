import { Outlet } from 'react-router-dom'

/**
 * ログイン／登録など未認証向け画面の共通レイアウト（中央寄せ・最大幅制限）。
 */
export function AuthLayout() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-b from-sky-50 via-[#f0f7ff] to-[#eef6ff] px-4 py-12 sm:px-6 lg:px-8">
      <div className="w-full max-w-md">
        <Outlet />
      </div>
    </div>
  )
}
