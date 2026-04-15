package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人员流动记录实体
 */
@Data
@Entity
@Table(name = "person_movement")
public class PersonMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long personId;

    @Column(nullable = false, length = 20)
    private String movementType;
    // RESIGN 离职
    // TRANSFER 转岗
    // JOIN 入职

    @Column
    private Long fromCompanyId;

    @Column
    private Long toCompanyId;

    @Column(nullable = false)
    private LocalDate movementDate;

    @Column(length = 50)
    private String newPosition;

    @Column(length = 500)
    private String remark;

    @Column
    private Boolean notified = false;

    @Column
    private LocalDateTime notifiedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
