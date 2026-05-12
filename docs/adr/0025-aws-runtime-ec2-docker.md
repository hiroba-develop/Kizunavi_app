# ADR-0025: AWS ランタイム（EC2 + Docker）

- Status: Accepted
- Date: 2026-05-07
- Deciders: Product Template チーム

## Context

製品の運用形態として **単一 EC2 インスタンス上で Docker（docker compose）** を採用する。HTTPS 終端は **Application Load Balancer（ACM）** に集約し、デプロイは **SSH 手動**（`docker compose pull && up -d`）とする。

本決定は [ADR-0017（ECS on Fargate）](0017-aws-runtime-ecs-fargate.md) を置き換える。

## Decision

- **コンピュート**: **単一 EC2**（将来は複数台 + ALB に拡張可能）。ランタイムは **Docker / docker compose**。
- **HTTPS**: **ALB** で終端（443）。パスルーティング例: `/api/*` → バックエンド（8080）、デフォルト → フロント（Vite preview 5173）。
- **シークレット**: **AWS Secrets Manager** を正とする。ECS のタスク定義 `secrets` は使わず、**起動スクリプト（systemd `ExecStartPre` 等）** で取得し環境変数を設定してから `docker compose up`。
- **ログ**: **CloudWatch Logs**（Docker の `awslogs` ロギングドライバ、または CloudWatch Agent）。
- **ECR**: CI（GitHub Actions OIDC）で push、EC2 は **インスタンスプロファイル**で `ecr:GetAuthorizationToken` 等を許可し `docker login` 後に pull。

## Consequences

- Positive: Fargate 固定費や ECS 抽象化の学習コストを抑えられる。既存の `docker-compose.yml` と親和性が高い。
- Negative: **EC2 の OS パッチ・可用性**はオペレーション負担。単一インスタンスは **単一障害点**。
- Negative: SSH 運用は鍵管理・踏み台・権限分離の設計が必要。

## Alternatives Considered

- **ECS on Fargate**: サーバレス運用に有利だが、本プロダクトでは採用しない（[ADR-0017](0017-aws-runtime-ecs-fargate.md) を参照）。
- **EKS**: 本テンプレート規模では運用コストが高い。

## References

- [docs/ops/ec2-new-product-release.md](../ops/ec2-new-product-release.md)
- [docs/aws-ecr-setup.md](../aws-ecr-setup.md)
- [docs/aws-secrets-manager-setup.md](../aws-secrets-manager-setup.md)
- [ADR-0017（Deprecated）](0017-aws-runtime-ecs-fargate.md)
