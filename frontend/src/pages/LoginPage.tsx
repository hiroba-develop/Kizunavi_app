import { Link } from 'react-router-dom'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { LoginForm } from '@/components/auth/LoginForm'

/**
 * ログイン画面。見出し・説明・ログインフォーム・サインアップへの導線を表示する。
 */
export function LoginPage() {
  return (
    <>
      <div className="text-center">
        <h1 className="text-3xl font-bold text-primary">Product Template</h1>
        <p className="mt-2 text-gray-600">アカウントにログイン</p>
      </div>

      <Card className="mt-8">
        <CardHeader>
          <CardTitle>ログイン</CardTitle>
          <CardDescription>
            メールアドレスとパスワードを入力してください
          </CardDescription>
        </CardHeader>
        <CardContent>
          <LoginForm />
        </CardContent>
        <CardFooter className="flex justify-center">
          <p className="text-sm text-gray-600">
            アカウントをお持ちでない方は{' '}
            <Link to="/signup" className="text-primary hover:underline">
              新規登録
            </Link>
          </p>
        </CardFooter>
      </Card>
    </>
  )
}
