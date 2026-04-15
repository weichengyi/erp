package com.erp.tracksn.repository;

import com.erp.tracksn.entity.ProductSn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * 产品 SN 码 Repository
 */
public interface ProductSnRepository extends JpaRepository<ProductSn, Long> {

    /**
     * 根据 SN 码查询
     */
    Optional<ProductSn> findBySnCode(String snCode);

    /**
     * 根据状态查询
     */
    List<ProductSn> findByStatus(String status);

    /**
     * 根据当前所属公司查询
     */
    List<ProductSn> findByCurrentCompanyId(Long companyId);

    /**
     * 搜索 SN 码
     */
    List<ProductSn> findBySnCodeContaining(String keyword);

    /**
     * 查询某公司的所有 SN 码历史
     */
    @Query("SELECT ps FROM ProductSn ps WHERE ps.currentCompanyId = :companyId OR ps.remark LIKE %:companyId%")
    List<ProductSn> findCompanySnHistory(@Param("companyId") Long companyId);
}
