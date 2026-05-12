import { NavLink } from 'react-router-dom'
import { LayoutDashboard, User } from 'lucide-react'
import { cn } from '@/lib/utils'

/** デスクトップサイドバーに表示するナビゲーション項目（ラベル・パス・アイコン）。 */
const navigation = [
  { name: 'ダッシュボード', href: '/dashboard', icon: LayoutDashboard },
  { name: 'プロフィール', href: '/profile', icon: User },
]

/**
 * ラージ画面固定表示の左サイドナビ。`NavLink` でアクティブスタイルを切り替える。
 */
export function Sidebar() {
  return (
    <aside className="fixed left-0 top-16 z-30 hidden h-[calc(100vh-4rem)] w-64 border-r bg-white lg:block">
      <nav className="flex flex-col gap-1 p-4">
        {navigation.map((item) => (
          <NavLink
            key={item.name}
            to={item.href}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                isActive
                  ? 'bg-primary text-primary-foreground'
                  : 'text-muted-foreground hover:bg-muted hover:text-foreground'
              )
            }
          >
            <item.icon className="h-5 w-5" />
            {item.name}
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
