package com.kizunavi.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * AWS Secrets Manager から DB / JWT の機密値を読み込み、環境プロパティとして注入する。
 */
public class SecretsManagerPropertyInitializer implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecretsManagerPropertyInitializer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        boolean enabled = Boolean.parseBoolean(environment.getProperty("aws.secrets.enabled", "true"));
        if (!enabled) {
            log.info("aws.secrets.enabled=false のため Secrets Manager 取得をスキップします。");
            return;
        }

        String profile = resolveProfile(environment);
        String region = environment.getProperty("aws.secrets.region", "ap-northeast-1");
        String prefix = environment.getProperty("aws.secrets.prefix", "kizunavi");

        String databaseSecretName = prefix + "/" + profile + "/database";
        String jwtSecretName = prefix + "/" + profile + "/jwt";

        Map<String, Object> secretProperties = new LinkedHashMap<>();

        try (SecretsManagerClient client = SecretsManagerClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()) {
            Map<String, Object> databaseSecret = readSecret(client, databaseSecretName);
            String jdbcUrl = resolveJdbcUrl(databaseSecret, databaseSecretName);
            String username = requiredString(databaseSecret, "username", databaseSecretName);
            String password = requiredString(databaseSecret, "password", databaseSecretName);

            Map<String, Object> jwtSecret = readSecret(client, jwtSecretName);
            String signingSecret = requiredString(jwtSecret, "secret", jwtSecretName);

            secretProperties.put("DB_URL", jdbcUrl);
            secretProperties.put("DB_USERNAME", username);
            secretProperties.put("DB_PASSWORD", password);
            secretProperties.put("JWT_SECRET", signingSecret);
        } catch (Exception ex) {
            throw new IllegalStateException("Secrets Manager から DB/JWT シークレットの取得に失敗しました。", ex);
        }

        environment.getPropertySources()
            .addFirst(new MapPropertySource("awsSecretsManagerPropertySource", secretProperties));
        log.info("Secrets Manager から DB/JWT シークレットを読み込みました。profile={}", profile);
    }

    private String resolveProfile(ConfigurableEnvironment environment) {
        String profile = null;
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            profile = activeProfiles[0];
        }
        if (!StringUtils.hasText(profile)) {
            profile = environment.getProperty("spring.profiles.active", "dev");
            profile = profile.split(",")[0].trim();
        }

        if (!"dev".equals(profile) && !"prod".equals(profile)) {
            throw new IllegalStateException("spring.profiles.active は dev または prod のみ対応です: " + profile);
        }
        return profile;
    }

    private Map<String, Object> readSecret(SecretsManagerClient client, String secretName) throws Exception {
        GetSecretValueResponse response = client.getSecretValue(
            GetSecretValueRequest.builder().secretId(secretName).build()
        );
        String secretString = response.secretString();
        if (!StringUtils.hasText(secretString)) {
            throw new IllegalStateException("シークレットが SecretString 形式ではありません: " + secretName);
        }
        return OBJECT_MAPPER.readValue(secretString, MAP_TYPE);
    }

    private String resolveJdbcUrl(Map<String, Object> databaseSecret, String secretName) {
        String jdbcUrl = optionalString(databaseSecret, "url");
        if (StringUtils.hasText(jdbcUrl)) {
            return jdbcUrl;
        }

        String host = requiredString(databaseSecret, "host", secretName);
        String port = requiredString(databaseSecret, "port", secretName);
        String dbName = requiredString(databaseSecret, "dbname", secretName);
        return String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, dbName);
    }

    private String requiredString(Map<String, Object> values, String key, String secretName) {
        String value = optionalString(values, key);
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("シークレットに必須キーがありません: " + secretName + " / " + key);
        }
        return value;
    }

    private String optionalString(Map<String, Object> values, String key) {
        Object value = values.get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value).trim();
    }
}
