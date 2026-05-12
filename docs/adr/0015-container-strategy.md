# ADR-0015: コンテナ戦略（Docker / Vite preview）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

開発・本番に近い形でスタックを起動し、**EC2 上の `docker compose` による**デプロイ単位（イメージ）を揃える。

## Decision

- **バックエンド**: マルチステージ Dockerfile — **Temurin 21 JDK** で `bootJar`、**JRE** で実行。コンテナ内 Listen **8080**。非 root ユーザー（[`backend/Dockerfile`](../../backend/Dockerfile)）。
- **フロントエンド**: **Node** で `npm run build` 後、`vite preview --host 0.0.0.0 --port 5173` で静的配信。コンテナ内 **5173**。`/api` はローカル Docker では Vite proxy、本番は ALB の L7 ルーティングでバックエンドへ振り分ける。
- **Compose**: ルート [`docker-compose.yml`](../../docker-compose.yml) で `backend` + `frontend`。DB コンテナは含めず、**AWS RDS Oracle** へ `.env` 経由で接続する。

## Consequences

- Positive: フロント実行方式を Vite に統一できる。ALB で `/api/*` と `/` をパス分割しやすい。
- Negative: **HEALTHCHECK で `curl` / `wget` 依存** — ベースイメージにツールが無いと失敗する可能性 → イメージ更新時は検証すること。

## Alternatives Considered

- **フロントも Node で SSR**: 本テンプレートは静的 SPA 前提 → 却下。

## References

- [`frontend/Dockerfile`](../../frontend/Dockerfile)
- [ADR-0025](0025-aws-runtime-ec2-docker.md)
