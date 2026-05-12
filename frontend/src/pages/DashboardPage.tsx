import { useAuthStore } from '@/store/useAuthStore'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Activity, Users, CreditCard, DollarSign } from 'lucide-react'

/**
 * 認証後のホーム／ダッシュボード。ストアのユーザー名表示とテンプレ用 KPI カードを並べる。
 */
export function DashboardPage() {
  const { user } = useAuthStore()

  /** 画面に表示するダミー KPI（タイトル・値・変化率・アイコン）。 */
  const stats = [
    {
      title: '売上',
      value: '¥4,523,100',
      change: '+20.1%',
      icon: DollarSign,
    },
    {
      title: 'ユーザー数',
      value: '2,350',
      change: '+180.1%',
      icon: Users,
    },
    {
      title: '取引数',
      value: '12,234',
      change: '+19%',
      icon: CreditCard,
    },
    {
      title: 'アクティブ率',
      value: '87.3%',
      change: '+4.3%',
      icon: Activity,
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">ダッシュボード</h1>
        <p className="text-muted-foreground mt-2">
          こんにちは、{user?.name ?? 'ゲスト'} さん
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
              <stat.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">
                <span className="text-green-600">{stat.change}</span> 先月比
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>最近のアクティビティ</CardTitle>
            <CardDescription>
              最新のユーザーアクティビティを表示します
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="flex items-center gap-4">
                  <div className="h-2 w-2 rounded-full bg-green-500" />
                  <div className="flex-1 space-y-1">
                    <p className="text-sm font-medium leading-none">
                      ユーザー {i} がログインしました
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {i * 5} 分前
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>お知らせ</CardTitle>
            <CardDescription>システムからのお知らせ</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="rounded-lg border p-4">
                <h4 className="font-medium">システムメンテナンスのお知らせ</h4>
                <p className="text-sm text-muted-foreground mt-1">
                  定期メンテナンスを実施予定です。
                </p>
              </div>
              <div className="rounded-lg border p-4">
                <h4 className="font-medium">新機能リリース</h4>
                <p className="text-sm text-muted-foreground mt-1">
                  新しいダッシュボード機能が追加されました。
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
