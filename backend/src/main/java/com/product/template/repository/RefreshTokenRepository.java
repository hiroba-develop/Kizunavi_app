package com.product.template.repository;

import com.product.template.entity.RefreshToken;
import com.product.template.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * {@link RefreshToken} の永続化と照会。
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHashAndRevokedAtIsNull(String tokenHash);

    List<RefreshToken> findByUserAndRevokedAtIsNull(User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RefreshToken r set r.revokedAt = :revokedAt where r.user = :user and r.revokedAt is null")
    int revokeAllByUser(@Param("user") User user, @Param("revokedAt") LocalDateTime revokedAt);
}
