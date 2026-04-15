package com.erp.tracksn.controller;

import com.erp.tracksn.entity.ProductSn;
import com.erp.tracksn.repository.ProductSnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * SN 码管理 Controller
 */
@RestController
@RequestMapping("/api/product-sns")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductSnController {

    private final ProductSnRepository productSnRepository;

    /**
     * 获取所有 SN 码
     */
    @GetMapping
    public ResponseEntity<List<ProductSn>> getAllProductSns() {
        return ResponseEntity.ok(productSnRepository.findAll());
    }

    /**
     * 根据 SN 码查询
     */
    @GetMapping("/code/{snCode}")
    public ResponseEntity<ProductSn> getBySnCode(@PathVariable String snCode) {
        return productSnRepository.findBySnCode(snCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建 SN 码记录
     */
    @PostMapping
    public ResponseEntity<ProductSn> createProductSn(@RequestBody ProductSn productSn) {
        ProductSn saved = productSnRepository.save(productSn);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新 SN 码
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductSn> updateProductSn(
            @PathVariable Long id,
            @RequestBody ProductSn productSn) {
        
        return productSnRepository.findById(id)
                .map(existing -> {
                    productSn.setId(id);
                    return ResponseEntity.ok(productSnRepository.save(productSn));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除 SN 码
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSn(@PathVariable Long id) {
        productSnRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductSn>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(productSnRepository.findByStatus(status));
    }

    /**
     * 溯源查询 - 根据 SN 码查询全流程
     */
    @GetMapping("/trace/{snCode}")
    public ResponseEntity<ProductSn> traceSnCode(@PathVariable String snCode) {
        Optional<ProductSn> productSn = productSnRepository.findBySnCode(snCode);
        return productSn.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
