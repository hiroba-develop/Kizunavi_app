import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Loader2 } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from '@/hooks/use-toast'

const forgotPasswordSchema = z.object({
  email: z.string().email('有効なメールアドレスを入力してください'),
})

type ForgotPasswordFormData = z.infer<typeof forgotPasswordSchema>

/**
 * パスワード再設定リンクの送信画面。
 */
export function ForgotPasswordPage() {
  const [isSubmitted, setIsSubmitted] = useState(false)
  const [submittedEmail, setSubmittedEmail] = useState('')
  const { toast } = useToast()

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ForgotPasswordFormData>({
    resolver: zodResolver(forgotPasswordSchema),
  })

  const onSubmit = async (data: ForgotPasswordFormData) => {
    setSubmittedEmail(data.email)
    setIsSubmitted(true)
    toast({
      title: '送信を受け付けました',
      description: '登録済みの場合、パスワード再設定リンクをメールで送信します。',
    })
  }

  return (
    <div className="flex flex-col items-center">
      <div className="mb-8 flex flex-col items-center text-center">
        <img
          src="/KizuNavi_logo.png"
          alt="KizuNavi"
          className="mx-auto h-auto w-full max-w-[min(100%,320px)] object-contain drop-shadow-sm sm:max-w-sm"
        />
        <p className="mt-5 text-base font-medium text-gray-700">
          パスワード再設定
        </p>
        <p className="mt-4 text-base font-medium text-gray-700">
          パスワードをお忘れの方
        </p>
        <p className="mt-1 text-sm text-gray-500">
          登録済みのメールアドレスを入力してください
        </p>
      </div>

      <div className="w-full rounded-2xl border border-sky-100 bg-white p-8 shadow-lg shadow-sky-200/60 sm:p-10">
        {isSubmitted ? (
          <div className="space-y-6 text-center">
            <div className="rounded-xl bg-sky-50 px-4 py-5 text-sm leading-6 text-gray-700">
              <p className="font-medium text-gray-900">
                リセットリンクの送信を受け付けました
              </p>
              <p className="mt-2">
                {submittedEmail} が登録済みの場合、再設定用リンクをメールで送信します。
              </p>
            </div>
            <Link
              to="/login"
              className="inline-flex text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
            >
              ログイン画面に戻る
            </Link>
          </div>
        ) : (
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="space-y-2 text-center">
              <Label
                htmlFor="email"
                className="block w-full text-center text-sm font-medium text-gray-700"
              >
                メールアドレス
              </Label>
              <Input
                id="email"
                type="email"
                placeholder="登録したメールアドレスを入力"
                autoComplete="email"
                className="h-11 rounded-lg border-sky-200/90 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400"
                {...register('email')}
              />
              {errors.email && (
                <p className="text-left text-sm text-red-500 sm:text-center">
                  {errors.email.message}
                </p>
              )}
              <p className="text-xs text-gray-500">
                パスワードリセットのリンクをメールで送信します
              </p>
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="flex h-12 w-full items-center justify-center gap-2 rounded-lg bg-sky-500 text-base font-semibold text-white shadow-sm transition-colors hover:bg-sky-600 disabled:pointer-events-none disabled:opacity-50"
            >
              {isSubmitting && (
                <Loader2 className="h-4 w-4 shrink-0 animate-spin" />
              )}
              リセットリンクを送信
            </button>

            <div className="text-center">
              <Link
                to="/login"
                className="text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
              >
                ログイン画面に戻る
              </Link>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}
