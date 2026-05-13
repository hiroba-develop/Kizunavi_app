# テスト入門ガイド（単体テスト / C0）

この資料は、テストコードを初めて書くメンバー向けの最初のガイドです。  
本プロジェクトでは、まず「単体テストを継続できる状態」を作ることを優先します。

## 1. なぜテストコードを書くのか

テストコードは「品質を守るための保険」であり、「実装の仕様書」でもあります。

- 回帰防止: 既存機能を壊していないかを変更直後に確認できる
- リファクタリング支援: 内部実装を安心して整理できる
- 仕様の明文化: 「何を期待するか」をコードで残せる
- レビュー効率化: レビュアーが意図を追いやすくなる
- 長期コスト削減: バグ修正の手戻りを小さくできる

## 2. 今回の対象範囲

今回のスコープは **単体テストのみ** です。

- 対象: サービスロジック、ユーティリティ関数、カスタムフック、Zustand ストア
- 非対象: 結合テスト、E2E テスト、性能試験
- 外部 I/O（DB / HTTP / ファイル / 外部 API）はモック化して単体テストで隔離する

方針の正式定義は [ADR-0024](../adr/0024-testing-strategy.md) を参照してください。

## 3. C0（命令網羅）とは

**C0（命令網羅）** は「ソースコードの各文を少なくとも 1 回は実行したか」を見る指標です。

- 目的: 未実行のコードを減らす
- 注意: 分岐の全パターン保証ではない

例:

- `if (isAdmin) { A } else { B }` がある場合
- `A` しか通っていなくても、他の文を実行していれば C0 はある程度上がる
- ただし `B` の正しさは保証できない（これは C1/分岐網羅の領域）

本プロジェクトでは C0 を **レポート出力のみ** で運用します（閾値で失敗させない）。

## 4. 単体テストの書き方（最小ルール）

### AAA パターン

1 テストは次の 3 段で書きます。

1. Arrange: 前提を準備する
2. Act: 対象メソッド/関数を実行する
3. Assert: 期待値を検証する

### 1テスト1振る舞い

- `@Test` / `it(...)` 1つにつき、検証する意図は1つに絞る
- 失敗時に原因が分かる単位で分割する

### モックの原則

- モックする対象は「外部境界（I/O）」のみ
- 実装内部の private ロジックまで過剰にモックしない

## 5. 命名と配置

### バックエンド（JUnit 5）

- 配置: `backend/src/test/java/<本体と同パッケージ>/<Target>Test.java`
- 推奨: `@DisplayName` は日本語で「何を保証するか」を記述
- 主なツール: JUnit 5 / Mockito / AssertJ

### フロントエンド（Vitest）

- 配置: 対象ファイルの近くに `__tests__/*.test.ts(x)`
- 主なツール: Vitest / React Testing Library / jsdom
- API クライアントなど I/O は `vi.mock(...)` で置き換える

## 6. よくある落とし穴

- 実装詳細に依存したテストを書く（リファクタで壊れやすい）
- 時刻依存を固定せずにテストする（`now()` の揺らぎで不安定になる）
- テスト間で状態を共有してしまう（前テストの副作用で失敗）
- 乱数や UUID を固定しない（再現性が下がる）

## 7. 実行方法（チートシート）

### バックエンド

```bash
cd backend
./gradlew test
./gradlew test jacocoTestReport
```

- レポート: `backend/build/reports/jacoco/test/html/index.html`

### フロントエンド

```bash
cd frontend
npm run test
npm run test:coverage
```

- レポート: `frontend/coverage/index.html`

## 8. まず読むべきテンプレート

次の順番で読むと理解しやすいです。

1. [backend/src/test/java/com/kizunavi/util/TokenHashUtilTest.java](../../backend/src/test/java/com/kizunavi/util/TokenHashUtilTest.java)
2. [frontend/src/lib/__tests__/utils.test.ts](../../frontend/src/lib/__tests__/utils.test.ts)
3. [backend/src/test/java/com/kizunavi/service/UserServiceTest.java](../../backend/src/test/java/com/kizunavi/service/UserServiceTest.java)
4. [frontend/src/store/__tests__/useAuthStore.test.ts](../../frontend/src/store/__tests__/useAuthStore.test.ts)
5. [frontend/src/hooks/__tests__/useUser.test.tsx](../../frontend/src/hooks/__tests__/useUser.test.tsx)

## 9. PR 前セルフチェック

- [ ] 変更したロジックに対して単体テストを追加した
- [ ] 正常系だけでなく、主要な異常系も1つ以上書いた
- [ ] テスト名を読めば意図が分かる
- [ ] ローカルでテストを実行し、失敗なく完了した
- [ ] カバレッジレポートを確認し、未実行コードを把握した
