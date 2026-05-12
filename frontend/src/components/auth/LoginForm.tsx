import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useLogin } from '@/hooks/useAuth'
import { Loader2 } from 'lucide-react'

/** ログインフォーム用 Zod スキーマ（メール形式・非空パスワード）。 */
const loginSchema = z.object({
  email: z.string().email('有効なメールアドレスを入力してください'),
  password: z.string().min(1, 'パスワードを入力してください'),
})

/** ログインフォームの入力値型（Zod から推論）。 */
type LoginFormData = z.infer<typeof loginSchema>

/**
 * メールアドレス・パスワード入力と `useLogin` ミューテーションを束ねるフォーム。
 */
export function LoginForm() {
  const login = useLogin()

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
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="email">メールアドレス</Label>
        <Input
          id="email"
          type="email"
          placeholder="email@example.com"
          {...register('email')}
        />
        {errors.email && (
          <p className="text-sm text-red-500">{errors.email.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="password">パスワード</Label>
        <Input
          id="password"
          type="password"
          placeholder="パスワードを入力"
          {...register('password')}
        />
        {errors.password && (
          <p className="text-sm text-red-500">{errors.password.message}</p>
        )}
      </div>

      <Button type="submit" className="w-full" disabled={login.isPending}>
        {login.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
        ログイン
      </Button>
    </form>
  )
}
