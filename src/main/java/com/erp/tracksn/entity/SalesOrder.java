package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售订单实体
 */
@Data
@Entity
@Table(name = "sales_order")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(nullable = false)
    private Long customerId;

    @Column
    private Long contactPersonId;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(length = 20)
    private String status = "PENDING";
    // PENDING, CONFIRMED, SHIPPED, COMPLETED, CANCELLED

    @Column(length = 20)
    private String paymentStatus = "UNPAID";
    // UNPAID, PARTIAL, PAID

    @Column(length = 500)
    private String shippingAddress;

    @Column(length = 1000)
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
        orderDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
