package com.erp.tracksn.controller;

import com.erp.tracksn.entity.Asset;
import com.erp.tracksn.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 资产管理 Controller
 */
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssetController {

    private final AssetService assetService;

    /**
     * 生成新的资产编码 (不保存，仅预览)
     */
    @GetMapping("/generate-code")
    public ResponseEntity<String> generateAssetCode() {
        String code = assetService.generateAssetCode();
        return ResponseEntity.ok(code);
    }

    /**
     * 获取所有资产
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    /**
     * 根据资产编码查询
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Asset> getByAssetCode(@PathVariable String code) {
        return assetService.findByAssetCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建资产
     */
    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) {
        Asset created = assetService.createAsset(asset);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新资产
     */
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable Long id,
            @RequestBody Asset asset) {
        Asset updated = assetService.updateAsset(id, asset);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除资产
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok().build();
    }
}
