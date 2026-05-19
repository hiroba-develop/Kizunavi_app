import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Loader2 } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useFirstLogin } from '@/hooks/useAuth'

const firstLoginSchema = z
  .object({
    email: z.string().email('有効なメールアドレスを入力してください'),
    tempPassword: z.string().min(1, '仮パスワードを入力してください'),
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

type FirstLoginFormData = z.infer<typeof firstLoginSchema>

const inputClassName =
  'h-11 rounded-lg border-sky-200/90 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400'

/** 仮パスワードから本パスワードへ変更する初回ログイン画面。 */
export function FirstLoginPage() {
  const [isCompleted, setIsCompleted] = useState(false)
  const firstLogin = useFirstLogin()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FirstLoginFormData>({
    resolver: zodResolver(firstLoginSchema),
  })

  const onSubmit = (data: FirstLoginFormData) => {
    firstLogin.mutate(
      {
        email: data.email,
        tempPassword: data.tempPassword,
        newPassword: data.newPassword,
      },
      { onSuccess: () => setIsCompleted(true) }
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
        <p className="mt-5 text-base font-medium text-gray-700">初回パスワード設定</p>
        <p className="mt-1 text-sm text-gray-500">
          従業員登録時に通知された仮パスワードの方のみご利用ください。
        </p>
        <p className="mt-1 text-sm text-gray-500">
          すでにパスワードを設定済みの場合は
          <Link to="/forgot-password" className="text-sky-500 hover:underline">
            パスワード再設定
          </Link>
          をご利用ください。
        </p>
      </div>

      <div className="w-full rounded-2xl border border-sky-100 bg-white p-8 shadow-lg shadow-sky-200/60 sm:p-10">
        {isCompleted ? (
          <div className="space-y-6 text-center">
            <div className="rounded-xl bg-sky-50 px-4 py-5 text-sm leading-6 text-gray-700">
              <p className="font-medium text-gray-900">パスワードを設定しました</p>
              <p className="mt-2">新しいパスワードでログインしてください。</p>
            </div>
            <Link
              to="/login"
              className="inline-flex text-sm font-medium text-sky-500 hover:text-sky-600 hover:underline"
            >
              ログイン画面へ
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
                placeholder="登録したメールアドレス"
                autoComplete="email"
                className={inputClassName}
                {...register('email')}
              />
              {errors.email && (
                <p className="text-left text-sm text-red-500 sm:text-center">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div className="space-y-2 text-center">
              <Label
                htmlFor="tempPassword"
                className="block w-full text-center text-sm font-medium text-gray-700"
              >
                仮パスワード
              </Label>
              <Input
                id="tempPassword"
                type="password"
                placeholder="通知された仮パスワード"
                autoComplete="current-password"
                className={inputClassName}
                {...register('tempPassword')}
              />
              {errors.tempPassword && (
                <p className="text-left text-sm text-red-500 sm:text-center">
                  {errors.tempPassword.message}
                </p>
              )}
            </div>

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
              disabled={firstLogin.isPending}
              className="flex h-12 w-full items-center justify-center gap-2 rounded-lg bg-sky-500 text-base font-semibold text-white shadow-sm transition-colors hover:bg-sky-600 disabled:pointer-events-none disabled:opacity-50"
            >
              {firstLogin.isPending && (
                <Loader2 className="h-4 w-4 shrink-0 animate-spin" />
              )}
              パスワードを設定
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