package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产品 SN 码实体 (一机一码)
 */
@Data
@Entity
@Table(name = "product_sn")
public class ProductSn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String snCode;

    @Column(length = 200)
    private String productName;

    @Column(length = 100)
    private String productModel;

    @Column(length = 100)
    private String category;

    @Column
    private LocalDate productionDate;

    @Column(length = 20)
    private String status = "IN_STOCK";
    // IN_STOCK 在库
    // SOLD 已售
    // OUT_OF_STOCK 出库
    // REPAIR 维修中
    // SCRAP 报废

    @Column
    private Long currentCompanyId;

    @Column
    private Long currentContactId;

    @Column(length = 200)
    private String warehouseLocation;

    @Column(length = 500)
    private String remark;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
