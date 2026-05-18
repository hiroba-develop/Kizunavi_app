package com.kizunavi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 利用企業テーブル: テナントとなる利用企業の基本情報・契約情報
 *
 * <p>このファイルは {@code kizunavi_ddl.sql} から自動生成されています。手編集しないでください。
 * 再生成: {@code ./gradlew generateEntities copyGeneratedEntities}</p>
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {


    /** 顧客ID (UUID, 全テーブルのテナントキー) */
    @Id
    @Column(name = "customer_id", length = 36)
    private String customerId;

    /** 顧客名 (利用企業名) */
    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    /** 顧客名カナ */
    @Column(name = "customer_name_kana", length = 255)
    private String customerNameKana;

    /** 従業員数 */
    @Column(name = "employee_cnt")
    private Integer employeeCnt;

    /** 業界コード (アプリ側固定コードで管理) */
    @Column(name = "industry")
    private Integer industry;

    /** 郵便番号 (ハイフン含む xxx-xxxx 形式) */
    @Column(name = "postal_num", length = 8)
    private String postalNum;

    /** 住所 */
    @Column(name = "address", length = 255)
    private String address;

    /** 電話番号 */
    @Column(name = "tel_num", length = 20)
    private String telNum;

    /** 利用企業の連絡先メール */
    @Column(name = "mail", length = 255)
    private String mail;

    /** 削除フラグ (0:有効 1:削除) */
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
