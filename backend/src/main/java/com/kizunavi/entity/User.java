package com.kizunavi.entity;

import com.kizunavi.dto.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 認証ユーザーテーブル: ログイン認証情報・アカウント状態を管理
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "uk_users_email", columnNames = { "email" }), @UniqueConstraint(name = "uk_users_employee_id", columnNames = { "employee_id" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    /** 従業員ID (FK→EMPLOYEES, 1:1, NULL=管理者アカウント等) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /** 顧客ID (FK→CUSTOMERS, テナント特定用) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /** ユーザーID (IDENTITY) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** メールアドレス (ログインID, 一意) */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** パスワードハッシュ (BCrypt 72文字固定) */
    @Column(name = "password_hash", nullable = false, length = 72)
    private String passwordHash;

    /** 表示名 (人事氏名としても利用) */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** ロール (ROLE_USER / ROLE_ADMIN) */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    /** 有効フラグ (1:有効 0:無効) */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /** 連続ログイン失敗回数 */
    @Column(name = "failed_login_count", nullable = false)
    private Integer failedLoginCount;

    /** ロック解除時刻 (NULL=ロックなし) */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    /** 最終ログイン日時 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /** 最終パスワード変更日時 */
    @Column(name = "last_password_changed_at", nullable = false)
    private LocalDateTime lastPasswordChangedAt;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
