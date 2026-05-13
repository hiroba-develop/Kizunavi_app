import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Link } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useLogin } from '@/hooks/useAuth'
import { Loader2 } from 'lucide-react'
import { cn } from '@/lib/utils'

/** ログインフォーム用 Zod スキーマ（メール形式・非空パスワード）。 */
const loginSchema = z.object({
  email: z.string().email('有効なメールアドレスを入力してください'),
  password: z.string().min(1, 'パスワードを入力してください'),
})

/** ログインフォームの入力値型（Zod から推論）。 */
type LoginFormData = z.infer<typeof loginSchema>

type LoginFormProps = {
  /** `brand`: KizuNavi ログイン画面用のスタイル */
  variant?: 'default' | 'brand'
}

/**
 * メールアドレス・パスワード入力と `useLogin` ミューテーションを束ねるフォーム。
 */
export function LoginForm({ variant = 'default' }: LoginFormProps) {
  const login = useLogin()
  const isBrand = variant === 'brand'

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  /**
   * 検証成功時にログイン API を呼び出す。
   *
   * @param data 検証済みフォーム値
   */
  const onSubmit = (data: LoginFormData) => {
    login.mutate(data)
  }

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className={cn('space-y-4', isBrand && 'space-y-6')}
    >
      <div className={cn('space-y-2', isBrand && 'space-y-2 text-center')}>
        <Label
          htmlFor="email"
          className={cn(
            isBrand &&
              'block w-full text-center text-sm font-medium text-gray-700'
          )}
        >
          メールアドレス
        </Label>
        <Input
          id="email"
          type="email"
          placeholder={
            isBrand ? 'kizunavi@hiroba1931.co.jp' : 'email@example.com'
          }
          autoComplete="email"
          className={cn(
            isBrand &&
              'h-11 rounded-lg border-sky-200/90 bg-sky-50 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400'
          )}
          {...register('email')}
        />
        {errors.email && (
          <p className="text-left text-sm text-red-500 sm:text-center">
            {errors.email.message}
          </p>
        )}
      </div>

      <div className={cn('space-y-2', isBrand && 'space-y-2 text-center')}>
        <Label
          htmlFor="password"
          className={cn(
            isBrand &&
              'block w-full text-center text-sm font-medium text-gray-700'
          )}
        >
          パスワード
        </Label>
        <Input
          id="password"
          type="password"
          placeholder="パスワードを入力"
          autoComplete="current-password"
          className={cn(
            isBrand &&
              'h-11 rounded-lg border-sky-200/90 bg-sky-50 px-4 text-sm text-gray-900 placeholder:text-gray-400 focus-visible:border-sky-400 focus-visible:ring-sky-400'
          )}
          {...register('password')}
        />
        {errors.password && (
          <p className="text-left text-sm text-red-500 sm:text-center">
            {errors.password.message}
          </p>
        )}
      </div>

      {isBrand ? (
        <button
          type="submit"
          disabled={login.isPending}
          className="flex h-12 w-full items-center justify-center gap-2 rounded-lg bg-sky-500 text-base font-semibold text-white shadow-sm transition-colors hover:bg-sky-600 disabled:pointer-events-none disabled:opacity-50"
        >
          {login.isPending && <Loader2 className="h-4 w-4 shrink-0 animate-spin" />}
          ログイン
        </button>
      ) : (
        <Button type="submit" className="w-full" disabled={login.isPending}>
          {login.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
          ログイン
        </Button>
      )}

      {isBrand && (
        <div className="flex flex-col items-center gap-2 pt-1 text-sm font-medium">
          <Link
            to="/forgot-password"
            className="text-sky-500 hover:text-sky-600 hover:underline"
          >
            パスワードをお忘れですか？
          </Link>
          <Link
            to="/signup"
            className="text-sky-500 hover:text-sky-600 hover:underline"
          >
            初めての方
          </Link>
        </div>
      )}
    </form>
  )
}
