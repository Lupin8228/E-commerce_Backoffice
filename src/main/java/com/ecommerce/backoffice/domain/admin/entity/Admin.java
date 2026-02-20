package com.ecommerce.backoffice.domain.admin.entity;

import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto_increment
    private Long id;

    @Column(nullable = false, length = 50) // 이름 필수, 최대 50자
    private String name;

    @Column(nullable = false, unique = true, length = 100) // 이메일 중복 방지, DB 레벨 UNIQUE
    private String email;

    @Column(name = "password_hash", nullable = false, length = 200) // 원문 저장 금지, 해시만 저장
    private String passwordHash;

    @Column(nullable = false, length = 13) // 010-XXXX-XXXX, 13자리
    private String phone;

    @Enumerated(EnumType.STRING) // ENUM은 문자열 저장, 순서 변경 이슈 방지
    @Column(nullable = false, length = 30)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) // 회원가입 기본 상태는 승인대기
    private AdminStatus status;

    // 승인/거부 관련
    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt;

    @Column(length = 200)
    private String rejectedReason;

    @Builder
    private Admin(String name, String email, String passwordHash,
                  String phone, AdminRole role, AdminStatus status) {

        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

}