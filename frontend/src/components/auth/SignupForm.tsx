import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useSignup } from '@/hooks/useAuth'
import { Loader2 } from 'lucide-react'

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

/**
 * 名前・メール・パスワード入力と `useSignup` を束ねる登録フォーム。
 */
export function SignupForm() {
  const signup = useSignup()

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
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="name">名前</Label>
        <Input
          id="name"
          type="text"
          placeholder="山田 太郎"
          {...register('name')}
        />
        {errors.name && (
          <p className="text-sm text-red-500">{errors.name.message}</p>
        )}
      </div>

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
          placeholder="8文字以上のパスワード"
          {...register('password')}
        />
        {errors.password && (
          <p className="text-sm text-red-500">{errors.password.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="confirmPassword">パスワード確認</Label>
        <Input
          id="confirmPassword"
          type="password"
          placeholder="パスワードを再入力"
          {...register('confirmPassword')}
        />
        {errors.confirmPassword && (
          <p className="text-sm text-red-500">{errors.confirmPassword.message}</p>
        )}
      </div>

      <Button type="submit" className="w-full" disabled={signup.isPending}>
        {signup.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
        アカウント作成
      </Button>
    </form>
  )
}
