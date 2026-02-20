package com.ecommerce.backoffice.domain.admin.entity;



import com.ecommerce.backoffice.domain.admin.dto.request.UpdateAdminRequest;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE admins SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 200)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt;

    @Column(length = 200)
    private String rejectedReason;

    @Builder
    private Admin(String name, String email, String password,
                  String phone, AdminRole role, AdminStatus status) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public void updateInfo(String newName, String newEmail, String newPhone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void updateAdminRole(AdminRole role) {
        this.role = role;
    }

    public void approve(LocalDateTime now) {
        this.status = AdminStatus.APPROVED;
        this.approvedAt = now;
        this.rejectedAt = null;
        this.rejectedReason = null;
    }

    public void reject(LocalDateTime now, String reason) {
        this.status = AdminStatus.REJECTED;
        this.rejectedAt = now;
        this.rejectedReason = reason;
    }

    public void changePasswordHash(String newPasswordHash) {
        this.password = newPasswordHash;
    }
}