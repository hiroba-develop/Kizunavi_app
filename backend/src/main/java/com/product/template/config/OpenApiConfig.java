package com.product.template.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc / OpenAPI（Swagger UI）のメタデータおよび Bearer JWT セキュリティ定義。
 *
 * <p>API ドキュメント上のタイトル、説明、サーバー URL、認証スキームを宣言する。</p>
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Product Template API",
        version = "1.0.0",
        description = "Spring Boot + React テンプレートプロジェクトのREST API",
        contact = @Contact(
            name = "Product Template Team",
            email = "support@example.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "開発環境"),
        @Server(url = "https://api.example.com", description = "本番環境")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT認証トークン"
)
public class OpenApiConfig {
}
