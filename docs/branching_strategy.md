# ブランチ戦略 運用ガイド

本ドキュメントは、本プロジェクトにおける Git ブランチ戦略および開発フローを定めるものです。
全メンバーが共通の運用ルールに従い、安全かつスムーズに開発を進めることを目的とします。

---

## 1. 概要

### 環境構成

本プロジェクトには 3 つの環境があります。

| 環境 | 用途 | デプロイ元ブランチ |
| --- | --- | --- |
| 本番環境（EC2） | エンドユーザー向け | `main` |
| 開発環境（EC2） | 動作確認用 | `develop` |
| ローカル環境（自PC） | 開発・単体テスト | 各 feature ブランチ |

### 採用戦略

**GitHub Flow + develop ブランチ** をベースとした軽量戦略を採用します。

```
feature/xxx ──► develop ──► main
   (ローカル)   (開発EC2)   (本番EC2)
```

- `main`：本番リリース済みのコードのみ。常にデプロイ可能な状態を保つ
- `develop`：次回リリース予定のコード。開発環境で動作確認するためのブランチ
- `feature/*`：個別タスクの作業ブランチ。`main` から派生させる

---

## 2. ブランチ構成

| ブランチ | 用途 | デプロイ先 | 直接 push | 派生元 |
| --- | --- | --- | --- | --- |
| `main` | 本番リリース済みコード | 本番 EC2 | **禁止**（PR経由のみ） | - |
| `develop` | 次回リリース予定 / 動作確認 | 開発 EC2 | **禁止**（PR経由のみ） | `main` |
| `feature/*` | 新機能開発 | なし（ローカル） | OK | `main` |
| `fix/*` | バグ修正 | なし（ローカル） | OK | `main` |
| `hotfix/*` | 本番緊急修正 | なし（ローカル） | OK | `main` |
| `refactor/*` | リファクタリング | なし（ローカル） | OK | `main` |
| `chore/*` | 設定変更・依存更新等 | なし（ローカル） | OK | `main` |
| `docs/*` | ドキュメント更新 | なし（ローカル） | OK | `main` |

---

## 3. メンバーの役割

### 歳納（テックリード / `yuta-toshino1931`）

#### 担当業務 / できること

- **PR のレビューと承認**
  - インターン生から出された PR を確認し、Approve または変更依頼を出す
- **`develop` へのマージ**
  - レビュー完了後、`feature/* → develop` の PR をマージ
- **`main` へのマージ（リリース判断）**
  - リリースタイミングを判断し、`develop → main` の PR を作成・マージ
- **本番デプロイの承認**
  - GitHub Actions の手動承認ステップを実行
- **hotfix の判断と実施**
  - 本番障害発生時、`hotfix/*` ブランチでの修正を判断・実施
- **ブランチ保護設定・リポジトリ設定の管理**

#### 通常の開発も担当

歳納自身も `feature/*` ブランチを切って機能開発を行います。その際は通常通り PR を作成し、可能であればインターン生にもレビューを依頼します（学習機会）。

---

### インターン生（`yuta-munakata1931`）

#### 担当業務 / できること

- **`feature/*` ブランチでの開発**
  - `main` から派生したブランチでローカル開発
- **自分のブランチへの push**
  - `git push origin feature/xxx` は自由に行える
- **PR の作成**
  - `feature/* → develop` の PR を GitHub 上で作成
- **レビュー指摘への対応**
  - 同じブランチに修正コミットを追加して push
- **開発環境での動作確認**
  - `develop` マージ後、開発 EC2 で動作確認

#### できないこと（システム的に制限）

- `main` への直接 push（GitHub のブランチ保護で弾かれる）
- `develop` への直接 push（同上）
- 自分の PR の self-approve
- `main` / `develop` ブランチの削除
- force push（`main` / `develop` に対して）

---

## 4. 標準的な開発フロー

### Step 1: 最新の `main` を取得

```bash
git checkout main
git pull origin main
```

### Step 2: feature ブランチを作成

```bash
git checkout -b feature/kizuna-score-calc
```

