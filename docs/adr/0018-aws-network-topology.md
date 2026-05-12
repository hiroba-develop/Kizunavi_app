# ADR-0018: AWS ネットワーク（VPC / ALB / Route53 / ACM）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

インターネット向け SPA と API を安全に公開し、TLS と DNS を管理する。

## Decision

- **VPC**: 最低 **パブリックサブネット（ALB）** + **プライベートサブネット（アプリ用 EC2、RDS）** の **2 層**を推奨。
- **ALB**: 443 終端（**ACM** 証明書）。ルール例:
  - `/api/*` → バックエンドターゲットグループ（コンテナ **8080**、ヘルス `/actuator/health`）
  - デフォルト → フロント（Vite preview **5173**、パス `/`）
- **Route 53**: ホストゾーンで ALB へのエイリアス（A/AAAA）。
- **WAF（任意）**: 一般的な OWASP 対策、レート制限。コストと要件に応じて付与。

## Consequences

- Positive: TLS とルーティングを ALB に集約し、コンテナはプライベートに置ける。
- Negative: ALB は固定費。マルチ AZ 構成でサブネット設計が必要。

## Alternatives Considered

- **CloudFront 前面**: 静的アセットのキャッシュ・地理分散に有効。初期は ALB のみでも可、後から追加。

## References

- [docs/ops/aws-infrastructure-best-practices.md](../ops/aws-infrastructure-best-practices.md)
