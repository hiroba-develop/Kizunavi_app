package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * サーベイメール送信ログ: 案内メールの送信履歴
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "survey_mail_logs", indexes = { @Index(name = "idx_survey_mail_logs_survey_id", columnList = "survey_id"), @Index(name = "idx_survey_mail_logs_employee_id", columnList = "employee_id"), @Index(name = "idx_survey_mail_logs_customer_id", columnList = "customer_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyMailLog {


    /** メールログID (UUID) */
    @Id
    @Column(name = "mail_log_id", length = 36)
    private String mailLogId;

    /** サーベイID (概念FK→SURVEY_DETAILS.survey_id) */
    @Column(name = "survey_id", nullable = false, length = 36)
    private String surveyId;

    /** 従業員ID (概念FK→EMPLOYEES.employee_id) */
    @Column(name = "employee_id", nullable = false, length = 36)
    private String employeeId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id, 検索用) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** メール種別 (INVITE=案内 / REMIND=リマインド / CLOSE=締切通知) */
    @Column(name = "mail_type", nullable = false, length = 20)
    private String mailType;

    /** 送信先メールアドレス (送信時点のスナップショット) */
    @Column(name = "sent_to", nullable = false, length = 255)
    private String sentTo;

    /** 送信日時 */
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    /** 送信ステータス (SUCCESS / FAILED / BOUNCED) */
    @Column(name = "status", nullable = false, length = 10)
    private String status;

    /** エラーメッセージ (status=FAILED時) */
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    /** 作成日時 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
