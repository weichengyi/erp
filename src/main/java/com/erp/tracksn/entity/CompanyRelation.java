package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公司关联关系实体
 */
@Data
@Entity
@Table(name = "company_relation")
public class CompanyRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long relatedCompanyId;

    @Column(length = 50)
    private String relationType;
    // PARENT_SUBSIDIARY 母子公司
    // SIBLING 兄弟公司
    // RELATED_HOLDING 关联控股
    // COOPERATION 合作联营
    // SAME_LEGAL 同一法人
    // FAMILY_ENTERPRISE 家族企业
    // GROUP_SUBSIDIARY 集团子公司

    @Column(length = 500)
    private String relationDesc;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
