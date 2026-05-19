import { useState } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { useAuthStore } from '@/store/useAuthStore'
import { useLogout } from '@/hooks/useAuth'

// サイドバーのナビゲーションアイテム
const navigationItems = [
  { name: "ダッシュボード", path: "/dashboard" },
  { name: "従業員登録", path: "/employees" },
  { name: "サーベイ実施", path: "/surveys" },
];

/**
 * 認証後の共通シェル。子ルートは `Outlet` で描画する。
 */
const Layout = () => {
  const { user } = useAuthStore();
  const location = useLocation();
  const logoutMutation = useLogout();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  /** サーバー側のリフレッシュ失効と Cookie 削除を含むログアウト。 */
  const handleLogout = () => {
    logoutMutation.mutate();
  };

  return (
    <div className="flex min-h-screen flex-col bg-sky-50">
      {/* ヘッダー */}
      <header className="shrink-0 border-b border-sky-200/80 bg-sky-100/70 shadow-sm">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex">
              <div className="flex-shrink-0 flex items-center">
                <img src="/KizuNavi_logo.png" alt="KizuNavi" className="h-12 w-auto mt-2" />
              </div>
            </div>
            <div className="flex items-center">
              <div className="hidden md:ml-4 md:flex md:items-center">
                <div className="ml-3 relative">
                  <div className="flex items-center">
                    <span className="mr-3 text-sm font-medium text-gray-700">
                      {user?.name}
                    </span>
                    <button
                      onClick={handleLogout}
                      className="px-3 py-1 text-sm text-white bg-sky-500 rounded hover:bg-sky-600"
                    >
                      ログアウト
                    </button>
                  </div>
                </div>
              </div>
              <div className="ml-3 md:hidden">
                <button
                  onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                  className="inline-flex items-center justify-center p-2 rounded-md text-sky-600 hover:text-sky-700 hover:bg-sky-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-sky-400"
                >
                  <span className="sr-only">メニューを開く</span>
                  {/* ハンバーガーメニューアイコン */}
                  <svg
                    className="block h-6 w-6"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    aria-hidden="true"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M4 6h16M4 12h16M4 18h16"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* モバイルメニュー */}
        {isMobileMenuOpen && (
          <div className="md:hidden">
            <div className="pt-2 pb-3 space-y-1">
              {navigationItems.map((item) => (
                <Link
                  key={item.name}
                  to={item.path}
                  className={`block pl-3 pr-4 py-2 border-l-4 text-base font-medium ${
                    location.pathname === item.path
                      ? "border-sky-500 text-sky-700 bg-sky-50"
                      : "border-transparent text-gray-600 hover:bg-sky-50/60 hover:border-sky-200 hover:text-gray-800"
                  }`}
                >
                  {item.name}
                </Link>
              ))}
            </div>
            <div className="pt-4 pb-3 border-t border-sky-200">
              <div className="flex items-center px-4">
                <div className="flex-shrink-0">
                  <div className="h-10 w-10 rounded-full bg-sky-200 flex items-center justify-center">
                    <span className="text-lg font-medium text-sky-800">
                      {user?.name?.charAt(0) ?? "?"}
                    </span>
                  </div>
                </div>
                <div className="ml-3">
                  <div className="text-base font-medium text-gray-800">
                    {user?.name}
                  </div>
                  <div className="text-sm font-medium text-gray-500">
                    {user?.email}
                  </div>
                </div>
              </div>
              <div className="mt-3 space-y-1">
                <button
                  onClick={handleLogout}
                  className="block w-full text-left px-4 py-2 text-base font-medium text-gray-600 hover:text-gray-900 hover:bg-sky-50"
                >
                  ログアウト
                </button>
              </div>
            </div>
          </div>
        )}
      </header>

      {/* ヘッダー直下〜画面下端まで占有（白いシェルが常にビューポート下端まで伸びる） */}
      <div className="flex min-h-0 flex-1">
        {/* サイドバー（デスクトップ） */}
        <div className="hidden min-h-0 md:flex md:shrink-0">
          <div className="flex h-full min-h-0 w-64 flex-col border-r border-sky-200 bg-white">
            <div className="flex min-h-0 flex-1 flex-col overflow-y-auto pt-5 pb-4">
              <nav className="mt-5 flex-1 space-y-1 bg-white px-2">
                {navigationItems.map((item) => (
                  <Link
                    key={item.name}
                    to={item.path}
                    className={`group flex items-center px-2 py-2 text-sm font-medium rounded-md ${
                      location.pathname === item.path
                        ? "bg-sky-100 text-sky-700"
                        : "text-gray-600 hover:bg-sky-50 hover:text-sky-700"
                    }`}
                  >
                    {item.name}
                  </Link>
                ))}
              </nav>
            </div>
          </div>
        </div>

        {/* メインコンテンツ */}
        <main className="flex min-h-0 flex-1 flex-col overflow-y-auto bg-sky-50/50 focus:outline-none">
          <div className="flex min-h-0 flex-1 flex-col py-6">
            <div className="mx-auto flex min-h-0 w-full max-w-7xl flex-1 flex-col px-4 sm:px-6 md:px-8">
              <Outlet />
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};

export default Layout;
