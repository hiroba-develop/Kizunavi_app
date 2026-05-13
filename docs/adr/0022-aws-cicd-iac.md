# ADR-0022: CI/CD と IaC（GitHub Actions / ECR / Terraform）

- Status: Deprecated（後続: [ADR-0026: CI/CD 方針（GitHub Actions / ECR、IaC は見送り）](0026-cicd-without-iac.md)）
- Date: 2026-04-20
- Deciders: KizuNavi チーム

> **廃止理由**: IaC（Terraform）の導入は見送りとなり、CI/CD とデプロイの記録は [ADR-0026](0026-cicd-without-iac.md) に集約した。

## Context

手作業の `docker build` / EC2 配置から、**再現可能なパイプライン**と**インフラのコード化**へ移行する。

## Decision

- **コンテナレジストリ**: **Amazon ECR**。`backend` / `frontend` でリポジトリを分けるか、イメージ名タグで分離するかはプロダクトで選択。
- **CI/CD**: **GitHub Actions** を推奨（例: `main` マージでビルド・テスト、タグまたは環境ブランチで ECR push → `ecs update-service --force-new-deployment`）。
- **IaC**: **Terraform** を推奨（VPC、ALB、ECS、RDS、Secrets、IAM）。**モジュール化**し、プロダクト複製時は変数と `tfvars` のみ差し替え可能にする。
- **現状**: 本リポジトリには **ワークフロー・Terraform の実体は未同梱**の場合がある → プロダクト化時に追加する。

## Consequences

- Positive: 環境間の差分が `plan` で見える。デプロイが自動化される。
- Negative: Terraform の状態ファイル管理（S3 + DynamoDB ロック）の初期セットアップが必要。

## Alternatives Considered

- **AWS CDK**: TypeScript で書ける利点があるが、チームスキルに合わせ Terraform をデフォルト推奨とする。
- **CodePipeline のみ GUI**: 再現性・レビュー性で IaC 併用が望ましい。

## References

- [docs/ops/ec2-new-product-release.md](../ops/ec2-new-product-release.md)（後続方針）
- [docs/aws-ecr-setup.md](../aws-ecr-setup.md)
