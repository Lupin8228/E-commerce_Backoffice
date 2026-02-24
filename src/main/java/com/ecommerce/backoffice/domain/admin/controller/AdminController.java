package com.ecommerce.backoffice.domain.admin.controller;


import com.ecommerce.backoffice.domain.admin.dto.request.*;
import com.ecommerce.backoffice.domain.admin.dto.response.*;
import com.ecommerce.backoffice.domain.admin.service.AdminService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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

    // 관리자 리스트 조회
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping("/admins")
    public ResponseEntity<ApiResponse<GetAdminListResponse>> getAllAdmins(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status
    ) {
        GetAdminListResponse response = adminService.getAllAdmins(keyword, page, size, sortBy, sortDir, role, status);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 상세 조회
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping("/admins/{adminId}")
    public ResponseEntity<ApiResponse<GetAdminDetailResponse>> getOne(
            @PathVariable Long adminId
    ) {
        GetAdminDetailResponse response = adminService.getOne(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 정보 수정
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/admins/{adminId}")
    public ResponseEntity<ApiResponse<UpdateAdminResponse>> updateAdminInfo(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRequest requestBody
    ) {
        UpdateAdminResponse response = adminService.updateAdminInfo(adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 역할 변경
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/admins/{adminId}/role")
    public ResponseEntity<ApiResponse<UpdateAdminRoleResponse>> updateAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest requestBody

    ) {
        UpdateAdminRoleResponse response = adminService.updateAdminRole(adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 상태 변경
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/admins/{adminId}/status")
    public ResponseEntity<ApiResponse<PatchAdminStatusChangeResponse>> changeStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody PatchAdminStatusChangeRequest requestBody

    ) {
        PatchAdminStatusChangeResponse response = adminService.changeStatus(adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 관리자 삭제
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/admins/{adminId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long adminId
    ) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    // 관리자 승인
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/admins/{adminId}/approve")
    public ResponseEntity<ApiResponse<PatchDecisionAdminResponse>> approve(
            @PathVariable Long adminId
    ) {
        PatchDecisionAdminResponse response = adminService.approveAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 거부
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/admins/{adminId}/reject")
    public ResponseEntity<ApiResponse<PatchDecisionAdminResponse>> reject(
            @PathVariable Long adminId,
            @Valid @RequestBody PatchRejectAdminRequest requestBody
    ) {
        PatchDecisionAdminResponse response = adminService.rejectAdmin(adminId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 프로필 조회
    @GetMapping("/admins/me/profile")
    public ResponseEntity<ApiResponse<GetProfileResponse>> getProfile(
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        Long meId = principal.getAdmin().getId();
        GetProfileResponse response = adminService.getProfile(meId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 프로필 수정
    @PatchMapping("/admins/me/profile")
    public ResponseEntity<ApiResponse<UpdateProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody UpdateProfileRequest requestBody
    ) {
        Long meId = principal.getAdmin().getId();
        UpdateProfileResponse response = adminService.updateProfile(meId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 내 비밀 번호 변경
    @PatchMapping("/admins/me/password")
    public ResponseEntity<ApiResponse<UpdatePasswordResponse>> changeMyPassword(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody UpdatePasswordRequest requestBody
    ) {
        Long meId = principal.getAdmin().getId();
        UpdatePasswordResponse response = adminService.changePassword(meId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

}



