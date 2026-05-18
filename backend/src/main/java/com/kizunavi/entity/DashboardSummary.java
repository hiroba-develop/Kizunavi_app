package com.kizunavi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * ダッシュボードテーブル: 1サーベイ1レコードの集計結果
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "dashboard_summaries", indexes = { @Index(name = "idx_dashboard_summaries_customer_id", columnList = "customer_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummary {


    /** サーベイID (PK, 概念FK→SURVEY_DETAILS.survey_id) */
    @Id
    @Column(name = "survey_id", length = 36)
    private String surveyId;

    /** 顧客ID (概念FK→CUSTOMERS.customer_id, 検索高速化用) */
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    /** コンディション (1:良好 2:普通 3:要注意) */
    @Column(name = "condition")
    private Integer conditionCode;

    /** トータルキズナスコア (アラートチェック達成割合%) */
    @Column(name = "total_kizuna_score")
    private BigDecimal totalKizunaScore;

    /** 対個人間キズナスコア (個人間U/A/T/P/C合計の正規化) */
    @Column(name = "kizuna_score")
    private BigDecimal kizunaScore;

    /** 直近キズナ認識変化 (CHスコア集計, プラス=改善) */
    @Column(name = "kizuna_change_score")
    private BigDecimal kizunaChangeScore;

    /** 役割期待値スコア (EXスコアを正規化) */
    @Column(name = "role_expectation_score")
    private BigDecimal roleExpectationScore;

    /** 従業員エンゲージメントスコア (O1〜O6を正規化) */
    @Column(name = "engagement_score")
    private BigDecimal engagementScore;

    /** 組織温度スコア (CLスコアを正規化) */
    @Column(name = "climate_score")
    private BigDecimal climateScore;

    /** 回答正確性スコア (Mスコアを正規化) */
    @Column(name = "accuracy_score")
    private BigDecimal accuracyScore;

    /** アラート上位10件 (JSON文字列) */
    @Column(name = "alert_top10", length = 2000)
    private String alertTop10;

    /** 集計日時 (バックエンドが集計・保存した日時) */
    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    /** 更新日時 (再集計時に更新) */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
