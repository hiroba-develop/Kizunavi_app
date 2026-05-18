package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * 部テーブル: 企業ごとの部を管理
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "divisions", indexes = { @Index(name = "idx_divisions_customer_id", columnList = "customer_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Division {


    /** 部ID (UUID) */
    @Id
    @Column(name = "division_id", length = 36)
    private String divisionId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** 部の名称 (例: 営業部、開発部) */
    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    /** 削除フラグ (0:有効 1:削除) */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "del_flg", length = 1)
    private String delFlg;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
