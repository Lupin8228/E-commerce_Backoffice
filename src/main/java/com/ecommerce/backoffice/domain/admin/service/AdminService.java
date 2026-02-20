package com.ecommerce.backoffice.domain.admin.service;


import com.ecommerce.backoffice.domain.admin.dto.request.*;
import com.ecommerce.backoffice.domain.admin.dto.response.*;
import com.ecommerce.backoffice.domain.admin.dto.session.TimeProvider;
import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final TimeProvider timeProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminSignUpResponse signUp(AdminSignUpRequest request) {
        if (adminRepository.existsByEmail(request.email())) {
            throw new CommonException(CommonError.INVALID_PASSWORD);
        }

        String encodePassword = passwordEncoder.encode(request.password());
        Admin newAdmin = adminRepository.save(
                Admin.builder()
                        .name(request.name())
                        .email(request.email())
                        .password(encodePassword)
                        .phone(request.phone())
                        .role(AdminRole.CS_ADMIN)
                        .status(AdminStatus.PENDING)
                        .build()
        );

        return AdminSignUpResponse.from(newAdmin);
    }


    @Transactional(readOnly = true)
    public AdminLoginResponse login(AdminLoginRequest request, HttpServletRequest sessionRequest) {
        Admin admin = adminRepository.findByEmail(request.email()).orElseThrow(
                () -> new CommonException(CommonError.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(request.password(), admin.getPassword())){
            throw new CommonException(CommonError.INVALID_PASSWORD);
        }

        if (admin.getStatus() != AdminStatus.APPROVED) {
            switch (admin.getStatus()) {
                case PENDING   -> throw new CommonException(CommonError.PENDING_ACCOUNT);
                case SUSPENDED -> throw new CommonException(CommonError.SUSPENDED_ACCOUNT);
                case INACTIVATE -> throw new CommonException(CommonError.INACTIVE_ACCOUNT);
                default        -> throw new CommonException(CommonError.LOGIN_FAILED);
            }
        }

        HttpSession session = sessionRequest.getSession(true);
        session.setAttribute("ADMIN_ID", admin.getId());

        return AdminLoginResponse.from(admin);
    }

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
        return UpdateAdminResponse.from(admin);
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
        admin.approve(timeProvider.now());
        return DecisionAdminResponse.from(admin);
    }

    // 관리자 거부
    @Transactional
    public DecisionAdminResponse rejectAdmin(Long adminId, RejectAdminRequest requestBody) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(
                        () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
                );

        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new CommonException(CommonError.ADMIN_NOT_PENDING);
        }

        admin.reject(timeProvider.now(), requestBody.reason());
        return DecisionAdminResponse.from(admin);
    }

    // 내 프로필 조회
    @Transactional(readOnly = true)
    public GetProfileResponse getProfile(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(
                () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
        );
        return new GetProfileResponse(admin.getName(), admin.getEmail(), admin.getPhone());
    }

    // 내 프로필 수정
    @Transactional
    public UpdateProfileResponse updateProfile(Long id, UpdateProfileRequest requestBody) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new CommonException(CommonError.ADMIN_NOT_FOUND));

        boolean hasAny = (requestBody.name() != null) || (requestBody.email() != null) || (requestBody.phone() != null);
        if (!hasAny) {
            throw new CommonException(CommonError.INVALID_UPDATE_REQUEST);
        }

        String newName = (requestBody.name() == null) ? admin.getName() : requestBody.name();
        String newEmail = (requestBody.email() == null) ? admin.getEmail() : requestBody.email();
        String newPhone = (requestBody.phone() == null) ? admin.getPhone() : requestBody.phone();

        if (requestBody.email() != null) {
            boolean duplicated = adminRepository.existsByEmailAndIdNot(newEmail, id);
            if (duplicated) {
                throw new CommonException(CommonError.DUPLICATE_EMAIL);
            }
        }

        admin.updateInfo(newName, newEmail, newPhone);
        return new UpdateProfileResponse(admin.getName(), admin.getEmail(), admin.getPhone());
    }

    // 내 비밀 번호 변경
    @Transactional
    public UpdatePasswordResponse changePassword(Long id, UpdatePasswordRequest requestBody) {

        Admin admin = adminRepository.findById(id).orElseThrow(
                () -> new CommonException(CommonError.ADMIN_NOT_FOUND)
        );

        if (!passwordEncoder.matches(requestBody.currentPassword(), admin.getPassword())) {
            throw new CommonException(CommonError.CURRENT_PASSWORD_MISMATCH);
        }

        String newHash = passwordEncoder.encode(requestBody.newPassword());
        admin.changePasswordHash(newHash);

        return new UpdatePasswordResponse(admin.getId(), "비밀번호가 변경되었습니다.");
    }

}
