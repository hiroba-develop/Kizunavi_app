# ADR-0021: 可観測性（CloudWatch / EC2 + Docker）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

EC2 上の Docker コンテナで障害検知と原因調査を行うため、ログ・メトリクス・トレースの最低ラインを決める。

## Decision

- **ログ**: Docker の **awslogs ロギングドライバ**（または **CloudWatch Agent**）で **CloudWatch Logs** に集約。
- **メトリクス**: **EC2 の標準メトリクス**（CloudWatch、必要に応じて **詳細モニタリング**）と、ALB / RDS のメトリクスを併用。
- **アラーム**: ALB の 5xx、ターゲット不健全、RDS の空き容量・接続数、EC2 の StatusCheckFailed など、**Runbook とセット**で CloudWatch Alarm を定義。
- **分散トレース**: 要件に応じて **AWS X-Ray** または OpenTelemetry 系を追加検討（テンプレート最小構成では必須としない）。

## Consequences

- Positive: 障害時の初動が速い。ALB / RDS / EC2 の観測点が揃う。
- Negative: ログ量に比例してコスト増 → 保持期間・サンプリング・DEBUG ログの本番抑制が必要。

## Alternatives Considered

- **サードパーティ APM のみ**: 強力だがコストとデータ国外保管の検討が必要 → AWS ネイティブをベースラインとする。

## References

- [`backend/src/main/resources/application.yml`](../../backend/src/main/resources/application.yml)（`logging.level`）
- [docs/ops/aws-infrastructure-best-practices.md](../ops/aws-infrastructure-best-practices.md)
