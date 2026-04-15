package com.erp.tracksn.repository;

import com.erp.tracksn.entity.CompanyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * 公司关联关系 Repository
 */
public interface CompanyRelationRepository extends JpaRepository<CompanyRelation, Long> {

    /**
     * 查询公司的所有关联关系
     */
    List<CompanyRelation> findByCompanyId(Long companyId);

    /**
     * 查询关联公司 ID 列表
     */
    @Query("SELECT cr.relatedCompanyId FROM CompanyRelation cr WHERE cr.companyId = :companyId")
    List<Long> findRelatedCompanyIds(@Param("companyId") Long companyId);

    /**
     * 根据关联类型查询
     */
    List<CompanyRelation> findByRelationType(String relationType);

    /**
     * 检查两个公司是否已有关联
     */
    CompanyRelation findByCompanyIdAndRelatedCompanyId(Long companyId, Long relatedCompanyId);
}
