package com.erp.tracksn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联系人档案实体
 */
@Data
@Entity
@Table(name = "contact_person")
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String position;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column
    private Boolean isPrimary = false;

    @Column
    private Boolean isDecisionMaker = false;

    @Column
    private LocalDate entryDate;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, LEFT, TRANSFERRED

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
