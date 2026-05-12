import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useAuthStore } from '@/store/useAuthStore'
import { useUpdateUser } from '@/hooks/useUser'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Separator } from '@/components/ui/separator'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Loader2 } from 'lucide-react'

/** 表示名更新フォーム用 Zod スキーマ。 */
const profileSchema = z.object({
  name: z.string().min(1, '名前を入力してください').max(100, '名前は100文字以下で入力してください'),
})

/** パスワード変更フォーム用 Zod スキーマ（確認入力の一致チェック付き）。 */
const passwordSchema = z
  .object({
    currentPassword: z.string().min(1, '現在のパスワードを入力してください'),
    newPassword: z
      .string()
      .min(8, '新しいパスワードは8文字以上で入力してください')
      .max(100, 'パスワードは100文字以下で入力してください'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: 'パスワードが一致しません',
    path: ['confirmPassword'],
  })

/** プロフィール名フォームの入力型。 */
type ProfileFormData = z.infer<typeof profileSchema>
/** パスワード変更フォームの入力型。 */
type PasswordFormData = z.infer<typeof passwordSchema>

/**
 * ログインユーザーのプロフィール編集ページ。
 *
 * 表示名の更新とパスワード変更の 2 フォームを提供し、`useUpdateUser` で API に反映する。
 */
export function ProfilePage() {
  const { user } = useAuthStore()
  const updateUser = useUpdateUser()

  const profileForm = useForm<ProfileFormData>({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      name: user?.name || '',
    },
  })

  const passwordForm = useForm<PasswordFormData>({
    resolver: zodResolver(passwordSchema),
  })

  /**
   * 名前のみを `PUT /api/users/me` に送信する。
   *
   * @param data 検証済みプロフィールフォーム値
   */
  const onProfileSubmit = (data: ProfileFormData) => {
    updateUser.mutate({ name: data.name })
  }

  /**
   * 現在パスワード確認付きで新パスワードを送信し、フォームをリセットする。
   *
   * @param data 検証済みパスワードフォーム値
   */
  const onPasswordSubmit = (data: PasswordFormData) => {
    updateUser.mutate({
      currentPassword: data.currentPassword,
      password: data.newPassword,
    })
    passwordForm.reset()
  }

  /**
   * アバター表示用に名前から最大 2 文字のイニシャルを生成する。
   *
   * @param name ユーザーの表示名（空白区切りの各部分の先頭文字を使用）
   * @returns 大文字のイニシャル文字列（最大 2 文字）
   */
  const getInitials = (name: string) => {
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2)
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">プロフィール</h1>
        <p className="text-muted-foreground mt-2">
          アカウント情報の確認と更新ができます
        </p>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>基本情報</CardTitle>
            <CardDescription>プロフィール情報を更新します</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex items-center gap-4 mb-6">
              <Avatar className="h-20 w-20">
                <AvatarFallback className="text-xl">
                  {user?.name ? getInitials(user.name) : 'U'}
                </AvatarFallback>
              </Avatar>
              <div>
                <p className="font-medium">{user?.name}</p>
                <p className="text-sm text-muted-foreground">{user?.email}</p>
                <p className="text-xs text-muted-foreground mt-1">
                  {user?.role === 'ROLE_ADMIN' ? '管理者' : 'ユーザー'}
                </p>
              </div>
            </div>

            <Separator className="my-4" />

            <form onSubmit={profileForm.handleSubmit(onProfileSubmit)} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="name">名前</Label>
                <Input
                  id="name"
                  {...profileForm.register('name')}
                />
                {profileForm.formState.errors.name && (
                  <p className="text-sm text-red-500">
                    {profileForm.formState.errors.name.message}
                  </p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="email">メールアドレス</Label>
                <Input
                  id="email"
                  type="email"
                  value={user?.email || ''}
                  disabled
                  className="bg-muted"
                />
                <p className="text-xs text-muted-foreground">
                  メールアドレスは変更できません
                </p>
              </div>

              <Button type="submit" disabled={updateUser.isPending}>
                {updateUser.isPending && (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                )}
                保存
              </Button>
            </form>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>パスワード変更</CardTitle>
            <CardDescription>
              アカウントのパスワードを変更します
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form
              onSubmit={passwordForm.handleSubmit(onPasswordSubmit)}
              className="space-y-4"
            >
              <div className="space-y-2">
                <Label htmlFor="currentPassword">現在のパスワード</Label>
                <Input
                  id="currentPassword"
                  type="password"
                  {...passwordForm.register('currentPassword')}
                />
                {passwordForm.formState.errors.currentPassword && (
                  <p className="text-sm text-red-500">
                    {passwordForm.formState.errors.currentPassword.message}
                  </p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="newPassword">新しいパスワード</Label>
                <Input
                  id="newPassword"
                  type="password"
                  {...passwordForm.register('newPassword')}
                />
                {passwordForm.formState.errors.newPassword && (
                  <p className="text-sm text-red-500">
                    {passwordForm.formState.errors.newPassword.message}
                  </p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="confirmPassword">新しいパスワード（確認）</Label>
                <Input
                  id="confirmPassword"
                  type="password"
                  {...passwordForm.register('confirmPassword')}
                />
                {passwordForm.formState.errors.confirmPassword && (
                  <p className="text-sm text-red-500">
                    {passwordForm.formState.errors.confirmPassword.message}
                  </p>
                )}
              </div>

              <Button type="submit" disabled={updateUser.isPending}>
                {updateUser.isPending && (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                )}
                パスワードを変更
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
