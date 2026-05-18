# KizuNavi - Backend

Spring Boot 3.4 を使用したREST APIバックエンドプロジェクトです。

## 技術スタック

- **Java 21**
- **Spring Boot 3.4.x**
- **Spring Security + JWT認証**
- **Spring Data JPA (Hibernate)**
- **Oracle Database（AWS RDS）**
- **HikariCP (コネクションプール)**
- **SpringDoc OpenAPI (Swagger UI)**
- **AWS SES (メール送信)**
- **Gradle (Groovy DSL)**

## Spring プロファイル

| プロファイル | 用途 | 接続先 |
|--------------|------|--------|
| `dev` | 自 PC（bootRun / Docker）・**AWS dev EC2** 共通 | dev 環境の AWS RDS Oracle・SES |
| `prod` | **AWS prod EC2** のみ | prod 環境の AWS RDS Oracle・SES |

既定は `application.yml` の **`SPRING_PROFILES_ACTIVE=dev`** です。H2 等のインメモリ DBは使用しません。

## 前提条件

- JDK 21以上
- Gradle 8.x (Gradle Wrapperを使用可能)
- **AWS Secrets Manager**（DB / JWT 用シークレット）へのアクセス権限と **AWS SES** 用の認証情報（`dev` / `prod` いずれでも必須）

## セットアップ

### 1. 環境変数の設定

プロジェクトルートの `.env` を使う場合（推奨）は [ルート README](../README.md) の手順に従い、ルートで `cp .env.example .env` を編集してください。

`backend` 直下のみで動かす場合:

```bash
cp .env.example .env
```

`.env` に `AWS_SECRETS_ENABLED` / `AWS_SECRETS_REGION` / `AWS_SECRETS_PREFIX` と `AWS_*` を設定します（`SPRING_PROFILES_ACTIVE` は通常 `dev`）。DB 接続情報と JWT 署名鍵は Secrets Manager から取得されます。

### 2. Oracle スキーマ（ログイン機能）の適用

RDS に `users` / `refresh_tokens` 等が未作成の場合、DBA が事前に作成した **`template_app`** ユーザーで [`src/main/resources/db/oracle/login_schema.sql`](src/main/resources/db/oracle/login_schema.sql) を実行する。表領域 `TEMPLATE_TABLE` / `TEMPLATE_INDEX` とユーザー `template_app` 自体の作成は DBA 作業（SQL ファイル内 `[実施済み]` コメント参照）。設計の詳細は [`docs/db/login-schema.md`](../docs/db/login-schema.md)。

### 3. 起動（dev）

環境変数を読み込んだうえで `bootRun` します。

**bash の例:**

```bash
set -a && source .env && set +a   # または source ../.env（ルートの .env を使う場合）
./gradlew bootRun
```

**Windows PowerShell（ルート `.env` を使う例）:**

```powershell
cd backend
Get-Content ..\.env | ForEach-Object {
  if ($_ -match '^\s*#' -or $_ -match '^\s*$') { return }
  if ($_ -match '^(\w+)=(.*)$') {
    [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
  }
}
.\gradlew.bat bootRun
```

### 4. 起動（prod）

EC2 上では起動スクリプトまたは `docker compose` で `SPRING_PROFILES_ACTIVE=prod` と prod 用シークレットを渡します。自 PC で prod に接続する場合のみ、同様に `.env` を prod 用にしてから:

