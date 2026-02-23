package com.ecommerce.backoffice.domain.admin.controller;


import com.ecommerce.backoffice.domain.admin.dto.request.*;
import com.ecommerce.backoffice.domain.admin.dto.response.*;
import com.ecommerce.backoffice.domain.admin.dto.session.SessionAdmin;
import com.ecommerce.backoffice.domain.admin.dto.session.SessionKey;
import com.ecommerce.backoffice.domain.admin.service.AdminService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminSignUpResponse>> signUp(
            @Valid @RequestBody AdminSignUpRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(adminService.signUp(request))
                );
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request
    ) {
        AdminLoginResponse response = adminService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        adminService.logout(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }


    // 관리자 리스트 조회
    @GetMapping("/admins")
    public ResponseEntity<ApiResponse<GetAdminListResponse>> getAllAdmins(
            HttpSession session,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status
    ) {
        GetAdminListResponse response = adminService.getAllAdmins(session, keyword, page, size, sortBy, sortDir, role, status);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<GetAdminDetailResponse>> getOne(
            HttpSession session,
            @PathVariable Long adminId
    ) {
        GetAdminDetailResponse response = adminService.getOne(session, adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<ApiResponse<UpdateAdminResponse>> updateAdminInfo(
            HttpSession session,
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRequest requestBody
    ) {
        UpdateAdminResponse response = adminService.updateAdminInfo(session, adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<ApiResponse<UpdateAdminRoleResponse>> updateAdminRole(
            HttpSession session,
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest requestBody

    ) {
        UpdateAdminRoleResponse response = adminService.updateAdminRole(session, adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<ApiResponse<PatchAdminStatusChangeResponse>> changeStatus(
            HttpSession session,
            @PathVariable Long adminId,
            @Valid @RequestBody PatchAdminStatusChangeRequest requestBody

    ) {
        PatchAdminStatusChangeResponse response = adminService.changeStatus(session, adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            HttpSession session,
            @PathVariable Long adminId
    ) {
        adminService.deleteAdmin(session, adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    // 관리자 승인
    @PatchMapping("/{adminId}/approve")
    public ResponseEntity<ApiResponse<PatchDecisionAdminResponse>> approve(
            HttpSession session,
            @PathVariable Long adminId
    ) {
        PatchDecisionAdminResponse response = adminService.approveAdmin(session, adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 거부
    @PatchMapping("/{adminId}/reject")
    public ResponseEntity<ApiResponse<PatchDecisionAdminResponse>> reject(
            HttpSession session,
            @PathVariable Long adminId,
            @Valid @RequestBody PatchRejectAdminRequest requestBody
    ) {
        PatchDecisionAdminResponse response = adminService.rejectAdmin(session, adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 프로필 조회
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<GetProfileResponse>> getProfile(
            HttpSession session
    ) {
        SessionAdmin me = SessionKey.getLoginAdmin(session);
        GetProfileResponse response = adminService.getProfile(session, me.id());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 프로필 수정
    @PatchMapping("/me/profile")
    public ResponseEntity<ApiResponse<UpdateProfileResponse>> updateProfile(
            HttpSession session,
            @Valid @RequestBody UpdateProfileRequest requestBody
    ) {
        SessionAdmin me = SessionKey.getLoginAdmin(session);
        UpdateProfileResponse response = adminService.updateProfile(session, me.id(), requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 비밀 번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<UpdatePasswordResponse>> changeMyPassword(
            HttpSession session,
            @Valid @RequestBody UpdatePasswordRequest requestBody
    ) {
        SessionAdmin me = SessionKey.getLoginAdmin(session);
        UpdatePasswordResponse response = adminService.changePassword(session, me.id(), requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

}



