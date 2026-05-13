package com.kizunavi.repository;

import com.kizunavi.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link LoginAttempt} の永続化と照会。
 */
@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
}