```bash
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

### 5. ホットリロード（dev のみ）

`build.gradle` に `developmentOnly 'org.springframework.boot:spring-boot-devtools'` が入っており、`dev` プロファイルでは `application-dev.yml` の `spring.devtools.restart` が有効です。`build/classes` 配下のクラスファイル更新を検知すると、Spring Boot アプリだけが自動で再起動されます（JVM プロセスは継続するため、フル再起動より高速）。

#### `bootRun` 一本（推奨）

`bootRun` 起動時に `build.gradle` が **`gradlew -t classes`（継続ビルド）をバックグラウンドの子プロセス**として同時起動します。別ターミナルや IDE の自動ビルドは不要です。

```powershell
cd backend
.\gradlew.bat bootRun
```

**bash の例:**

```bash
cd backend
./gradlew bootRun
```

起動ログに `[hot-reload] gradlew -t classes をバックグラウンドで起動します` が出たあと、`.java` や `src/main/resources` を保存すると再コンパイルされ、数秒〜十数秒程度で DevTools による再起動が走ります（環境・変更量による）。

**動作確認の目安**

1. 上記で `bootRun` を起動し、ホットリロード用ログが出ることを確認する。
2. 任意の `.java`（例: `src/main/java/.../entity/LoginAttempt.java`）を編集して保存する。
3. ターミナルで `compileJava` の再実行に続き、DevTools の再起動ログ（`Restarting` など）が出ることを確認する。
4. `Ctrl+C` で停止する（子プロセスの継続ビルドもシャットダウンで終了する）。

#### ホットリロードを一時的に無効化したい場合

```bash
DEVTOOLS_RESTART_ENABLED=false ./gradlew bootRun
```

（DevTools によるアプリの自動再起動のみオフになる。）

#### 注意点

- **Gradle Daemon が 2 つ**立つ（`bootRun` 用と `-t classes` 用）。停止後にデーモンをまとめて止めたい場合は `.\gradlew.bat --stop`（bash では `./gradlew --stop`）。
- 本番ビルド（`./gradlew build` の `bootJar`）では `developmentOnly` が含まれないため、DevTools クラス自体が同梱されません。`prod` プロファイルにも DevTools 設定は入れていません。
- `application*.yml` 等のリソースを編集した場合も再起動対象です。再起動を避けたい変更がある場合は `spring.devtools.restart.additional-exclude` の追加を検討してください。
- IDE で更に反応を速めたい場合のみ、**IntelliJ IDEA** の **Build project automatically** などの自動ビルドを併用してもよい（必須ではない）。

## API ドキュメント

アプリケーション起動後、以下のURLでAPIドキュメントにアクセスできます：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 主要なエンドポイント

### 認証 API

| メソッド | パス | 説明 |
|---------|------|------|
| POST | `/api/auth/signup` | 新規ユーザー登録 |
| POST | `/api/auth/login` | ログイン |
| POST | `/api/auth/refresh` | トークンリフレッシュ |
| POST | `/api/auth/logout` | ログアウト |

### ユーザー API

| メソッド | パス | 説明 |
|---------|------|------|
| GET | `/api/users/me` | 現在のユーザー情報取得 |
| PUT | `/api/users/me` | 現在のユーザー情報更新 |
| GET | `/api/users` | ユーザー一覧取得 (管理者のみ) |
| GET | `/api/users/{id}` | ユーザー情報取得 (管理者のみ) |
| DELETE | `/api/users/{id}` | ユーザー削除 (管理者のみ) |

## プロジェクト構成

```
src/main/java/com/kizunavi/
├── KizuNaviApplication.java   # メインクラス
├── config/                           # 設定クラス
├── controller/                       # REST コントローラー
├── dto/                              # データ転送オブジェクト
├── entity/                           # JPA エンティティ
├── exception/                        # カスタム例外
├── repository/                       # Spring Data リポジトリ
├── security/                         # セキュリティ関連
└── service/                          # ビジネスロジック
```

## Docker で起動

単体で起動する場合（**ルートの `.env` をマウントまたは `--env-file`** で RDS / SES を渡すこと）:

```bash
docker build -t kizuNavi-backend .
docker run -p 8080:8080 --env-file ../.env kizuNavi-backend
```

フロントエンドと一緒に起動する場合はプロジェクトルートで:

```bash
cd ..
docker compose up --build
```

- バックエンド: http://localhost:8080（`BACKEND_PORT`）
- フロントエンド: http://localhost:5173（`FRONTEND_PORT`）

## JPA エンティティの自動生成

DDL（[`src/main/resources/db/oracle/kizunavi_ddl.sql`](src/main/resources/db/oracle/kizunavi_ddl.sql)）から `com.kizunavi.entity` 配下のエンティティを生成する。

```bash
# 生成のみ（出力: build/generated-sources/entities）
./gradlew generateEntities

# 上記のあと src/main/java/com/kizunavi/entity へコピー
./gradlew copyGeneratedEntities

# 生成とコピーを連続実行
./gradlew generateAndCopyEntities
```

Windows では `gradlew.bat` を使用する。生成ファイルには「手編集しない」旨のヘッダが付く。DDL の `COMMENT ON` がクラス・フィールドの Javadoc になる。物理 FK のみ `@ManyToOne` を出力し、概念 FK は ID カラムとして扱う。

## ビルド

```bash
./gradlew build
```

ビルド成果物は `build/libs/` に生成されます。

## テスト

```bash
./gradlew test
```

カバレッジレポート（JaCoCo）を生成する場合:

```bash
./gradlew test jacocoTestReport
```

- HTML レポート: `build/reports/jacoco/test/html/index.html`
- 方針: [ADR-0024 単体テスト戦略](../docs/adr/0024-testing-strategy.md)
- 初学者向け: [テスト入門ガイド](../docs/guides/testing-guide.md)

## ライセンス

MIT License
