# 単体テスト実施一覧（テスト台帳）

認証をはじめとするドメイン別に、**どの観点で何をテストしているか**を管理するディレクトリです。  
実装コード（`src/test` / `__tests__`）と **常に整合** させてください。

## ファイル構成

| ファイル | 内容 |
|----------|------|
| [auth-backend.md](auth-backend.md) | 認証関連（バックエンド・JUnit 5） |
| [auth-frontend.md](auth-frontend.md) | 認証関連（フロントエンド・Vitest） |

今後ドメインが増えたら、同じ形式で `employees-backend.md` のように追加します。

## 更新ルール（必須）

次のいずれかに該当する PR では、**対応する台帳を同じ PR で更新** してください。

1. 認証（または台帳対象ドメイン）の **本番コード** を変更した
2. 上記に対応する **単体テスト** を追加・削除・変更した
3. テスト観点・カバレッジ目標・対象ファイルの範囲が変わった

### 更新手順

1. テストコードの `@DisplayName` / `it(...)` と台帳の行を一致させる
2. 台帳先頭の **最終更新日**（`YYYY-MM-DD`）を更新する
3. カバレッジを確認し、台帳の **カバレッジ状況** セクションを更新する
4. 新規テストファイルを追加した場合は、**テストファイル一覧** に追記する

### カバレッジ確認コマンド

```bash
# バックエンド（JaCoCo）
cd backend
./gradlew test jacocoTestReport
# → build/reports/jacoco/test/html/index.html

# フロントエンド（Vitest / v8）
cd frontend
npm run test:coverage
# → coverage/index.html
```

認証ドメインのみを絞る例（フロント）は [auth-frontend.md](auth-frontend.md) を参照。

## 観点ラベル（共通）

| ラベル | 意味 |
|--------|------|
| 正常系 | 期待どおりの入力・状態で処理が完了する |
| 異常系 | バリデーション失敗・認証失敗・トークン無効など |
| 境界 | null / 空文字 / センチネル値・二重実行など |
| セキュリティ | 存在秘匿（常に success）、エラーメッセージの統一など |
| 回帰 | ロック・失敗回数・トークンローテーションなど既知仕様の固定 |
| UI | 画面表示・バリデーション・ローディング（フロントのみ） |
| I/O 隔離 | DB / HTTP / SES をモックし単体テストで隔離している |

## 関連ドキュメント

- [ADR-0024: 単体テスト戦略（C0）](../adr/0024-testing-strategy.md)
- [テスト入門ガイド](../guides/testing-guide.md)
- [ログインフロー](../guides/login-flow.md)
- [`.cursor/rules/60-testing.mdc`](../../.cursor/rules/60-testing.mdc)
