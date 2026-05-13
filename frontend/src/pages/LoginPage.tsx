import { LoginForm } from '@/components/auth/LoginForm'

/**
 * ログイン画面（KizuNavi ブランド・カードレイアウト）。
 */
export function LoginPage() {
  return (
    <div className="flex flex-col items-center">
      {/* ロゴ・キャッチ（カードの上） */}
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
      </div>

      {/* ログインカード */}
      <div className="w-full rounded-2xl border border-sky-100 bg-white p-8 shadow-lg shadow-sky-200/60 sm:p-10">
        <LoginForm variant="brand" />
      </div>
    </div>
  )
}
