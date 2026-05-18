package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 課テーブル: 企業ごとの課を管理。division_idで親の部と紐付く
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "sections", indexes = { @Index(name = "idx_sections_customer_id", columnList = "customer_id"), @Index(name = "idx_sections_division_id", columnList = "division_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {


    /** 課ID (UUID) */
    @Id
    @Column(name = "section_id", length = 36)
    private String sectionId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** 部ID (概念FK→DIVISIONS.division_id) */
    @Column(name = "division_id", nullable = false, length = 36)
    private String divisionId;

    /** 課の名称 (例: 第一営業課、第二開発課) */
    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    /** 削除フラグ (0:有効 1:削除) */
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
