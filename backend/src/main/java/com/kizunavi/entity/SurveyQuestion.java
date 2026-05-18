package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * サーベイ設問テーブル: 設問定義マスタ。SURVEY_QUESTION_MAPPINGSで割当管理
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "survey_questions", indexes = { @Index(name = "idx_survey_questions_customer_id", columnList = "customer_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestion {


    /** 設問ID (例: Q_CEO_001) */
    @Id
    @Column(name = "question_id", length = 50)
    private String questionId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** 回答者役職レベル (1〜5, NULL=全役職共通) */
    @Column(name = "respondent_kizuna_level")
    private Integer respondentKizunaLevel;

    /** 設問番号 (企業内での管理番号) */
    @Column(name = "question_no", nullable = false)
    private Integer questionNo;

    /** 設問文言 */
    @Column(name = "question_text", nullable = false, length = 4000)
    private String questionText;

    /** 対象役職レベル (1〜5, NULL=役職以外が対象) */
    @Column(name = "target_kizuna_level")
    private Integer targetKizunaLevel;

    /** 対象部門種別 (1:会社 2:経営陣 3:部署 4:課) */
    @Column(name = "target_dept_type")
    private Integer targetDeptType;

    /** 指標コード (U/A/T/P/C/O1〜O6/CL/CH/EX/M/PH/V1/V2/S1/S2) */
    @Column(name = "element_code", nullable = false, length = 10)
    private String elementCode;

    /** スコア方向 (F=順方向 / R=逆方向 / NULL=フェーズ評価) */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "score_direction", length = 1)
    private String scoreDirection;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
