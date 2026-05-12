package com.product.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Product Template アプリケーションのエントリポイント。
 *
 * <p>Spring Boot による REST API および関連コンポーネントを起動する。</p>
 */
@SpringBootApplication
public class ProductTemplateApplication {

    /**
     * アプリケーションを起動する。
     *
     * @param args コマンドライン引数（Spring Boot にそのまま渡される）
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductTemplateApplication.class, args);
    }
}
