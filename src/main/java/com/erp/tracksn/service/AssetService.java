package com.erp.tracksn.service;

import com.erp.tracksn.entity.Asset;
import com.erp.tracksn.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 资产编码服务
 */
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    /**
     * 生成资产编码 (YYYYMMDD+4 位)
     */
    @Transactional
    public synchronized String generateAssetCode() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 查询当日已生成的资产编码
        List<Asset> todayAssets = assetRepository.findByAssetCodeStartingWith(datePrefix);
        
        // 生成 4 位序列号
        int sequence = todayAssets.size() + 1;
        if (sequence > 9999) {
            throw new RuntimeException("当日资产编码已达上限 (9999)");
        }
        
        String sequenceStr = String.format("%04d", sequence);
        return datePrefix + sequenceStr;
    }

    /**
     * 创建资产
     */
    @Transactional
    public Asset createAsset(Asset asset) {
        // 如果未指定资产编码，自动生成
        if (asset.getAssetCode() == null || asset.getAssetCode().isEmpty()) {
            asset.setAssetCode(generateAssetCode());
        }
        return assetRepository.save(asset);
    }

    /**
     * 根据编码查询资产
     */
    public Optional<Asset> findByAssetCode(String assetCode) {
        return assetRepository.findByAssetCode(assetCode);
    }

    /**
     * 根据 SN ID 查询资产
     */
    public Optional<Asset> findBySnId(Long snId) {
        return assetRepository.findBySnId(snId);
    }

    /**
     * 获取所有资产
     */
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    /**
     * 更新资产
     */
    @Transactional
    public Asset updateAsset(Long id, Asset asset) {
        asset.setId(id);
        return assetRepository.save(asset);
    }

    /**
     * 删除资产
     */
    @Transactional
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }
}
