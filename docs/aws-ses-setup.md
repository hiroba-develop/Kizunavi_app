# AWS SES (Simple Email Service) 設定ガイド

このドキュメントでは、AWS SES を使用してアプリケーションからメールを送信するための設定手順を説明します。

> **重要**: 本テンプレートでは **`dev` / `prod` いずれのプロファイルでも SES が有効**です。自 PC・Docker・dev EC2 でも **dev 用 SES 設定**（`.env` の `AWS_*`）が必須です。

## 目次

1. [AWS SES 側の設定](#1-aws-ses-側の設定)
2. [IAM ユーザーとポリシーの作成](#2-iam-ユーザーとポリシーの作成)
3. [アプリケーション側の設定変更](#3-アプリケーション側の設定変更)
4. [Docker Compose での利用](#4-docker-compose-での利用)
5. [テスト・動作確認](#5-テスト動作確認)
6. [本番運用時の注意事項](#6-本番運用時の注意事項)
7. [トラブルシューティング](#7-トラブルシューティング)

---

## 1. AWS SES 側の設定

### 1.1 SES の利用開始

1. **AWS マネジメントコンソール** にログイン
2. **SES (Simple Email Service)** サービスに移動
3. リージョンを選択（推奨: **ap-northeast-1（東京）**）

> **重要**: アプリケーションで設定するリージョン（`AWS_SES_REGION`）と同じリージョンを選択してください。

### 1.2 送信元メールアドレスの検証

SES からメールを送信するには、送信元（From）アドレスを検証する必要があります。

#### メールアドレス検証手順

1. SES コンソールで **検証済み ID** → **ID の作成** をクリック
2. **E メールアドレス** を選択
3. 送信元として使用するメールアドレスを入力
4. **ID の作成** をクリック
5. 入力したメールアドレスに検証メールが届く
6. メール内の検証リンクをクリック

**検証完了の確認:**
- SES コンソールで該当メールアドレスのステータスが「検証済み」になっていることを確認

### 1.3 ドメイン検証（推奨）

本番環境では、個別のメールアドレスではなく、ドメイン全体を検証することを推奨します。

#### ドメイン検証手順

1. SES コンソールで **検証済み ID** → **ID の作成** をクリック
2. **ドメイン** を選択
3. ドメイン名を入力（例: `yourdomain.com`）
4. **DKIM 署名を有効にする** にチェック（推奨）
5. **ID の作成** をクリック
6. 表示される DNS レコードを、ドメインの DNS 設定に追加

**追加が必要な DNS レコード:**

| タイプ | 名前 | 値 | 用途 |
|--------|------|-----|------|
| TXT | `_amazonses.yourdomain.com` | (表示される値) | ドメイン所有権の確認 |
| CNAME | (3つ) | (表示される値) | DKIM 署名用 |
| MX | (オプション) | `inbound-smtp.ap-northeast-1.amazonaws.com` | メール受信用 |

### 1.4 サンドボックスモードについて

新規の SES アカウントは **サンドボックスモード** で開始されます。

#### サンドボックスモードの制限

| 制限項目 | サンドボックス | 本番 |
|----------|--------------|------|
| 送信先 | 検証済みアドレスのみ | 任意のアドレス |
| 送信レート | 1通/秒 | リクエストに応じて |
| 1日の送信上限 | 200通 | リクエストに応じて |

### 1.5 サンドボックス解除申請

本番利用には、サンドボックス解除が必要です。

#### 申請手順

1. SES コンソールで **アカウントダッシュボード** に移動
2. 「サンドボックス」セクションで **本番アクセスをリクエスト** をクリック
3. 以下の情報を入力:
   - **メールタイプ**: トランザクション（パスワードリセット等）
   - **ウェブサイト URL**: アプリケーションの URL
   - **ユースケースの説明**: 詳細な利用目的
   - **送信予定量**: 1日の送信数、送信先の種類

**申請のポイント:**
- 具体的なユースケースを記載（例: ユーザー登録確認、パスワードリセット）
- オプトアウト（配信停止）の仕組みがあることを明記
- バウンス・苦情の処理方法を記載
- 審査には1〜2営業日かかる場合があります

---

## 2. IAM ユーザーとポリシーの作成

### 2.1 SES 送信用 IAM ポリシーの作成

1. **IAM** コンソールに移動
2. **ポリシー** → **ポリシーを作成**
3. JSON タブで以下を入力:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowSendEmail",
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail",
                "ses:SendRawEmail"
            ],
            "Resource": "*"
        }
    ]
}
```

4. ポリシー名: `SES-SendEmail-Policy`
5. **ポリシーを作成**

#### より制限的なポリシー（推奨）

特定の送信元アドレスのみ許可する場合:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowSendEmailFromVerifiedIdentity",
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail",
                "ses:SendRawEmail"
            ],
            "Resource": "arn:aws:ses:ap-northeast-1:123456789012:identity/noreply@yourdomain.com"
        }
    ]
}
```

> **注意**: `123456789012` は実際の AWS アカウント ID に置き換えてください。

### 2.2 IAM ユーザーの作成

1. **IAM** コンソールで **ユーザー** → **ユーザーを追加**
2. ユーザー名: `ses-mail-sender`（任意）
3. **AWS 認証情報タイプ**: アクセスキー - プログラムによるアクセス
4. **アクセス許可**: 作成した `SES-SendEmail-Policy` をアタッチ
5. ユーザーを作成

### 2.3 アクセスキーの発行

1. 作成したユーザーを選択
2. **セキュリティ認証情報** タブ
3. **アクセスキーを作成**
4. ユースケース: **ローカルコード** または **AWS の外部で実行されるアプリケーション**
5. **アクセスキー ID** と **シークレットアクセスキー** を安全に保存

> **重要**: シークレットアクセスキーはこの時点でのみ表示されます。紛失した場合は新しいキーを発行する必要があります。

### 2.4 IAM ロールの利用（EC2 環境推奨）

EC2 で実行する場合は、アクセスキーではなく **インスタンスプロファイル（IAM ロール）** の利用を推奨します。

**メリット:**
- アクセスキーの管理が不要
- 自動的にキーがローテーション
- よりセキュア

**設定方法:**
1. EC2 インスタンスプロファイルに `SES-SendEmail-Policy` をアタッチ
2. アプリケーションの `AWS_ACCESS_KEY` と `AWS_SECRET_KEY` を空にする
3. AWS SDK がデフォルトの認証情報プロバイダーチェーンで IAM ロールを使用

---

## 3. アプリケーション側の設定変更

### 3.1 環境変数の設定

`backend/.env` ファイルで以下の値を設定します：

```bash
# backend/.env
# AWS SES 設定
AWS_SES_REGION=ap-northeast-1
AWS_ACCESS_KEY=AKIAXXXXXXXXXXXXXXXX
AWS_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
AWS_SES_FROM_EMAIL=noreply@yourdomain.com
```

| 環境変数 | 説明 | 例 |
|----------|------|-----|
| `AWS_SES_REGION` | SES のリージョン | `ap-northeast-1` |
| `AWS_ACCESS_KEY` | IAM アクセスキー ID | `AKIAXXXXXXXXXXXXXXXX` |
| `AWS_SECRET_KEY` | IAM シークレットアクセスキー | （40文字の文字列） |
| `AWS_SES_FROM_EMAIL` | 送信元メールアドレス（検証済み） | `noreply@yourdomain.com` |

### 3.2 application.yml の設定箇所

`backend/src/main/resources/application.yml` の以下の箇所が環境変数で上書きされます：

```yaml
# 46-51行目: AWS SES 設定
aws:
  ses:
    region: ${AWS_SES_REGION:ap-northeast-1}     # ← AWS_SES_REGION で上書き
    access-key: ${AWS_ACCESS_KEY:}               # ← AWS_ACCESS_KEY で上書き
    secret-key: ${AWS_SECRET_KEY:}               # ← AWS_SECRET_KEY で上書き
    from-email: ${AWS_SES_FROM_EMAIL:noreply@example.com}  # ← AWS_SES_FROM_EMAIL で上書き
```

### 3.3 SES 有効化の設定

**プロファイルは `dev` と `prod` のみ**で、どちらも [`application-dev.yml`](../../backend/src/main/resources/application-dev.yml) / [`application-prod.yml`](../../backend/src/main/resources/application-prod.yml) で **`aws.ses.enabled: true`** です。

| プロファイル | aws.ses.enabled | 動作 |
|-------------|-----------------|------|
| dev | `true` | 実際にメール送信（開発用 SES / サンドボックス想定） |
| prod | `true` | 実際にメール送信（本番 SES） |

**前提:** `.env` または EC2 の環境変数に `AWS_ACCESS_KEY` / `AWS_SECRET_KEY` / `AWS_SES_FROM_EMAIL` を設定すること。IAM ロール利用時はキーを空にし、SDK のデフォルト認証チェーンに任せる。

### 3.4 EmailService の動作説明

`backend/src/main/java/com/kizunavi/service/EmailService.java` は以下のメソッドを提供します：

| メソッド | 用途 | 引数 |
|----------|------|------|
| `sendEmail(to, subject, htmlBody)` | 汎用メール送信 | 宛先、件名、HTML本文 |
| `sendWelcomeEmail(to, name)` | 登録完了メール | 宛先、ユーザー名 |
| `sendPasswordResetEmail(to, resetToken)` | パスワードリセット | 宛先、リセットトークン |

**動作条件:**
- `aws.ses.enabled` が `true`
- `AWS_ACCESS_KEY` と `AWS_SECRET_KEY` が設定されている

上記条件を満たさない場合、メールは送信されずログ出力のみ行われます。

---

## 4. Docker Compose での利用

`docker-compose.yml` は環境変数で設定を上書きできるように構成されています。

### 4.1 起動コマンド

```bash
# .env に AWS_SES_* および DB / JWT を設定したうえで（既定プロファイルは dev）
docker compose up --build

# prod プロファイルで検証する場合（本番は EC2 + Docker 推奨）
SPRING_PROFILES_ACTIVE=prod docker compose up --build
```

### 4.2 .env ファイルを使用する方法（推奨）

プロジェクトルートの `.env.example` をコピーして `.env` を作成し、設定を編集します：

```bash
cp .env.example .env
```

起動：

```bash
docker compose up --build
```

### 4.3 機密情報の管理

機密情報（アクセスキー等）は `.env` ファイルで管理し、Git にコミットしないでください。

```bash
# プロジェクトルートに .env ファイルを作成
DB_PASSWORD=your-db-password
JWT_SECRET=your-256-bit-secret-key
AWS_ACCESS_KEY=AKIAXXXXXXXXXXXXXXXX
AWS_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

```yaml
# docker-compose.yml
services:
  backend:
    env_file:
      - .env
    environment:
      # .env の値を参照
      DB_PASSWORD: ${DB_PASSWORD}
```

---

## 5. テスト・動作確認

### 5.1 サンドボックス環境でのテスト

サンドボックスモードでは、送信先も検証済みである必要があります。

1. テスト用の受信メールアドレスを SES で検証
2. アプリケーションからそのアドレス宛にメール送信
3. メールが届くことを確認

### 5.2 ログでの送信確認

アプリケーションログで送信結果を確認できます。

**成功時のログ:**
```
INFO  c.p.t.service.EmailService - Email sent successfully. MessageId: 0100018e-xxxx-xxxx-xxxx-xxxxxxxxxxxx-000000
```

**SES 無効時のログ:**
```
INFO  c.p.t.service.EmailService - Email service disabled. Would have sent email to: user@example.com, subject: ようこそ！
```

**認証情報未設定時のログ:**
```
WARN  c.p.t.service.EmailService - AWS SES credentials not configured. Email service will not send emails.
```

### 5.3 テスト用 API エンドポイント

開発環境でテスト用のエンドポイントを追加することも検討できます（本番では削除）：

```java
@RestController
@RequestMapping("/api/test")
@Profile("dev")  // 開発環境のみ有効
public class TestController {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send-test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        emailService.sendEmail(to, "テストメール", "<h1>テスト</h1><p>これはテストメールです。</p>");
        return ResponseEntity.ok("Test email sent to: " + to);
    }
}
```

---

## 6. 本番運用時の注意事項

### 6.1 バウンス（不達）処理の設定

バウンスが多発するとアカウントが停止される可能性があります。

**SNS トピックの設定:**
1. SNS でトピックを作成（例: `ses-bounces`）
2. SES コンソール → 検証済み ID → 設定 → 通知
3. バウンス通知を SNS トピックに設定
4. Lambda 等でバウンスを処理

### 6.2 苦情（Complaint）処理の設定

受信者が「迷惑メール報告」した場合の通知を設定します。

1. SNS でトピックを作成（例: `ses-complaints`）
2. SES コンソールで苦情通知を設定
3. 苦情のあったアドレスは送信リストから除外

### 6.3 送信レート制限

| レベル | 送信レート | 1日の送信上限 |
|--------|-----------|--------------|
| サンドボックス | 1通/秒 | 200通 |
| 本番（初期） | 14通/秒 | 50,000通 |
| 本番（上限引き上げ後） | リクエストに応じて | リクエストに応じて |

**上限引き上げ申請:**
- SES コンソール → アカウントダッシュボード → 送信制限の引き上げをリクエスト

### 6.4 IAM ロールの利用推奨

EC2 環境では、アクセスキーではなく IAM ロール（インスタンスプロファイル）の利用を強く推奨します。

**理由:**
- キーのローテーションが自動化される
- キー漏洩のリスクが低減
- AWS のベストプラクティス

**設定方法:**

1. EC2 インスタンスプロファイルに SES ポリシーをアタッチ
2. アプリケーション設定で `AWS_ACCESS_KEY` と `AWS_SECRET_KEY` を空に
3. `EmailService.java` を修正（DefaultCredentialsProvider を使用）:

```java
// 現在の実装
if (enabled && !accessKey.isEmpty() && !secretKey.isEmpty()) {
    this.sesClient = SesClient.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();
}

// IAM ロール対応版（推奨）
if (enabled) {
    SesClientBuilder builder = SesClient.builder()
        .region(Region.of(region));
    
    if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
        // 明示的な認証情報がある場合は使用
        builder.credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ));
    }
    // それ以外はデフォルトの認証情報チェーン（IAM ロール等）を使用
    
    this.sesClient = builder.build();
}
```

### 6.5 送信元アドレスの DKIM 設定

DKIM を設定すると、メールの信頼性が向上し、迷惑メールフォルダに振り分けられにくくなります。

1. SES コンソール → 検証済み ID → ドメインを選択
2. **認証** タブ → **DKIM** セクション
3. Easy DKIM を有効化
4. 表示される CNAME レコードを DNS に追加

---

## 7. トラブルシューティング

### 7.1 メールが送信されない

**確認ポイント:**

1. **SES が有効か確認**
   ```bash
   # ログを確認
   grep "Email service disabled" application.log
   ```

2. **認証情報が設定されているか確認**
   ```bash
   grep "SES credentials not configured" application.log
   ```

3. **プロファイルの確認**
   - `SPRING_PROFILES_ACTIVE` は **`dev`（既定）または `prod`**。どちらも SES は有効。
   - 送信されない場合は **認証情報未設定**（`SES credentials not configured`）やサンドボックス制限を疑う。

### 7.2 認証エラー

**エラー:** `InvalidClientTokenId: The security token included in the request is invalid`

**対処:**
- アクセスキー ID が正しいか確認
- IAM ユーザーが有効か確認

**エラー:** `SignatureDoesNotMatch: The request signature we calculated does not match...`

**対処:**
- シークレットアクセスキーが正しいか確認
- 環境変数にスペースや改行が含まれていないか確認

### 7.3 送信先エラー

**エラー:** `MessageRejected: Email address is not verified`

**原因:**
- サンドボックスモードで、検証されていないアドレスに送信しようとした

**対処:**
- 送信先アドレスを SES で検証する
- または、サンドボックス解除を申請する

### 7.4 リージョンエラー

**エラー:** `Could not connect to the endpoint URL`

**対処:**
- `AWS_SES_REGION` が正しいリージョンコードか確認
- 例: `ap-northeast-1`（東京）、`us-east-1`（バージニア）

### 7.5 レート制限エラー

**エラー:** `Throttling: Maximum sending rate exceeded`

**対処:**
- 送信間隔を空ける
- 送信レートの引き上げを申請

---

## チェックリスト

本番デプロイ前に以下を確認：

- [ ] 送信元メールアドレス / ドメインが SES で検証済み
- [ ] サンドボックス解除が完了している（本番の場合）
- [ ] IAM ユーザー / ポリシーが作成されている
- [ ] アクセスキーが発行され、安全に保存されている
- [ ] `AWS_SES_REGION` が正しく設定されている
- [ ] `AWS_ACCESS_KEY` と `AWS_SECRET_KEY` が設定されている
- [ ] `AWS_SES_FROM_EMAIL` が検証済みアドレスになっている
- [ ] `SPRING_PROFILES_ACTIVE=prod` が設定されている
- [ ] テストメールが正常に送信・受信できる
- [ ] バウンス / 苦情通知が設定されている
- [ ] DKIM が設定されている（推奨）
- [ ] 機密情報（アクセスキー等）が Git にコミットされていない

---

## 参考リンク

- [AWS SES 開発者ガイド](https://docs.aws.amazon.com/ja_jp/ses/latest/dg/Welcome.html)
- [AWS SDK for Java 2.x - SES](https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/java_ses_code_examples.html)
- [SES サンドボックス解除申請](https://docs.aws.amazon.com/ja_jp/ses/latest/dg/request-production-access.html)
- [DKIM 認証の設定](https://docs.aws.amazon.com/ja_jp/ses/latest/dg/send-email-authentication-dkim.html)
