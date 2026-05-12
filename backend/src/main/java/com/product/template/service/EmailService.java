package com.product.template.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;
import software.amazon.awssdk.services.ses.model.*;

/**
 * Amazon SES を用いた HTML メール送信（ウェルカム・パスワードリセット等）。
 *
 * <p>アクセスキーが未設定の場合は AWS SDK のデフォルト認証チェーン
 * （例: EC2 インスタンスプロファイル）を利用する。</p>
 */
@Service
@Slf4j
public class EmailService {

    /** SES API クライアント。メール機能が無効時は {@code null}。 */
    private final SesClient sesClient;
    /** 送信元メールアドレス（SES で検証済みであること）。 */
    private final String fromEmail;
    /** メール機能全体の有効／無効。 */
    private final boolean enabled;
    /** パスワードリセットリンクのベース URL（末尾スラッシュは除去済み）。 */
    private final String frontendBaseUrl;

    /**
     * SES クライアントおよび送信設定を初期化する。
     *
     * @param region AWS リージョン
     * @param accessKey AWS アクセスキー（空の場合はデフォルト認証チェーンを利用）
     * @param secretKey AWS シークレットキー
     * @param fromEmail 送信元アドレス
     * @param enabled {@code false} の場合送信は行わずログのみ
     * @param frontendBaseUrl フロントエンドのベース URL
     */
    public EmailService(
        @Value("${aws.ses.region:ap-northeast-1}") String region,
        @Value("${aws.ses.access-key:}") String accessKey,
        @Value("${aws.ses.secret-key:}") String secretKey,
        @Value("${aws.ses.from-email:noreply@example.com}") String fromEmail,
        @Value("${aws.ses.enabled:true}") boolean enabled,
        @Value("${app.frontend-base-url:http://localhost:5173}") String frontendBaseUrl
    ) {
        this.fromEmail = fromEmail;
        this.enabled = enabled;
        this.frontendBaseUrl = frontendBaseUrl.replaceAll("/$", "");

        if (!enabled) {
            this.sesClient = null;
            return;
        }

        SesClientBuilder sesClientBuilder = SesClient.builder().region(Region.of(region));
        if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
            sesClientBuilder.credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
            );
        } else {
            sesClientBuilder.credentialsProvider(DefaultCredentialsProvider.create());
            log.info("AWS SES static credentials are not configured. Using default credentials provider chain.");
        }
        this.sesClient = sesClientBuilder.build();
    }

    /**
     * 単一宛先へ HTML メールを送信する。
     *
     * @param to 宛先メールアドレス
     * @param subject 件名
     * @param htmlBody HTML 本文
     * @throws RuntimeException SES 送信に失敗した場合
     */
    public void sendEmail(String to, String subject, String htmlBody) {
        if (!enabled) {
            log.info("Email service disabled. Would have sent email to: {}, subject: {}", to, subject);
            return;
        }

        if (sesClient == null) {
            log.warn("SES client not initialized. Cannot send email to: {}", to);
            return;
        }

        try {
            SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder()
                    .toAddresses(to)
                    .build())
                .message(Message.builder()
                    .subject(Content.builder()
                        .charset("UTF-8")
                        .data(subject)
                        .build())
                    .body(Body.builder()
                        .html(Content.builder()
                            .charset("UTF-8")
                            .data(htmlBody)
                            .build())
                        .build())
                    .build())
                .source(fromEmail)
                .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent successfully. MessageId: {}", response.messageId());
        } catch (SesException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("メール送信に失敗しました", e);
        }
    }

    /**
     * 新規登録完了を通知するウェルカムメールを送信する。
     *
     * @param to 宛先メールアドレス
     * @param name ユーザー表示名（本文に差し込む）
     */
    public void sendWelcomeEmail(String to, String name) {
        String subject = "ようこそ！アカウント登録完了のお知らせ";
        String htmlBody = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body>
                <h1>%s 様</h1>
                <p>アカウント登録が完了しました。</p>
                <p>ご利用いただきありがとうございます。</p>
            </body>
            </html>
            """, name);

        sendEmail(to, subject, htmlBody);
    }

    /**
     * パスワードリセット用リンクを含むメールを送信する。
     *
     * @param to 宛先メールアドレス
     * @param resetToken リセット用トークン（クエリパラメータに付与）
     */
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "パスワードリセットのご案内";
        String htmlBody = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body>
                <h1>パスワードリセット</h1>
                <p>以下のリンクをクリックしてパスワードをリセットしてください。</p>
                <p><a href="%s/reset-password?token=%s">パスワードをリセット</a></p>
                <p>このリンクは24時間有効です。</p>
            </body>
            </html>
            """, frontendBaseUrl, resetToken);

        sendEmail(to, subject, htmlBody);
    }
}
