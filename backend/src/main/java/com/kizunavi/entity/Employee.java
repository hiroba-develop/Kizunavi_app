package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * 従業員テーブル: 経営陣(kizuna_level=2)はdivision_id/section_idともNULLで運用
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "employees", indexes = { @Index(name = "idx_employees_customer_id", columnList = "customer_id"), @Index(name = "idx_employees_division_id", columnList = "division_id"), @Index(name = "idx_employees_section_id", columnList = "section_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {


    /** 従業員ID (UUID, USERSテーブルと1:1) */
    @Id
    @Column(name = "employee_id", length = 36)
    private String employeeId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** 部ID (概念FK→DIVISIONS.division_id, 経営陣はNULL) */
    @Column(name = "division_id", length = 36)
    private String divisionId;

    /** 課ID (概念FK→SECTIONS.section_id, 部長以上または課なし社員はNULL) */
    @Column(name = "section_id", length = 36)
    private String sectionId;

    /** キズナ診断役職レベル (1:社長 2:役員 3:部長 4:課長 5:社員) */
    @Column(name = "kizuna_level")
    private Integer kizunaLevel;

    /** 入社年月日 */
    @Column(name = "hire_date")
    private LocalDate hireDate;

    /** 削除フラグ (0:有効 1:削除, 退職者用) */
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
