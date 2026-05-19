package com.kizunavi.auth;

import com.kizunavi.entity.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 初回パスワード設定（{@code PUT /api/auth/firstlogin}）向けの判定・初期値。
 *
 * <p>仮パスワード発行時は {@link #PENDING_PASSWORD_CHANGE_AT} を {@code last_password_changed_at} に設定する。
 * 本パスワード設定完了後は実時刻へ更新し、パスワード再設定フローとの混同を防ぐ。</p>
 */
public final class FirstLoginPasswordSupport {

    /**
     * 初回パスワード変更待ちを示すセンチネル（{@code last_password_changed_at}）。
     * 従業員登録で仮パスワードを発行したユーザーにのみ設定する。
     */
    public static final LocalDateTime PENDING_PASSWORD_CHANGE_AT =
        LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    private FirstLoginPasswordSupport() {}

    /**
     * 初回パスワード設定 API の対象ユーザーかどうか。
     *
     * @param user 判定対象
     * @return 仮パスワード変更待ちなら {@code true}
     */
    public static boolean requiresFirstLoginPasswordChange(User user) {
        if (user == null || user.getLastPasswordChangedAt() == null) {
            return false;
        }
        return user.getLastPasswordChangedAt()
            .truncatedTo(ChronoUnit.MICROS)
            .equals(PENDING_PASSWORD_CHANGE_AT);
    }

    /**
     * 新規ユーザー（仮パスワード）作成時に設定する {@code last_password_changed_at}。
     *
     * @return センチネル値
     */
    public static LocalDateTime pendingPasswordChangeTimestamp() {
        return PENDING_PASSWORD_CHANGE_AT;
    }
}
