package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * パスワードリセットトークンテーブル: ワンタイムトークン (SHA-256ハッシュ保存)
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "password_reset_tokens", indexes = { @Index(name = "idx_password_reset_tokens_user_expires", columnList = "user_id, expires_at") }, uniqueConstraints = @UniqueConstraint(name = "uk_password_reset_tokens_token_hash", columnNames = { "token_hash" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {


    /** ユーザーID (FK→USERS, ON DELETE CASCADE) */
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

    /** 失効日時 */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** 使用日時 (NULL=未使用) */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /** 作成日時 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** リクエスト元IP (監査用) */
    @Column(name = "requester_ip", length = 64)
    private String requesterIp;
}
