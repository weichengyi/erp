package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产编码实体 (YYYYMMDD+4 位)
 */
@Data
@Entity
@Table(name = "asset")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String assetCode;

    @Column(nullable = false, length = 200)
    private String assetName;

    @Column(length = 50)
    private String assetType;

    @Column
    private Long snId;

    @Column
    private Long companyId;

    @Column
    private LocalDate purchaseDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(length = 20)
    private String status = "ACTIVE";
    // ACTIVE, INACTIVE, TRANSFERRED, SCRAP

    @Column(length = 200)
    private String location;

    @Column(length = 50)
    private String responsiblePerson;

    @Column(length = 500)
    private String remark;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private Long createdBy;

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
