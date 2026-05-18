package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * リフレッシュトークンテーブル: デバイス単位でSHA-256ハッシュを保持。ローテーション方式
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "refresh_tokens", indexes = { @Index(name = "idx_refresh_tokens_expires_at", columnList = "expires_at"), @Index(name = "idx_refresh_tokens_user_revoked", columnList = "user_id, revoked_at") }, uniqueConstraints = @UniqueConstraint(name = "uk_refresh_tokens_token_hash", columnNames = { "token_hash" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {


    /** ユーザーID (FK→USERS.user_id, ON DELETE CASCADE) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** トークンID (IDENTITY) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    /** トークンハッシュ (SHA-256 hex文字列) */
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    /** 発行日時 */
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    /** 失効日時 */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** 明示失効日時 (NULL=有効) */
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    /** ユーザーエージェント */
    @Column(name = "user_agent", length = 512)
    private String userAgent;

    /** IPアドレス (IPv4/IPv6両対応) */
    @Column(name = "ip_address", length = 64)
    private String ipAddress;
}
