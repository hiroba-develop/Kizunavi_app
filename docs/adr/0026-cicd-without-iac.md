# ADR-0026: CI/CD 方針（GitHub Actions / ECR、IaC は見送り）

- Status: Accepted
- Date: 2026-05-07
- Deciders: Product Template チーム

## Context

イメージのビルド・レジストリ連携は自動化しつつ、**インフラのコード化（Terraform 等）は当面導入しない**。

本決定は [ADR-0022（CI/CD と IaC）](0022-aws-cicd-iac.md) を置き換える。

## Decision

- **コンテナレジストリ**: **Amazon ECR**。`backend` / `frontend` でリポジトリを分けるか、タグで分離するかはプロダクトで選択。
- **CI**: **GitHub Actions** を推奨。**OIDC** で AWS に接続し、長期アクセスキーを避ける（`ecr:GetAuthorizationToken` および push 用権限）。
- **デプロイ**: **EC2 への手動 SSH** 後、`aws ecr get-login-password` でログインし、`docker compose pull && up -d`。
- **IaC**: **Terraform / CDK 等は導入見送り**。VPC・ALB・EC2 はコンソールまたは組織標準手順で構築する。

## Consequences

- Positive: 初期のインフラセットアップ負荷と state 管理のオーバーヘッドを避けられる。
- Negative: 環境差分の **再現性・レビュー性**は IaC より弱くなる。構成ドリフトに注意。

## Alternatives Considered

- **Terraform + S3 backend**: 再現性は高いが、本プロダクトではスコープ外（将来再検討）。
- **CodePipeline のみ GUI**: パイプラインは GUI に閉じがちで、EC2 手動デプロイとの組み合わせはチーム方針次第。

## References

- [docs/aws-ecr-setup.md](../aws-ecr-setup.md)
- [docs/ops/ec2-new-product-release.md](../ops/ec2-new-product-release.md)
- [ADR-0022（Deprecated）](0022-aws-cicd-iac.md)
