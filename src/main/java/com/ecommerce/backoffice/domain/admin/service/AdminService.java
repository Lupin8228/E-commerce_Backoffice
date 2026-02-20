package com.ecommerce.backoffice.domain.admin.service;


import com.ecommerce.backoffice.domain.admin.dto.request.UpdateAdminRequest;
import com.ecommerce.backoffice.domain.admin.dto.request.UpdateAdminRoleRequest;
import com.ecommerce.backoffice.domain.admin.dto.response.DecisionAdminResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.GetAdminDetailResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.UpdateAdminResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.UpdateAdminRoleResponse;
import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public GetAdminDetailResponse getOne(Long adminId) {
        Admin admin = findById(adminId);
        return GetAdminDetailResponse.from(admin);
    }

    // 관리자Id 존재 여부 확인
    private Admin findById(Long adminId) {
        return adminRepository.findById(adminId).orElseThrow(
                () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
        );
    }

    // 관리자 정보 수정
    @Transactional
    public UpdateAdminResponse updateAdminInfo(Long adminId, UpdateAdminRequest requestBody) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CommonException(CommonError.ADMIN_NOT_FOUND));

        boolean hasAny = (requestBody.name() != null) || (requestBody.email() != null) || (requestBody.phone() != null);

        if (!hasAny) {
            throw new CommonException(CommonError.INVALID_UPDATE_REQUEST);
        }

        String newName = (requestBody.name() == null) ? admin.getName() : requestBody.name();
        String newEmail = (requestBody.email() == null) ? admin.getEmail() : requestBody.email();
        String newPhone = (requestBody.phone() == null) ? admin.getPhone() : requestBody.phone();

        // 이메일 변경 시에만 중복 체크(본인 제외)
        if (requestBody.email() != null) {
            boolean duplicated = adminRepository.existsByEmailAndIdNot(newEmail, adminId);
            if (duplicated) {
                throw new CommonException(CommonError.DUPLICATE_EMAIL);
            }
        }

        admin.updateInfo(newName, newEmail, newPhone);

        return new UpdateAdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getApprovedAt()
        );
    }

    // 관리자 역할 변경
    @Transactional
    public UpdateAdminRoleResponse updateAdminRole(Long adminId, UpdateAdminRoleRequest requestBody) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
        );
        admin.updateAdminRole(requestBody.role().getRole());
        return new UpdateAdminRoleResponse(admin.getId(), admin.getRole());
    }

    // 관리자 삭제
    @Transactional
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CommonException(CommonError.ADMIN_NOT_FOUND));
        adminRepository.delete(admin);
    }

    // 관리자 승인
    @Transactional
    public DecisionAdminResponse approveAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(
                        () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
                );
        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new CommonException(CommonError.ADMIN_NOT_PENDING);
        }
        admin.approve(LocalDateTime.now());
        return DecisionAdminResponse.from(admin);
    }
}
