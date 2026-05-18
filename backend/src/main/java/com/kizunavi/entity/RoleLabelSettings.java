package com.kizunavi.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * 役職名称設定テーブル: 企業ごとの役職表示名 (レコードなし=デフォルト名)
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "role_label_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleLabelSettings {

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RoleLabelSettingsId implements Serializable {

            @Column(name = "customer_id", length = 36)
        private String customerId;

            @Column(name = "kizuna_level")
        private Integer kizunaLevel;
    }

    @EmbeddedId
    private RoleLabelSettingsId id;


    /** 部ID (概念FK→DIVISIONS.division_id, 経営陣はNULL, 将来拡張用) */
    @Column(name = "division_id", length = 36)
    private String divisionId;

    /** 課ID (概念FK→SECTIONS.section_id, 部長以上はNULL, 将来拡張用) */
    @Column(name = "section_id", length = 36)
    private String sectionId;

    /** 表示名 (例: CEO、取締役、GM) */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /** 作成日時 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** 削除フラグ (0:有効 1:削除) */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "del_flg", length = 1)
    private String delFlg;
}
