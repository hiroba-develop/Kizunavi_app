# KizuNavi - Frontend

React + TypeScript + Vite を使用したフロントエンドプロジェクトです。

## 技術スタック

- **React 18**
- **TypeScript**
- **Vite**
- **React Router v6**
- **TanStack Query (React Query)**
- **Zustand (状態管理)**
- **Tailwind CSS**
- **shadcn/ui**
- **Axios**
- **React Hook Form + Zod**

## 前提条件

- Node.js 18以上
- **npm v11.10.0 以上**（`min-release-age` 対応。後述の「サプライチェーン攻撃対策」参照）
- **バックエンド**は `dev` / `prod` いずれも **AWS RDS・SES 接続が必須**です。先にリポジトリルートで `cp .env.example .env` を行い、`DB_*` と `AWS_*` を設定してからバックエンドを起動してください（詳細は [ルート README](../README.md) および [backend/README](../backend/README.md)）。

## セットアップ

### 1. 依存関係のインストール

```bash
npm install
```

> [!NOTE]
> 本プロジェクトは `.npmrc` に `min-release-age=7` を設定しています。
> リリースから 7 日以内の新しいバージョンはインストールされません。
> 最新の依存を取り込みたい場合は、リリースから 7 日以上経過するのを待つか、
> 緊急時のみ `npm install --ignore-release-age` 等で明示的に回避してください。

### 2. 環境変数の設定

```bash
cp .env.example .env
```

`.env` ファイルを編集して、必要に応じてAPIのベースURLを設定してください。

### 3. 開発サーバーの起動

```bash
npm run dev
```

http://localhost:5173 でアプリケーションにアクセスできます。

## スクリプト

| コマンド | 説明 |
|---------|------|
| `npm run dev` | 開発サーバーを起動 |
| `npm run build` | 本番用ビルド |
| `npm run preview` | ビルド結果をプレビュー |
| `npm run lint` | ESLint でコードをチェック |
| `npm run lint:fix` | ESLint でコードを自動修正 |
| `npm run format` | Prettier でコードをフォーマット |
| `npm run generate:api` | OpenAPI Generator でAPIクライアントを生成 |
| `npm run test` | Vitest で単体テストを実行 |
| `npm run test:watch` | Vitest をウォッチモードで実行 |
| `npm run test:coverage` | カバレッジレポート（C0）を生成 |

テスト方針と学習資料:

- [ADR-0024 単体テスト戦略](../docs/adr/0024-testing-strategy.md)
- [テスト入門ガイド](../docs/guides/testing-guide.md)

## APIクライアントの生成

バックエンドのOpenAPI仕様からTypeScript Axiosクライアントを生成します：

```bash
# バックエンドを起動してOpenAPI仕様を取得可能な状態にする
cd ../backend
./gradlew bootRun

# 別のターミナルでクライアントを生成
cd ../frontend
npm run generate:api
```

生成されたクライアントは `src/api/generated/` に配置されます。

## プロジェクト構成

```
src/
├── api/                # API クライアント
│   └── generated/      # OpenAPI Generator で生成されたコード
├── components/         # React コンポーネント
│   ├── auth/           # 認証関連コンポーネント
│   ├── layout/         # レイアウトコンポーネント
│   └── ui/             # shadcn/ui コンポーネント
├── hooks/              # カスタムフック
├── lib/                # ユーティリティ
├── pages/              # ページコンポーネント
├── routes/             # ルーティング設定
├── store/              # Zustand ストア
├── styles/             # グローバルスタイル
└── main.tsx            # エントリーポイント
```

## 主要なページ

| パス | 説明 | 認証 |
|------|------|------|
| `/login` | ログインページ | 不要 |
| `/signup` | 新規登録ページ | 不要 |
| `/dashboard` | ダッシュボード | 必要 |
| `/profile` | プロフィール | 必要 |

## shadcn/ui コンポーネントの追加

```bash
npx shadcn-ui@latest add [component-name]
```

## Docker で起動

単体で起動する場合:

```bash
docker build -t kizunavi-frontend .
docker run -p 5173:5173 kizunavi-frontend
```

バックエンドと一緒に起動する場合はプロジェクトルートで:

```bash
cd ..
docker compose up --build
```

- フロントエンド: http://localhost:5173（`docker-compose.yml` の `FRONTEND_PORT`、既定は 5173）
- バックエンド: http://localhost:8080（`BACKEND_PORT`、既定は 8080）

Docker環境では `vite preview` の proxy（`/api`）でバックエンドコンテナへ中継します。
ALB 配下では `/api/*` を ALB の L7 ルールでバックエンドターゲットへ直接ルーティングします。

## ビルド

```bash
npm run build
```

ビルド成果物は `dist/` に生成されます。

## サプライチェーン攻撃対策

2026年3月の `axios` サプライチェーン攻撃（侵害版 `axios@1.14.1` / `axios@0.30.4` が
postinstall スクリプトでマルウェアを実行）のような攻撃を抑止するため、本プロジェクトでは
以下の 2 つの防御策を組み合わせています。

### 1. クールダウン期間（`min-release-age`）

`frontend/.npmrc` に以下を設定しています。

```ini
min-release-age=7
```

- リリース直後（7 日未満）のパッケージはインストールされません。
- 侵害版は通常 2〜3 時間〜数日で削除されるため、7 日待つことで安全なバージョンのみが
  取り込まれます。
- この機能は **npm v11.10.0（2026年2月リリース）以降**で利用可能です。

### 2. lockfile による厳密なインストール

- `package-lock.json` を Git 管理下に置いています。
- Docker ビルド（本番用）では `npm ci` を使用し、lockfile に記載されたバージョンのみを
  インストールします（`frontend/Dockerfile`）。
- これにより「ローカルで検証済みのバージョンのみが本番環境にデプロイ」されます。

### 運用フロー

1. 依存を追加／更新したら、ローカルで `npm install` を実行して動作確認する。
2. 更新された `package-lock.json` を Git にコミットする。
3. CI / Docker ビルドでは `npm ci` がそのまま lockfile を適用するため、
   タイミング依存の侵害版混入を防げます。

参考: [クールダウンで防ぐサプライチェーン攻撃 (Zenn)](https://zenn.dev/nenene01/articles/axios-attack-prevention)

## ライセンス

MIT License
