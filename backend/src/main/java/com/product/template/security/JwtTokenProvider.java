package com.product.template.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.product.template.config.JwtConfig;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JJWT を用いた JWT の生成・パース・検証を担当する。
 */
@Component
@Slf4j
public class JwtTokenProvider {

    /** 有効期限などの設定。 */
    private final JwtConfig jwtConfig;
    /** 署名・検証に用いる HMAC 秘密鍵。 */
    private final SecretKey secretKey;

    /**
     * 設定から秘密鍵を構築する。
     *
     * @param jwtConfig JWT 設定（秘密文字列および有効期限）
     */
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            java.util.Base64.getEncoder().encodeToString(jwtConfig.getSecret().getBytes())
        ));
    }

    /**
     * {@link Authentication} のプリンシパル名からアクセストークンを生成する。
     *
     * @param authentication 認証済みオブジェクト
     * @return JWT アクセストークン
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateAccessToken(userDetails.getUsername());
    }

    /**
     * ユーザー名（メール）を主題にしたアクセストークンを生成する。
     *
     * @param username 主題（subject）に設定する識別子
     * @return JWT アクセストークン
     */
    public String generateAccessToken(String username) {
        return buildToken(username, jwtConfig.getAccessTokenExpiration(), new HashMap<>());
    }

    /**
     * リフレッシュトークンを生成する（有効期限は設定のリフレッシュ用値）。
     *
     * @param username 主題（subject）に設定する識別子
     * @return JWT リフレッシュトークン
     */
    public String generateRefreshToken(String username) {
        return buildToken(username, jwtConfig.getRefreshTokenExpiration(), new HashMap<>());
    }

    /**
     * 指定された有効期限と追加クレームで JWT を組み立てる。
     *
     * @param username 主題（subject）
     * @param expiration 有効期限（ミリ秒、現在時刻からの相対）
     * @param extraClaims 追加クレーム（現状は空マップ）
     * @return コンパクトシリアライズされた JWT
     */
    private String buildToken(String username, long expiration, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .claims(extraClaims)
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    /**
     * トークン主題（通常はメールアドレス）を取得する。
     *
     * @param token JWT
     * @return subject の文字列
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * トークンの失効日時を取得する。
     *
     * @param token JWT
     * @return 失効 {@link Date}
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * パース済みクレームに対して任意の抽出関数を適用する。
     *
     * @param token JWT
     * @param claimsResolver クレームから値を取り出す関数
     * @param <T> 戻り値の型
     * @return 抽出結果
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 署名検証付きでトークンをパースし、ペイロード {@link Claims} を返す。
     *
     * @param token JWT
     * @return 検証済みクレーム
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * トークンの主題がユーザー詳細のユーザー名と一致し、かつ期限切れでないか判定する。
     *
     * @param token JWT
     * @param userDetails 比較対象のユーザー詳細
     * @return 有効とみなせる場合 {@code true}
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * 現在時刻に対してトークンが期限切れかどうかを判定する。
     *
     * @param token JWT
     * @return 期限切れの場合 {@code true}
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 署名および形式の観点でトークンが解釈可能かを検証する（期限は {@link #isTokenExpired} で別途確認）。
     *
     * @param token JWT
     * @return パースに成功した場合 {@code true}、改竄・形式不正・期限切れ等で失敗した場合 {@code false}
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}
