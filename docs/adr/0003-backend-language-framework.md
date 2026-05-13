# ADR-0003: バックエンド言語・フレームワーク（Java 21 / Spring Boot）

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

エンタープライズ向け REST API の土台として、言語とフレームワークを選定する必要がある。

## Decision

- **Java 21**（LTS）を採用する。ツールチェーンは [`backend/build.gradle`](../../backend/build.gradle)、コンテナは Temurin 21 系（[`backend/Dockerfile`](../../backend/Dockerfile)）。
- **Spring Boot 3.4.x** を採用する（`org.springframework.boot` プラグインでバージョン管理）。
- Web 層は **Spring Web MVC**、永続化は **Spring Data JPA**、セキュリティは **Spring Security** を利用する。

## Consequences

- Positive: エコシステム・ドキュメント・採用実績が豊富。AWS との親和性が高い。
- Negative: JVM メモリ・起動時間は軽量ランタイムより大きめ。コンテナのリソース下限設計が必要。

## Alternatives Considered

- **Kotlin + Spring**: 記述量は減るが、テンプレート利用者の Kotlin 前提が強い → 汎用テンプレートでは Java を優先。
- **Node.js (NestJS 等)**: フロントと同一言語だが、本テンプレートは JVM 系バックエンドを想定 → 別系統として却下。

## References

- [`backend/build.gradle`](../../backend/build.gradle)
- [`backend/src/main/java/com/kizunavi/KizuNaviApplication.java`](../../backend/src/main/java/com/kizunavi/KizuNaviApplication.java)
