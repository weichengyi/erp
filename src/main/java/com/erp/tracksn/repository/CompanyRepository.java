package com.erp.tracksn.repository;

import com.erp.tracksn.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * 公司档案 Repository
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * 根据公司类型查询
     */
    List<Company> findByType(String type);

    /**
     * 根据合作状态查询
     */
    List<Company> findByCooperationStatus(String status);

    /**
     * 搜索公司
     */
    List<Company> findByCompanyNameContaining(String keyword);

    /**
     * 查询关联公司
     */
    @Query("SELECT c FROM Company c WHERE c.parentCompanyId = :parentId OR c.id = :parentId")
    List<Company> findRelatedCompanies(@Param("parentId") Long parentId);

    /**
     * 查询启用的公司
     */
    List<Company> findByIsActiveTrue();
}
