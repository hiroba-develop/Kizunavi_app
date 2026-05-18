package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * サーベイ詳細テーブル: 診断回ごとの設定・回答期限を管理
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "survey_details", indexes = { @Index(name = "idx_survey_details_customer_id", columnList = "customer_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDetail {


    /** サーベイID (UUID) */
    @Id
    @Column(name = "survey_id", length = 36)
    private String surveyId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** サーベイ名 (例: 2025年第1回キズナ診断) */
    @Column(name = "survey_name", length = 255)
    private String surveyName;

    /** サーベイ説明文 */
    @Column(name = "survey_description", length = 1000)
    private String surveyDescription;

    /** 回答期限 */
    @Column(name = "answer_deadline")
    private LocalDate answerDeadline;

    /** 作成者ID (概念FK→EMPLOYEES.employee_id) */
    @Column(name = "created_by", length = 36)
    private String createdBy;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
