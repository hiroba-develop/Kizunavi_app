package com.kizunavi.repository;

import com.kizunavi.entity.PasswordResetToken;
import com.kizunavi.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * {@link PasswordResetToken} の永続化と照会。
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  /**
   * 未使用かつハッシュ一致のトークンを取得する。
   *
   * @param tokenHash SHA-256 hex
   * @return 該当トークン
   */
  Optional<PasswordResetToken> findByTokenHashAndUsedAtIsNull(String tokenHash);

  /**
   * 同一ユーザーの未使用トークンを失効させる（新規発行の直前に呼ぶ）。
   *
   * @param user 対象ユーザー
   * @param usedAt 失効日時（使用日時として記録）
   */
  @Modifying
  @Query(
      "UPDATE PasswordResetToken t SET t.usedAt = :usedAt "
          + "WHERE t.user = :user AND t.usedAt IS NULL")
  void invalidateUnusedByUser(@Param("user") User user, @Param("usedAt") LocalDateTime usedAt);
}
