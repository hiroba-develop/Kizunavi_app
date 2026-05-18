package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * サーベイ回答セッションテーブル: SURVEY_ANSWER_DETAILSの親テーブル
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "survey_answer_sessions", indexes = { @Index(name = "idx_survey_answer_sessions_survey_id", columnList = "survey_id"), @Index(name = "idx_survey_answer_sessions_employee_id", columnList = "employee_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyAnswerSession {


    /** サーベイ回答ID (UUID) */
    @Id
    @Column(name = "survey_answer_id", length = 36)
    private String surveyAnswerId;

    /** サーベイID (概念FK→SURVEY_DETAILS.survey_id) */
    @Column(name = "survey_id", nullable = false, length = 36)
    private String surveyId;

    /** 従業員ID (概念FK→EMPLOYEES.employee_id) */
    @Column(name = "employee_id", nullable = false, length = 36)
    private String employeeId;

    /** 回答完了日時 (NULL=未完了) */
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
