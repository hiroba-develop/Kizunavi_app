# AWS RDS Oracle 接続設定ガイド

このドキュメントでは、AWS RDS for Oracle にアプリケーションを接続するための設定手順を説明します。

> **重要**: 本テンプレートでは **H2 を使用しません**。自 PC の `bootRun`、Docker Compose、AWS dev EC2 はいずれも **`dev` プロファイル**で **dev 環境の RDS Oracle** に接続します。接続情報はルートの `.env`（または EC2 上の環境変数 / Secrets Manager）で必須設定です。

### アプリ接続ユーザーと RDS マスターユーザー

- **アプリケーション**（Spring の DataSource）は、スキーマ **`template_app`** に対応する専用ユーザー **`template_app`** で接続する（`DB_USERNAME` / `DB_PASSWORD`）。JPA エンティティは `schema = "template_app"` で定義されている。
- **RDS 作成時のマスターユーザー**（コンソール上の管理者）は、**テーブルスペース・ユーザー作成・初回 DDL 適用**など DBA 作業にのみ使用する。API の通常運用では使わない。

テーブルスペース・ユーザー作成の例は [backend/src/main/resources/db/oracle/login_schema.sql](../../backend/src/main/resources/db/oracle/login_schema.sql) の `[実施済み]` コメントを参照。オブジェクト作成は同ファイルの `CREATE TABLE` / `CREATE INDEX` を **`template_app` で実行**する。

## 目次

