package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * ログイン試行ログテーブル: 成功・失敗の監査ログ
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "login_attempts", indexes = { @Index(name = "idx_login_attempts_email_attempted", columnList = "email, attempted_at"), @Index(name = "idx_login_attempts_attempted_at", columnList = "attempted_at") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttempt {


    /** ユーザーID (FK→USERS, ON DELETE SET NULL, NULL=存在しないメールでの試行) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** 試行ID (IDENTITY) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    /** メールアドレス (入力された値をそのまま保存) */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** 成功フラグ (1:成功 0:失敗) */
    @Column(name = "succeeded", nullable = false)
    private boolean succeeded;

    /** 失敗理由 (成功時はNULL) */
    @Column(name = "failure_reason", length = 30)
    private String failureReason;

    /** IPアドレス (IPv4/IPv6両対応) */
    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    /** 試行日時 */
    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;
}
