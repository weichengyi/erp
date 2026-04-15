package com.erp.tracksn.repository;

import com.erp.tracksn.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 资产编码 Repository
 */
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * 根据资产编码查询
     */
    Optional<Asset> findByAssetCode(String assetCode);

    /**
     * 根据 SN ID 查询
     */
    Optional<Asset> findBySnId(Long snId);

    /**
     * 查询当日已生成的资产编码
     */
    List<Asset> findByAssetCodeStartingWith(String datePrefix);

    /**
     * 查询某公司的资产
     */
    List<Asset> findByCompanyId(Long companyId);

    /**
     * 根据状态查询
     */
    List<Asset> findByStatus(String status);
}
