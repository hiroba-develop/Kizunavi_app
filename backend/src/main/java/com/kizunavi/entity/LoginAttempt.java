package com.kizunavi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ログイン試行の監査ログ。存在しないメール等は {@code user} が null となり得る。
 * {@link User} が削除された場合は、このテーブルからも自動的に削除される。
 */
@Entity
@Table(
    name = "login_attempts",
    
    indexes = {
        @Index(name = "idx_login_attempts_email_attempted", columnList = "email, attempted_at"),
        @Index(name = "idx_login_attempts_attempted_at", columnList = "attempted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private boolean succeeded;

    @Column(name = "failure_reason", length = 30)
    private String failureReason;

    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;
}
