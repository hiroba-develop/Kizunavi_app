package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * アラートルールマスタ: ダッシュボードのアラートチェック32項目をDB管理
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "alert_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRule {


    /** ルールNo (1〜32, PK) */
    @Id
    @Column(name = "rule_no")
    private Integer ruleNo;

    /** カテゴリ (従業員エンゲージメント / キズナ度 / 組織温度 等) */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /** 項目名 */
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    /** コメント (プレースホルダー含む文言) */
    @Lob
    @Column(name = "alert_comment", nullable = false)
    private String alertComment;

    /** 重要度 (High / Middle / Low) */
    @Column(name = "importance", nullable = false, length = 10)
    private String importance;

    /** 優先順位 (1が最高優先) */
    @Column(name = "priority_order", nullable = false)
    private Integer priorityOrder;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
