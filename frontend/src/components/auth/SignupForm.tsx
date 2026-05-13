import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useSignup } from '@/hooks/useAuth'
import { Loader2 } from 'lucide-react'
import { cn } from '@/lib/utils'

/** 新規登録フォーム用 Zod スキーマ（パスワード確認の一致チェック付き）。 */
const signupSchema = z
  .object({
    name: z.string().min(1, '名前を入力してください').max(100, '名前は100文字以下で入力してください'),
    email: z.string().email('有効なメールアドレスを入力してください'),
    password: z
      .string()
      .min(8, 'パスワードは8文字以上で入力してください')
      .max(100, 'パスワードは100文字以下で入力してください'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'パスワードが一致しません',
    path: ['confirmPassword'],
  })

/** サインアップフォームの入力値型。 */
type SignupFormData = z.infer<typeof signupSchema>

type SignupFormProps = {
  /** `brand`: ログイン画面と揃えた KizuNavi 用スタイル */
  variant?: 'default' | 'brand'
}

const inputBrandClass =
  'h-11 rounded-lg border-sky-200/90 bg-sky-50 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400'

const labelBrandClass =
  'block w-full text-center text-sm font-medium text-gray-700'

/**
 * 名前・メール・パスワード入力と `useSignup` を束ねる登録フォーム。
 */
export function SignupForm({ variant = 'default' }: SignupFormProps) {
  const signup = useSignup()
  const isBrand = variant === 'brand'

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignupFormData>({
    resolver: zodResolver(signupSchema),
  })

  /**
   * 検証成功時にサインアップ API へ必要フィールドのみ送信する。
   *
   * @param data 検証済みフォーム値
   */
  const onSubmit = (data: SignupFormData) => {
    signup.mutate({
      name: data.name,
      email: data.email,
      password: data.password,
    })
  }

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className={cn('space-y-4', isBrand && 'space-y-6')}
    >
      <div className={cn('space-y-2', isBrand && 'text-center')}>
        <Label htmlFor="name" className={cn(isBrand && labelBrandClass)}>
          名前
        </Label>
        <Input
          id="name"
          type="text"
          placeholder="山田 太郎"
          autoComplete="name"
          className={cn(isBrand && inputBrandClass)}
          {...register('name')}
        />
        {errors.name && (
          <p
            className={cn(
              'text-sm text-red-500',
              isBrand && 'text-left sm:text-center'
            )}
          >
            {errors.name.message}
          </p>
        )}
      </div>

      <div className={cn('space-y-2', isBrand && 'text-center')}>
        <Label htmlFor="email" className={cn(isBrand && labelBrandClass)}>
          メールアドレス
        </Label>
        <Input
          id="email"
          type="email"
          placeholder={
            isBrand ? 'kizunavi@hiroba1931.co.jp' : 'email@example.com'
          }
          autoComplete="email"
          className={cn(isBrand && inputBrandClass)}
          {...register('email')}
        />
        {errors.email && (
          <p
            className={cn(
              'text-sm text-red-500',
              isBrand && 'text-left sm:text-center'
            )}
          >
            {errors.email.message}
          </p>
        )}
      </div>

      <div className={cn('space-y-2', isBrand && 'text-center')}>
        <Label htmlFor="password" className={cn(isBrand && labelBrandClass)}>
          パスワード
        </Label>
        <Input
          id="password"
          type="password"
          placeholder="8文字以上のパスワード"
          autoComplete="new-password"
          className={cn(isBrand && inputBrandClass)}
          {...register('password')}
        />
        {errors.password && (
          <p
            className={cn(
              'text-sm text-red-500',
              isBrand && 'text-left sm:text-center'
            )}
          >
            {errors.password.message}
          </p>
        )}
      </div>

      <div className={cn('space-y-2', isBrand && 'text-center')}>
        <Label
          htmlFor="confirmPassword"
          className={cn(isBrand && labelBrandClass)}
        >
          パスワード確認
        </Label>
        <Input
          id="confirmPassword"
          type="password"
          placeholder="パスワードを再入力"
          autoComplete="new-password"
          className={cn(isBrand && inputBrandClass)}
          {...register('confirmPassword')}
        />
        {errors.confirmPassword && (
          <p
            className={cn(
              'text-sm text-red-500',
              isBrand && 'text-left sm:text-center'
            )}
          >
            {errors.confirmPassword.message}
          </p>
        )}
      </div>

      {isBrand ? (
        <button
          type="submit"
          disabled={signup.isPending}
          className="flex h-12 w-full items-center justify-center gap-2 rounded-lg bg-sky-500 text-base font-semibold text-white shadow-sm transition-colors hover:bg-sky-600 disabled:pointer-events-none disabled:opacity-50"
        >
          {signup.isPending && (
            <Loader2 className="h-4 w-4 shrink-0 animate-spin" />
          )}
          アカウント作成
        </button>
      ) : (
        <Button type="submit" className="w-full" disabled={signup.isPending}>
          {signup.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
          アカウント作成
        </Button>
      )}
    </form>
  )
}
