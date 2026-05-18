package com.kizunavi.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 価値観分布テーブル: 従業員ごとの価値観・スタイル・キズナスコア・フェーズスコア
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "value_distributions", indexes = { @Index(name = "idx_value_distributions_customer_id", columnList = "customer_id"), @Index(name = "idx_value_distributions_employee_id", columnList = "employee_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValueDistribution {

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ValueDistributionId implements Serializable {

            @Column(name = "survey_id", length = 36)
        private String surveyId;

            @Column(name = "employee_id", length = 36)
        private String employeeId;
    }

    @EmbeddedId
    private ValueDistributionId id;


    /** 顧客ID (概念FK→CUSTOMERS.customer_id, 検索高速化用) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** 長期/短期スコア (V1: + 長期 / - 短期) */
    @Column(name = "score_long_short")
    private BigDecimal scoreLongShort;

    /** 探索/活用スコア (V2: + 探索 / - 活用) */
    @Column(name = "score_explore_exploit")
    private BigDecimal scoreExploreExploit;

    /** 主張/傾聴スコア (S1: + 主張 / - 傾聴) */
    @Column(name = "score_assert_listen")
    private BigDecimal scoreAssertListen;

    /** 表現/抑制スコア (S2: + 表現 / - 抑制) */
    @Column(name = "score_express_suppress")
    private BigDecimal scoreExpressSuppress;

    /** 対人キズナスコア (0〜100点) */
    @Column(name = "kizuna_human_score")
    private BigDecimal kizunaHumanScore;

    /** 対組織キズナスコア (0〜100点) */
    @Column(name = "kizuna_org_score")
    private BigDecimal kizunaOrgScore;

    /** フェーズスコア (1〜7) */
    @Column(name = "phase_score")
    private BigDecimal phaseScore;

    /** 集計日時 */
    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    /** 更新日時 (再集計時に更新) */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
