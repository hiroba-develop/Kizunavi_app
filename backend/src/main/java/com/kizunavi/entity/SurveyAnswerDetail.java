package com.kizunavi.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * サーベイ回答明細テーブル: 設問ごとの回答値を格納
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "survey_answer_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyAnswerDetail {

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class SurveyAnswerDetailId implements Serializable {

            @Column(name = "survey_answer_id", length = 36)
        private String surveyAnswerId;

            @Column(name = "question_id", length = 50)
        private String questionId;
    }

    @EmbeddedId
    private SurveyAnswerDetailId id;


    /** 回答値 (1〜7, フェーズ評価はフェーズ番号1〜7) */
    @Column(name = "answer_value")
    private Integer answerValue;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
