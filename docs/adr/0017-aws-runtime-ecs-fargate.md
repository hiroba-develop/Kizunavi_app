# ADR-0017: AWS ランタイム（ECS on Fargate）

- Status: Deprecated（後続: [ADR-0025: AWS ランタイム（EC2 + Docker）](0025-aws-runtime-ec2-docker.md)）
- Date: 2026-04-20
- Deciders: KizuNavi チーム

> **廃止理由**: 本テンプレートの運用ランタイムは **単一 EC2 + Docker** に変更された。

## Context

EC2 上で `docker compose` を直接運用している状態から、**スケール・パッチ・デプロイ**を標準化したい。コンテナの実行基盤として候補を比較する。

## Decision

- **Amazon ECS on AWS Fargate** を推奨ランタイムとする。
- **理由の要約**:
  - **サーバー管理不要**（OS パッチ、キャパシティプランニングの一部を抽象化）。
  - **タスク定義 + サービス**でバックエンド・フロント（nginx）を分離デプロイしやすい。
  - **IAM タスクロール**で Secrets / SES / RDS へ最小権限接続しやすい。
- **EC2 起動タイプ**: コスト最適化や特殊デバイスが必要な場合のオプションとして残す（詳細は運用手順書）。

## Consequences

- Positive: EC2 SSH 運用からの脱却、デプロイの再現性向上。
- Negative: Fargate は常時課金モデル。小規模でも **ALB + 最小タスク** で固定費が発生 → コスト設計が必要。

## Alternatives Considered

- **EKS**: マルチテナント大規模には有効だが、テンプレート規模では運用コストが高い → 既定では却下。
- **App Runner**: さらに簡素だが、**nginx サイドカー + 同一 ALB でのパス分割**等の要件で柔軟性が ECS に劣る場合がある → 本テンプレートの推奨は ECS Fargate。

## References

- [docs/ops/ec2-new-product-release.md](../ops/ec2-new-product-release.md)（現行の運用手順）
- [docs/aws-secrets-manager-setup.md](../aws-secrets-manager-setup.md)
