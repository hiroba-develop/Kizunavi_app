package com.product.template.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * リフレッシュトークン。平文は保持せず SHA-256 ハッシュ（hex 64 文字）のみを格納する。
 */
@Entity
@Table(
    name = "refresh_tokens",
    
    indexes = {
        @Index(name = "idx_refresh_tokens_user_revoked", columnList = "user_id, revoked_at"),
        @Index(name = "idx_refresh_tokens_expires_at", columnList = "expires_at")
    },
    uniqueConstraints = @UniqueConstraint(
        name = "uk_refresh_tokens_token_hash",
        columnNames = { "token_hash" }
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @Column(name = "ip_address", length = 64)
    private String ipAddress;
}
