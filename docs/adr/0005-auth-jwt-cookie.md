# ADR-0005: 認証（JWT + HttpOnly リフレッシュ Cookie）

- Status: Accepted
- Date: 2026-04-20
- Deciders: Product Template チーム

## Context

SPA からバックエンド API を呼ぶ際、**アクセストークンの格納場所**と**リフレッシュの扱い**は XSS / CSRF のトレードオフに直結する。

## Decision

- **セッションは使わず** `SessionCreationPolicy.STATELESS`（Spring Security）。
- **アクセストークン**: HTTP ヘッダー `Authorization: Bearer <JWT>`（短命）。
- **リフレッシュトークン**: **HttpOnly Cookie**（`CookieUtil`、パス `/api/auth` 等でスコープ）。レスポンス body からはマスクする方針。
- **JWT 実装**: **JJWT 0.12.x**（[`backend/build.gradle`](../../backend/build.gradle)）。
- **CORS**: 資格情報あり（`allowCredentials: true`）。許可オリジンは `CORS_ALLOWED_ORIGINS` / `app.cors.allowed-origins` で環境ごとに設定（[`CorsConfig.java`](../../backend/src/main/java/com/product/template/config/CorsConfig.java)）。
- **API の CSRF**: ステートレス API として CSRF 無効化（Cookie 利用とセットで設計・同一サイト属性の検討が必要）。

## Consequences

- Positive: XSS でアクセストークンが盗まれにくい設計（リフレッシュは HttpOnly）。モバイル以外のブラウザ SPA に馴染む。
- Negative: Cookie + クロスオリジンでは **SameSite / Secure / ドメイン** の運用が必須。本番は HTTPS と `Secure` フラグの整合が必要。

## Alternatives Considered

- **リフレッシュも localStorage**: 実装は単純だが XSS リスクが高い → 却下。
- **BFF でセッション Cookie のみ**: セキュリティは上がるが構成が重くなる → テンプレートの初期範囲外。

## References

- [`backend/src/main/java/com/product/template/security/`](../../backend/src/main/java/com/product/template/security/)
- [`backend/src/main/java/com/product/template/config/JwtConfig.java`](../../backend/src/main/java/com/product/template/config/JwtConfig.java)
