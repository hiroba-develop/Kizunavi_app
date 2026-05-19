package com.kizunavi.repository;

import com.kizunavi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link User} エンティティの永続化および照会を行う Spring Data JPA リポジトリ。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * メールアドレスでユーザーを検索する。
     *
     * @param email メールアドレス（ログイン ID）
     * @return 該当ユーザー、存在しなければ空の {@link Optional}
     */
    Optional<User> findByEmail(String email);

    /**
     * メールアドレスでユーザーを検索し、紐づく従業員・利用企業を同時に取得する。
     *
     * @param email メールアドレス（ログイン ID）
     * @return 該当ユーザー、存在しなければ空の {@link Optional}
     */
    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.employee
        LEFT JOIN FETCH u.customer
        WHERE u.email = :email
        """)
    Optional<User> findByEmailWithEmployeeAndCustomer(@Param("email") String email);

    /**
     * 指定メールアドレスのユーザーが既に存在するか判定する。
     *
     * @param email メールアドレス
     * @return 存在する場合 {@code true}
     */
    boolean existsByEmail(String email);

}
