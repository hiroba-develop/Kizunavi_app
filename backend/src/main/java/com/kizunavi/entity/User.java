package com.kizunavi.entity;

import com.kizunavi.dto.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * アプリケーション利用者を表す JPA エンティティ。
 *
 * <p>テーブル名は {@code users}。認証用のハッシュ済みパスワードを保持する。
 * リフレッシュトークンは {@link RefreshToken} テーブルに分離する。</p>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /** 主キー（自動採番）。 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** ログイン ID 兼ユニークなメールアドレス。 */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** BCrypt 等でハッシュ化されたパスワード。 */
    @Column(name = "password_hash", nullable = false, length = 72)
    private String passwordHash;

    /** 表示名。 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 権限（例: {@code ROLE_USER}）。 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** アカウントが有効かどうか（無効時は認証不可の想定）。 */
    @Column(nullable = false)
    private boolean enabled;

    /** 連続ログイン失敗回数。 */
    @Column(name = "failed_login_count", nullable = false)
    @Builder.Default
    private int failedLoginCount = 0;

    /** 一時ロック解除時刻。未ロックなら null。 */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    /** 最終ログイン成功時刻。 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /** パスワード最終変更時刻。 */
    @Column(name = "last_password_changed_at", nullable = false)
    private LocalDateTime lastPasswordChangedAt;

    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private long version = 0L;

    /** レコード作成日時（挿入時に自動設定）。 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** レコード最終更新日時。 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 永続化直前のデフォルト値補完。
     */
    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.ROLE_USER;
        }
        if (!enabled) {
            enabled = true;
        }
        if (lastPasswordChangedAt == null) {
            lastPasswordChangedAt = LocalDateTime.now();
        }
    }
}