1. [AWS RDS 側の設定](#1-aws-rds-側の設定)
2. [アプリケーション側の設定変更](#2-アプリケーション側の設定変更)
3. [本番プロファイルの作成](#3-本番プロファイルの作成)
4. [Docker Compose での利用](#4-docker-compose-での利用)
5. [接続確認方法](#5-接続確認方法)
6. [トラブルシューティング](#6-トラブルシューティング)

---

## 1. AWS RDS 側の設定

### 1.1 RDS Oracle インスタンスの作成

AWS マネジメントコンソールから RDS インスタンスを作成します。

1. **AWS マネジメントコンソール** にログイン
2. **RDS** サービスに移動
3. **データベースの作成** をクリック

#### 推奨設定

| 項目 | 推奨値 | 備考 |
|------|--------|------|
| エンジン | Oracle | Standard Edition 2 または Enterprise Edition |
| エンジンバージョン | 19c 以降 | アプリケーションは ojdbc11 を使用 |
| テンプレート | 本番用 or 開発/テスト | 用途に応じて選択 |
| インスタンスクラス | db.t3.medium 以上 | 本番は db.m5.large 以上推奨 |
| ストレージタイプ | gp3 | IOPS が必要な場合は io1 |
| ストレージサイズ | 100GB 以上 | 要件に応じて調整 |
| マルチAZ | 本番環境では有効化 | 高可用性のため |

### 1.2 VPC / セキュリティグループの設定

RDS インスタンスへのアクセスを許可するセキュリティグループを設定します。

#### セキュリティグループのインバウンドルール

| タイプ | プロトコル | ポート | ソース | 説明 |
|--------|----------|--------|--------|------|
| Oracle-RDS | TCP | 1521 | アプリケーションのセキュリティグループ ID | Oracle 接続用 |

**設定手順:**

1. **EC2** → **セキュリティグループ** に移動
2. RDS 用のセキュリティグループを選択（または新規作成）
3. **インバウンドルールを編集**
4. 以下のルールを追加:
   - タイプ: `Oracle-RDS`
   - ポート範囲: `1521`
   - ソース: アプリケーション **EC2** のセキュリティグループ ID、または特定の IP/CIDR

### 1.3 サブネットグループの設定

RDS インスタンスを配置するサブネットグループを作成します。

1. **RDS** → **サブネットグループ** に移動
2. **DB サブネットグループを作成**
3. 少なくとも2つの異なる AZ にあるプライベートサブネットを選択

### 1.4 パラメータグループの設定

必要に応じてカスタムパラメータグループを作成します。

1. **RDS** → **パラメータグループ** に移動
2. **パラメータグループを作成**
3. ファミリー: `oracle-se2-19` など（エンジンバージョンに合わせる）

#### 推奨パラメータ設定

| パラメータ | 値 | 説明 |
|-----------|-----|------|
| `nls_language` | `JAPANESE` | 日本語設定 |
| `nls_territory` | `JAPAN` | 日本のロケール |
| `open_cursors` | `300` | オープンカーソル数 |
| `processes` | `300` | 最大プロセス数 |

### 1.5 パブリックアクセスの設定

| 環境 | パブリックアクセス | 説明 |
|------|-------------------|------|
| 開発/テスト | 有効可 | ローカル開発からの接続が必要な場合 |
| 本番 | **無効** | セキュリティのため、VPC 内からのみアクセス |

> **注意**: パブリックアクセスを有効にする場合でも、セキュリティグループで接続元 IP を制限してください。

---

## 2. アプリケーション側の設定変更

### 2.1 環境変数の設定

`backend/.env` ファイルを作成し、以下の値を設定します。

```bash
# backend/.env を作成（.env.example をコピー）
cp backend/.env.example backend/.env
```

#### 変更が必要な環境変数

| 環境変数 | 現在の値（例） | 変更後の値（例） |
|----------|---------------|-----------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `dev`（開発用 RDS）または `prod`（本番 RDS） |
| `DB_URL` | `jdbc:oracle:thin:@//...rds...` | 接続先 RDS の JDBC URL（下記参照） |
| `DB_USERNAME` | `template_app` | アプリ専用ユーザー（スキーマ `template_app`） |
| `DB_PASSWORD` | （例）`template_app_pass0426` | 上記ユーザーのパスワード（本番は必ず独自の強い値に変更） |

#### DB_URL の書き方

RDS のエンドポイントは AWS コンソールの RDS インスタンス詳細画面で確認できます。

**形式:**
```
jdbc:oracle:thin:@//[エンドポイント]:1521/[データベース名]
```

**例:**
```bash
# .env ファイル
DB_URL=jdbc:oracle:thin:@//mydb-instance.xxxxxxxxxxxx.ap-northeast-1.rds.amazonaws.com:1521/ORCL
DB_USERNAME=template_app
DB_PASSWORD=template_app_pass0426
```

> **注意**: データベース名は RDS 作成時に指定した「DB インスタンス識別子」ではなく、「データベース名」（DB name）を使用します。

### 2.2 application.yml の設定箇所

`backend/src/main/resources/application.yml` の以下の箇所が環境変数で上書きされます：

```yaml
# 13-24行目: データソース設定
spring:
  datasource:
    url: ${DB_URL:jdbc:oracle:thin:@localhost:1521:xe}           # ← DB_URL で上書き
    username: ${DB_USERNAME:template_app}                        # ← DB_USERNAME で上書き
    password: ${DB_PASSWORD:template_app_pass0426}              # ← DB_PASSWORD で上書き
    driver-class-name: oracle.jdbc.OracleDriver
    hikari:
      pool-name: KizuNaviHikariCP
      maximum-pool-size: ${HIKARI_MAX_POOL_SIZE:10}              # ← 必要に応じて調整
      minimum-idle: ${HIKARI_MIN_IDLE:5}
      idle-timeout: ${HIKARI_IDLE_TIMEOUT:300000}
      max-lifetime: ${HIKARI_MAX_LIFETIME:1200000}
      connection-timeout: ${HIKARI_CONNECTION_TIMEOUT:30000}
      leak-detection-threshold: ${HIKARI_LEAK_DETECTION:60000}
```

### 2.3 HikariCP プール設定の推奨値

本番環境では以下の設定を推奨します：

```bash
# .env ファイル
HIKARI_MAX_POOL_SIZE=20          # 最大接続数（RDS インスタンスサイズに応じて調整）
HIKARI_MIN_IDLE=10               # 最小アイドル接続数
HIKARI_IDLE_TIMEOUT=600000       # アイドルタイムアウト（10分）
HIKARI_MAX_LIFETIME=1800000      # 最大生存時間（30分）
HIKARI_CONNECTION_TIMEOUT=30000  # 接続タイムアウト（30秒）
```

> **参考**: RDS インスタンスの最大接続数は `SHOW PARAMETER processes` で確認できます。アプリケーションのプールサイズはこの値を超えないように設定してください。

---

## 3. Spring プロファイル（`dev` / `prod`）

リポジトリには次のファイルが同梱されています。

- [`backend/src/main/resources/application-dev.yml`](../../backend/src/main/resources/application-dev.yml) … 自 PC / Docker / **dev EC2** 共通。`ddl-auto` の既定は `update`。
- [`backend/src/main/resources/application-prod.yml`](../../backend/src/main/resources/application-prod.yml) … **prod EC2** 専用。`ddl-auto` の既定は `validate`。

本番で `prod` を有効にするには `.env` または起動引数で指定します。

```bash
# .env ファイル
SPRING_PROFILES_ACTIVE=prod
```

```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## 4. Docker Compose での利用

`docker-compose.yml` は環境変数で設定を上書きできるように構成されています。

### 4.1 起動コマンド

```bash
# 事前に .env に DB_URL / DB_USERNAME / DB_PASSWORD / AWS_* / JWT_SECRET を記入（SPRING_PROFILES_ACTIVE は通常 dev）
cp .env.example .env

# 自 PC（Docker）— dev 用 RDS に接続
docker compose up --build

# prod 用 RDS に接続して検証する場合（非本番検証用。本番は EC2 + Docker 推奨）
SPRING_PROFILES_ACTIVE=prod docker compose up --build
```

### 4.2 .env ファイルを使用する方法（推奨）

プロジェクトルートの `.env.example` をコピーして `.env` を作成し、設定を編集します：

```bash
cp .env.example .env
```

`.env` ファイルの例：

```bash
# プロファイル
SPRING_PROFILES_ACTIVE=dev

# データベース
DB_URL=jdbc:oracle:thin:@//your-rds-endpoint.ap-northeast-1.rds.amazonaws.com:1521/ORCL
DB_USERNAME=template_app
DB_PASSWORD=template_app_pass0426

# JWT
JWT_SECRET=your-256-bit-secret-key-here-must-be-at-least-32-characters-long

# AWS SES
AWS_SES_REGION=ap-northeast-1
AWS_ACCESS_KEY=AKIAXXXXXXXXXXXXXXXX
AWS_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
AWS_SES_FROM_EMAIL=noreply@yourdomain.com
```

起動：

```bash
docker compose up --build
```

> **注意**: `.env` ファイルは `.gitignore` に追加し、Git にコミットしないでください。

---

## 5. 接続確認方法

### 5.1 Actuator ヘルスチェック

アプリケーション起動後、以下のエンドポイントで DB 接続状態を確認：

```bash
curl http://localhost:8080/actuator/health
```

**正常時のレスポンス例:**
```json
{
  "status": "UP"
}
```

### 5.2 データベース接続の詳細確認

`application.yml` のヘルス設定を変更して詳細を表示：

```yaml
management:
  endpoint:
    health:
      show-details: always    # UP のみでなく詳細を表示
```

```bash
curl http://localhost:8080/actuator/health
```

**詳細表示時のレスポンス例:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "Oracle",
        "validationQuery": "SELECT 1 FROM DUAL"
      }
    }
  }
}
```

### 5.3 Oracle への接続確認（任意）

Actuator に DB コンポーネントを追加している場合は、`management.endpoint.health.show-details` を一時的に `always` にして `components.db` を確認してください。H2 コンソールは本テンプレートでは提供しません。

---

## 6. トラブルシューティング

### 6.1 接続エラー

**エラー:** `ORA-12170: TNS:Connect timeout occurred`

**原因と対処:**
- セキュリティグループでポート 1521 が開放されていない
- RDS インスタンスがパブリックアクセス不可でローカルから接続しようとしている
- VPC のルートテーブル設定が不正

**確認コマンド:**
```bash
# ポート疎通確認
telnet your-rds-endpoint.ap-northeast-1.rds.amazonaws.com 1521
```

### 6.2 認証エラー

**エラー:** `ORA-01017: invalid username/password; logon denied`

**対処:**
- `DB_USERNAME` と `DB_PASSWORD` が正しいか確認（通常は **`template_app`** 用。マスターユーザーではない）
- マスターユーザーのパスワードは AWS コンソールからリセット可能（`template_app` のパスワードは DBA が `ALTER USER` で変更）

### 6.3 データベース名エラー

**エラー:** `ORA-12505: TNS:listener does not currently know of SID given in connect descriptor`

**対処:**
- JDBC URL の形式を確認
- SID 形式: `jdbc:oracle:thin:@host:1521:SID`
- サービス名形式: `jdbc:oracle:thin:@//host:1521/SERVICE_NAME`
- RDS は通常サービス名形式（`@//host:1521/ORCL`）を使用

### 6.4 接続プールの枯渇

**エラー:** `Connection is not available, request timed out after 30000ms`

**対処:**
- `HIKARI_MAX_POOL_SIZE` を増やす（ただし RDS の最大接続数を超えないこと）
- アプリケーション側で接続のリークがないか確認
- `HIKARI_LEAK_DETECTION` を有効にしてリークを検出

---

## チェックリスト

本番デプロイ前に以下を確認：

- [ ] RDS インスタンスが作成され、「利用可能」状態である
- [ ] セキュリティグループでポート 1521 が適切に開放されている
- [ ] `DB_URL` に正しい RDS エンドポイントが設定されている
- [ ] `DB_USERNAME` と `DB_PASSWORD` が正しく設定されている
- [ ] 本番では `SPRING_PROFILES_ACTIVE=prod` が設定されている
- [ ] `application-prod.yml` がリポジトリに含まれている（カスタマイズ済みであること）
- [ ] Actuator ヘルスチェックで "UP" が返る
- [ ] HikariCP のプールサイズが適切に設定されている
