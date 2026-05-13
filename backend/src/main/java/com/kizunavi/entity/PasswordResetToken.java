package com.kizunavi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * パスワード再設定用ワンタイムトークン。平文は保持せず SHA-256 ハッシュのみを格納する。
 */
@Entity
@Table(
    name = "password_reset_tokens",
    
    indexes = @Index(name = "idx_password_reset_tokens_user_expires", columnList = "user_id, expires_at"),
    uniqueConstraints = @UniqueConstraint(
        name = "uk_password_reset_token_hash",
        columnNames = { "token_hash" }
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "requester_ip", length = 64)
    private String requesterIp;
}
