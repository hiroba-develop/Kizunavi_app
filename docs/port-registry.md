# ポート採番台帳（ローカル開発・Docker Compose）

テンプレートを複製して複数プロダクトを同一開発マシンで動かす場合、**ホスト側ポートが衝突**しないよう、割当を一元管理します。

## 正の台帳（スプレッドシート）

**ポート番号の割当は次のスプレッドシートで管理しています**（追加・変更はこちらを更新してください）。

[ポート採番台帳（Google スプレッドシート）](https://docs.google.com/spreadsheets/d/1pMseDeBjZCV_ppZLVuaaD78iooxR7yeXWP5jsxzJcS8/edit?gid=0#gid=0)

本ファイル（`docs/port-registry.md`）は **ルールと手順の説明**、およびオフライン参照用の簡易例として残します。表の内容とスプレッドシートが食い違う場合は **スプレッドシートを優先**してください。

## ルール

- **ベース（テンプレート本体）**: フロント **5173**、バック **8080**（コンテナ内は frontend **5173** / Spring Boot **8080**）。
- **複製プロダクト**: 原則として **同時に +1**（フロント 5174 / バック 8081、…）。
- **設定場所**:
  - ルート `.env` の `FRONTEND_PORT` / `BACKEND_PORT`（[`docker-compose.yml`](../docker-compose.yml) が参照）。
  - `CORS_ALLOWED_ORIGINS` / `APP_FRONTEND_BASE_URL` は `.env.example` のコメント通り、通常 **`http://localhost:<FRONTEND_PORT>`** に合わせる。Compose 未指定時は `FRONTEND_PORT` に自動連動。
- **ローカル（Gradle + Vite 直接起動）**でバックポートを変えたときは、`frontend/vite.config.ts` の `server.proxy` の `target` も合わせること（[ADR-0016](adr/0016-port-numbering-policy.md)）。

## 採番手順

1. 上記 **スプレッドシート**で未使用のポート組を選び、新規行を追加する（編集権限のあるメンバーが実施）。
2. 該当リポジトリのルート `.env` を編集（`cp .env.example .env` が未実施なら実行）。`FRONTEND_PORT` / `BACKEND_PORT` をスプレッドシートの値に合わせる。
3. README の URL 表記は「デフォルトは 5173/8080」とし、**カスタムポートは `.env` 参照**とする。

### 参考例（スプレッドシートのイメージ）

| No | プロダクト名 | FRONTEND_PORT | BACKEND_PORT | 備考 |
|----|--------------|---------------|--------------|------|
| 0 | Product_Template（本リポジトリ） | 5173 | 8080 | 既定 |
| 1 | （例）product-a | 5174 | 8081 | 実データはスプレッドシートを参照 |

## 関連

- [ADR-0016: ポート採番ポリシー](adr/0016-port-numbering-policy.md)
- [docs/ops/ec2-new-product-release.md](ops/ec2-new-product-release.md)（本番 URL は本台帳のローカルポートとは別管理）
