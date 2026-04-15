package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公司档案实体 (客户/供应商)
 */
@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(length = 50)
    private String uniformCreditCode;

    @Column(nullable = false, length = 20)
    private String type; // CUSTOMER, SUPPLIER

    @Column(length = 500)
    private String address;

    @Column(length = 50)
    private String contactPerson;

    @Column(length = 20)
    private String contactPhone;

    @Column(length = 100)
    private String contactEmail;

    @Column(length = 20)
    private String cooperationStatus; // ACTIVE, INACTIVE, POTENTIAL

    @Column(length = 20)
    private String cooperationLevel; // A, B, C

    @Column
    private Integer paymentCycle;

    @Column
    private Long parentCompanyId;

    @Column
    private Boolean isActive = true;

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
