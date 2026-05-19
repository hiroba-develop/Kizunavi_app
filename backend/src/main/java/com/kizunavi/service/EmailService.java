package com.kizunavi.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

/**
 * Amazon SES を用いた HTML メール送信（ウェルカム・パスワードリセット等）。
 */
@Service
@Slf4j
public class EmailService {

  private final Optional<SesClient> sesClient;
  private final String fromEmail;
  private final boolean enabled;
  private final String frontendBaseUrl;

  /**
   * 送信設定を保持する。
   *
   * @param sesClient SES クライアント（{@code aws.ses.enabled=false} のとき空）
   * @param fromEmail 送信元アドレス（SES で検証済みであること）
   * @param enabled {@code false} の場合送信は行わずログのみ
   * @param frontendBaseUrl フロントエンドのベース URL
   */
  public EmailService(
      Optional<SesClient> sesClient,
      @Value("${aws.ses.from-email:noreply@example.com}") String fromEmail,
      @Value("${aws.ses.enabled:true}") boolean enabled,
      @Value("${app.frontend-base-url:http://localhost:5175}") String frontendBaseUrl) {
    this.sesClient = sesClient;
    this.fromEmail = fromEmail;
    this.enabled = enabled;
    this.frontendBaseUrl = frontendBaseUrl.replaceAll("/$", "");
    if (enabled && sesClient.isEmpty()) {
      log.warn("aws.ses.enabled=true ですが SesClient Bean がありません。メールは送信されません。");
    }
  }

  /**
   * 単一宛先へ HTML メールを送信する。
   *
   * @param to 宛先メールアドレス
   * @param subject 件名
   * @param htmlBody HTML 本文
   * @param textBody プレーンテキスト本文（HTML 非対応クライアント向け）
   * @throws RuntimeException SES 送信に失敗した場合
   */
  public void sendEmail(String to, String subject, String htmlBody, String textBody) {
    if (!enabled) {
      log.info("Email service disabled. Would have sent email to: {}, subject: {}", to, subject);
      return;
    }

    SesClient client = sesClient.orElse(null);
    if (client == null) {
      log.warn("SES client not available. Cannot send email to: {}", to);
      return;
    }

    try {
      SendEmailRequest request =
          SendEmailRequest.builder()
              .destination(Destination.builder().toAddresses(to).build())
              .message(
                  Message.builder()
                      .subject(
                          Content.builder().charset("UTF-8").data(subject).build())
                      .body(
                          Body.builder()
                              .html(
                                  Content.builder().charset("UTF-8").data(htmlBody).build())
                              .text(
                                  Content.builder().charset("UTF-8").data(textBody).build())
                              .build())
                      .build())
              .source(fromEmail)
              .build();

      SendEmailResponse response = client.sendEmail(request);
      log.info("Email sent successfully to {}. MessageId: {}", to, response.messageId());
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
    String textBody =
        """
        %s 様

        アカウント登録が完了しました。
        ご利用いただきありがとうございます。
        """
            .formatted(name);
    String htmlBody =
        """
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
        """
            .formatted(name);

    sendEmail(to, subject, htmlBody, textBody);
  }

  /**
   * パスワードリセット用リンクを含むメールを送信する。
   *
   * @param to 宛先メールアドレス
   * @param resetToken リセット用トークン（クエリパラメータに付与）
   */
  public void sendPasswordResetEmail(String to, String resetToken) {
    String resetUrl = "%s/reset-password?token=%s".formatted(frontendBaseUrl, resetToken);
    String subject = "パスワードリセットのご案内";
    String textBody =
        """
        パスワードリセット

        以下の URL をブラウザで開き、パスワードを再設定してください。
        %s

        このリンクは24時間有効です。
        """
            .formatted(resetUrl);
    String htmlBody =
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
        </head>
        <body>
            <h1>パスワードリセット</h1>
            <p>以下のリンクをクリックしてパスワードをリセットしてください。</p>
            <p><a href="%s">パスワードをリセット</a></p>
            <p>このリンクは24時間有効です。</p>
        </body>
        </html>
        """
            .formatted(resetUrl);

    sendEmail(to, subject, htmlBody, textBody);
  }
}
