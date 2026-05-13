# ADR-0007: バックエンド補助ライブラリ（Lombok / Validation / Actuator / SES）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

ボイラープレート削減、入力検証、運用監視、メール送信のために利用するライブラリ群を整理する。

## Decision

| 用途 | 採用 | 備考 |
|------|------|------|
| ボイラープレート | **Lombok** | `compileOnly` + `annotationProcessor` |
| 入力検証 | **spring-boot-starter-validation** | Jakarta Bean Validation |
| ヘルスチェック | **spring-boot-starter-actuator** | `/actuator/health`（Docker / ALB ヘルスに利用） |
| メール | **AWS SDK v2 SES** | **`dev` / `prod` いずれも SES を有効化**（[`application-dev.yml`](../../backend/src/main/resources/application-dev.yml) 等）。認証情報は `.env` または EC2 インスタンスプロファイル / Secrets 経由（[`EmailService`](../../backend/src/main/java/com/kizunavi/service/EmailService.java)） |
| OpenAPI Nullable | **jackson-databind-nullable** | 生成コード向け |

## Consequences

- Positive: 実装が簡潔になり、コンテナ・ロードバランサのヘルスチェックと整合しやすい。
- Negative: Lombok は IDE プラグイン前提。SES は **キー方式**の例が残るため、本番は **EC2 インスタンスプロファイル（IAM ロール）** 推奨へ移行を検討。

## Alternatives Considered

- **SendGrid / SMTP 直接**: AWS 前提のテンプレートでは SES を優先。
- **Micrometer 詳細メトリクス**: 現状は Actuator 最小公開。必要に応じて拡張。

## References

- [`backend/build.gradle`](../../backend/build.gradle)
- [`docs/aws-ses-setup.md`](../aws-ses-setup.md)
