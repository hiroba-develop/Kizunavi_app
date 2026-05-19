import { useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Loader2 } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useVerifyResetToken, useResetPassword } from '@/hooks/useAuth'

const resetPasswordSchema = z
  .object({
    newPassword: z
      .string()
      .min(8, 'パスワードは8文字以上で入力してください')
      .max(100, 'パスワードは100文字以下で入力してください'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: 'パスワードが一致しません',
    path: ['confirmPassword'],
  })

type ResetPasswordFormData = z.infer<typeof resetPasswordSchema>

const inputClassName =
  'h-11 rounded-lg border-sky-200/90 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400'

/**
 * メールのリセットリンクから遷移するパスワード再設定画面。
 */
export function ResetPasswordPage() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token')
  const [isCompleted, setIsCompleted] = useState(false)

  const {
    data: verifyResult,
    isLoading: isVerifying,
    isError: isVerifyError,
  } = useVerifyResetToken(token)

  const resetPassword = useResetPassword()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResetPasswordFormData>({
    resolver: zodResolver(resetPasswordSchema),
  })

  const onSubmit = (data: ResetPasswordFormData) => {
    if (!token) return

    resetPassword.mutate(
      { token, newPassword: data.newPassword },
      {
        onSuccess: () => {
          setIsCompleted(true)
        },
      }
    )
  }

  const renderContent = () => {
    if (!token) {
      return (
        <div className="space-y-6 text-center">
          <div className="rounded-xl bg-red-50 px-4 py-5 text-sm leading-6 text-gray-700">
            <p className="font-medium text-red-700">無効なリンクです</p>
            <p className="mt-2">
              パスワード再設定用のリンクが正しくありません。再度リセットリンクの送信をお試しください。
            </p>
          </div>
          <Link
            to="/forgot-password"
            className="inline-flex text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
          >
            パスワード再設定リンクを再送信
          </Link>
        </div>
      )
    }

    if (isVerifying) {
      return (
        <div className="flex flex-col items-center gap-3 py-8 text-sm text-gray-600">
          <Loader2 className="h-6 w-6 animate-spin text-sky-500" />
          <p>リンクを確認しています…</p>
        </div>
      )
    }

    if (isVerifyError || verifyResult?.valid === false) {
      const message =
        verifyResult?.message ||
        'このリンクは無効、期限切れ、または使用済みです。'

      return (
        <div className="space-y-6 text-center">
          <div className="rounded-xl bg-red-50 px-4 py-5 text-sm leading-6 text-gray-700">
            <p className="font-medium text-red-700">リンクを利用できません</p>
            <p className="mt-2">{message}</p>
          </div>
          <Link
            to="/forgot-password"
            className="inline-flex text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
          >
            パスワード再設定リンクを再送信
          </Link>
        </div>
      )
    }

    if (isCompleted) {
      return (
        <div className="space-y-6 text-center">
          <div className="rounded-xl bg-sky-50 px-4 py-5 text-sm leading-6 text-gray-700">
            <p className="font-medium text-gray-900">
              パスワードを再設定しました
            </p>
            <p className="mt-2">
              新しいパスワードでログインしてください。
            </p>
          </div>
          <Link
            to="/login"
            className="inline-flex text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
          >
            ログイン画面へ
          </Link>
        </div>
      )
    }

    return (
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
        <div className="space-y-2 text-center">
          <Label
            htmlFor="newPassword"
            className="block w-full text-center text-sm font-medium text-gray-700"
          >
            新しいパスワード
          </Label>
          <Input
            id="newPassword"
            type="password"
            placeholder="8文字以上のパスワード"
            autoComplete="new-password"
            className={inputClassName}
            {...register('newPassword')}
          />
          {errors.newPassword && (
            <p className="text-left text-sm text-red-500 sm:text-center">
              {errors.newPassword.message}
            </p>
          )}
        </div>

        <div className="space-y-2 text-center">
          <Label
            htmlFor="confirmPassword"
            className="block w-full text-center text-sm font-medium text-gray-700"
          >
            新しいパスワード（確認）
          </Label>
          <Input
            id="confirmPassword"
            type="password"
            placeholder="パスワードを再入力"
            autoComplete="new-password"
            className={inputClassName}
            {...register('confirmPassword')}
          />
          {errors.confirmPassword && (
            <p className="text-left text-sm text-red-500 sm:text-center">
              {errors.confirmPassword.message}
            </p>
          )}
        </div>

        <button
          type="submit"
          disabled={resetPassword.isPending}
          className="flex h-12 w-full items-center justify-center gap-2 rounded-lg bg-sky-500 text-base font-semibold text-white shadow-sm transition-colors hover:bg-sky-600 disabled:pointer-events-none disabled:opacity-50"
        >
          {resetPassword.isPending && (
            <Loader2 className="h-4 w-4 shrink-0 animate-spin" />
          )}
          パスワードを再設定
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
    )
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
        <p className="mt-1 text-sm text-gray-500">
          新しいパスワードを入力してください
        </p>
      </div>

      <div className="w-full rounded-2xl border border-sky-100 bg-white p-8 shadow-lg shadow-sky-200/60 sm:p-10">
        {renderContent()}
      </div>
    </div>
  )
}
