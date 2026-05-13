import { Link } from 'react-router-dom'
import { SignupForm } from '@/components/auth/SignupForm'

/**
 * 新規登録画面（ログイン画面と同一トンマナのブランドレイアウト）。
 */
export function SignupPage() {
  return (
    <div className="flex flex-col items-center">
      <div className="mb-8 flex flex-col items-center text-center">
        <img
          src="/KizuNavi_logo.png"
          alt="KizuNavi"
          className="mx-auto h-auto w-full max-w-[min(100%,320px)] object-contain drop-shadow-sm sm:max-w-sm"
        />
        <p className="mt-5 text-sm font-medium text-gray-600">
          エンゲージメント分析システム
        </p>
        <p className="mt-1 text-sm text-gray-500">
          社員のエンゲージメント向上を支援
        </p>
        <p className="mt-4 text-sm font-medium text-gray-700">
          新規アカウント作成
        </p>
      </div>

      <div className="w-full rounded-2xl border border-sky-100 bg-white p-8 shadow-lg shadow-sky-200/60 sm:p-10">
        <SignupForm variant="brand" />
        <p className="mt-6 text-center text-sm text-gray-500">
          既にアカウントをお持ちの方は{' '}
          <Link
            to="/login"
            className="font-medium text-sky-500 hover:text-sky-600 hover:underline"
          >
            ログイン
          </Link>
        </p>
      </div>
    </div>
  )
}