ブランチ名の付け方は [§5 ブランチ命名規則](#5-ブランチ命名規則) を参照。

### Step 3: 開発・コミット

```bash
git add .
git commit -m "feat: キズナ度スコア計算ロジックを追加"
```

コミットメッセージは [§6 コミットメッセージ規則](#6-コミットメッセージ規則) を参照。

### Step 4: push と PR 作成

```bash
git push origin feature/kizuna-score-calc
```

GitHub 上で **`feature/kizuna-score-calc` → `develop`** の Pull Request を作成。

- タイトル：作業内容が一目で分かるよう簡潔に
- 本文：何を・なぜ・どうやって変更したか / 動作確認内容 / 関連 Issue
- レビュアー：歳納を指定

### Step 5: レビュー対応

レビューで指摘があった場合：

```bash
# 同じブランチで修正
git add .
git commit -m "fix: レビュー指摘に対応"
git push origin feature/kizuna-score-calc
```

> ⚠ `git push --force` は使わない（共有ブランチではないが、習慣として禁止）

### Step 6: マージ（テックリードが実施）

歳納が Approve 後、GitHub 上で **Squash and merge** を実行。マージ後、feature ブランチは自動削除される。

### Step 7: 開発環境で動作確認

`develop` への自動デプロイ後、開発 EC2 で動作確認。

### Step 8: 本番リリース（テックリードが実施）

リリースタイミングで歳納が **`develop` → `main`** の PR を作成・マージ。手動承認後、本番 EC2 にデプロイ。

---

## 5. ブランチ命名規則

```
<種別>/<内容を簡潔に英語で>
```

### 種別一覧

| 種別 | 用途 | 例 |
| --- | --- | --- |
| `feature/` | 新機能追加 | `feature/login-page` |
| `fix/` | バグ修正 | `fix/header-overflow` |
| `hotfix/` | 本番緊急修正 | `hotfix/payment-error` |
| `refactor/` | リファクタリング | `refactor/user-service` |
| `chore/` | 設定・依存・雑務 | `chore/upgrade-node-20` |
| `docs/` | ドキュメント | `docs/api-reference` |

### 命名のコツ

- 英小文字とハイフンで区切る（`feature/login-page`）
- 簡潔に。長くても 30 文字以内
- 日本語・スペース・アンダースコアは避ける

---

## 6. コミットメッセージ規則

**Conventional Commits** に従います。

```
<type>: <subject>
```

### type 一覧

| type | 用途 |
| --- | --- |
| `feat` | 新機能 |
| `fix` | バグ修正 |
| `refactor` | リファクタリング（機能変更なし） |
| `chore` | ビルド・設定・依存関係など |
| `docs` | ドキュメント |
| `style` | コードスタイル（フォーマット等） |
| `test` | テストコード |
| `perf` | パフォーマンス改善 |

### 例

```
feat: ログイン画面に二要素認証を追加
fix: ヘッダーのレイアウト崩れを修正
refactor: UserService の責務を分離
chore: Spring Boot を 3.2.0 に更新
docs: README にセットアップ手順を追記
```

---

## 7. hotfix 対応フロー

本番に緊急バグが見つかった場合の手順。

```
1. main から hotfix/xxx を切る
   ↓
2. 修正・コミット
   ↓
3. hotfix/xxx → main の PR を作成・マージ（本番デプロイ）
   ↓
4. main → develop の back-merge PR を作成・マージ
```

### ⚠ 重要：back-merge を忘れない

ステップ 4 を忘れると、次回 `develop → main` のリリース時に hotfix の修正が**巻き戻ります**。
hotfix を切った時点で、back-merge タスクをセットで予定に入れてください。

---

## 8. やってはいけないこと

| ❌ NG | 理由 |
| --- | --- |
| `main` / `develop` への直接 push | 保護ルールで弾かれる。レビュー必須 |
| `main` / `develop` への force push | 履歴改ざんになり、他メンバーが同期できなくなる |
| 自分の PR を self-approve する | 第三者レビューの意義が失われる |
| 1 つの PR で複数の機能を混ぜる | レビューが困難、Revert も困難 |
| 数百〜千行を超える巨大な PR | レビュー効率が極端に下がる。300行以内を目安に |
| feature ブランチを長期間放置 | `main` との乖離が広がり、マージ地獄に。1週間以上同じブランチ作業時は `main` を取り込むこと |
| `develop` から派生して feature ブランチを切る | 未リリース機能を巻き込んでしまう。必ず `main` から派生 |
| コミットメッセージが「fix」「修正」だけ | 後から履歴を追えない。具体的に書く |

---

## 9. 困ったときは

- `main` を取り込みたい（feature ブランチが古くなった）：
  ```bash
  git checkout feature/xxx
  git fetch origin
  git merge origin/main
  # コンフリクトを解消してコミット
  ```
- 間違って `main` をローカルで変更してしまった：
  ```bash
  git checkout main
  git reset --hard origin/main
  ```
- 困ったら歳納に Slack で相談してください。git の事故は早めに相談したほうが復旧が楽です。

---

## 改訂履歴

| 日付 | 変更内容 | 作成者 |
| --- | --- | --- |
| 2026-05-15 | 初版作成 | yuta-toshino1931 |
