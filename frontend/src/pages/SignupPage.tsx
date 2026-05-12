import { Link } from 'react-router-dom'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { SignupForm } from '@/components/auth/SignupForm'

/**
 * 新規登録画面。サインアップフォームとログイン画面へのリンクを表示する。
 */
export function SignupPage() {
  return (
    <>
      <div className="text-center">
        <h1 className="text-3xl font-bold text-primary">Product Template</h1>
        <p className="mt-2 text-gray-600">新規アカウント作成</p>
      </div>

      <Card className="mt-8">
        <CardHeader>
          <CardTitle>新規登録</CardTitle>
          <CardDescription>
            必要な情報を入力してアカウントを作成してください
          </CardDescription>
        </CardHeader>
        <CardContent>
          <SignupForm />
        </CardContent>
        <CardFooter className="flex justify-center">
          <p className="text-sm text-gray-600">
            既にアカウントをお持ちの方は{' '}
            <Link to="/login" className="text-primary hover:underline">
              ログイン
            </Link>
          </p>
        </CardFooter>
      </Card>
    </>
  )
}
