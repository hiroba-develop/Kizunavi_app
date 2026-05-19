package com.kizunavi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * Amazon SES クライアントの Spring Bean 定義。
 *
 * <p>アクセスキーが未設定またはプレースホルダの場合は、AWS CLI プロファイルや
 * EC2 インスタンスプロファイル等のデフォルト認証チェーンを利用する。</p>
 */
@Configuration
@Slf4j
public class SesConfig {

  /**
   * SES 送信用クライアント。{@code aws.ses.enabled=false} のときは Bean を登録しない。
   *
   * @param region AWS リージョン
   * @param accessKey 静的アクセスキー（任意）
   * @param secretKey 静的シークレットキー（任意）
   * @return クローズ可能な {@link SesClient}
   */
  @Bean(destroyMethod = "close")
  @ConditionalOnProperty(name = "aws.ses.enabled", havingValue = "true", matchIfMissing = true)
  public SesClient sesClient(
      @Value("${aws.ses.region:ap-northeast-1}") String region,
      @Value("${aws.ses.access-key:}") String accessKey,
      @Value("${aws.ses.secret-key:}") String secretKey) {
    var builder = SesClient.builder().region(Region.of(region));
    if (hasUsableStaticCredentials(accessKey, secretKey)) {
      builder.credentialsProvider(
          StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)));
      log.info("AWS SES client: using static credentials from configuration");
    } else {
      builder.credentialsProvider(DefaultCredentialsProvider.create());
      log.info("AWS SES client: using default credentials provider chain");
    }
    return builder.build();
  }

  /**
   * 設定値が実際の静的認証情報として使えるかを判定する。
   *
   * @param accessKey アクセスキー
   * @param secretKey シークレットキー
   * @return 両方が非空かつプレースホルダでない場合 {@code true}
   */
  static boolean hasUsableStaticCredentials(String accessKey, String secretKey) {
    if (!StringUtils.hasText(accessKey) || !StringUtils.hasText(secretKey)) {
      return false;
    }
    String normalizedAccess = accessKey.trim();
    String normalizedSecret = secretKey.trim();
    return !normalizedAccess.startsWith("your-")
        && !normalizedSecret.startsWith("your-")
        && !"your-aws-access-key".equals(normalizedAccess)
        && !"your-aws-secret-key".equals(normalizedSecret);
  }
}
