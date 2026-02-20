package com.ecommerce.backoffice.domain.admin.controller;


import com.ecommerce.backoffice.domain.admin.dto.request.*;
import com.ecommerce.backoffice.domain.admin.dto.response.*;
import com.ecommerce.backoffice.domain.admin.dto.session.SessionAdmin;
import com.ecommerce.backoffice.domain.admin.dto.session.SessionAdminContext;
import com.ecommerce.backoffice.domain.admin.service.AdminService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionAdminContext sessionAdminContext;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminSignUpResponse>> signUp(
            @Valid @RequestBody AdminSignUpRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(adminService.signUp(request))
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest sessionRequest
    ) {
        AdminLoginResponse response = adminService.login(request, sessionRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }
//    // 관리자 리스트 조회(검색/페이징/정렬/역할/상태)
//    @GetMapping
//    public ResponseEntity<AdminListResponse> list(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir,
//            @RequestParam(required = false) String role,
//            @RequestParam(required = false) String status,
//            HttpServletRequest request
//    ) {
//        AdminSearchCondition condition = AdminSearchCondition.from(
//                keyword, page, size, sortBy, sortDir, role, status
//        );
//        return ResponseEntity.ok(adminService.findAdmins(condition));
//    }


    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<GetAdminDetailResponse> getOne(
            @PathVariable Long adminId,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);
        GetAdminDetailResponse response = adminService.getOne(adminId);
        return ResponseEntity.ok(response);
    }

    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<UpdateAdminResponse> updateAdminInfo(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminRequest requestBody,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);

        UpdateAdminResponse response = adminService.updateAdminInfo(adminId, requestBody);
        return ResponseEntity.ok(response);
    }

    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<UpdateAdminRoleResponse> updateAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest requestBody,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);

        UpdateAdminRoleResponse response = adminService.updateAdminRole(adminId, requestBody);
        return ResponseEntity.ok(response);
    }

    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long adminId,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    // 관리자 승인
    @PatchMapping("/{adminId}/approve")
    public ResponseEntity<DecisionAdminResponse> approve(
            @PathVariable Long adminId,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);
        DecisionAdminResponse response = adminService.approveAdmin(adminId);
        return ResponseEntity.ok(response);
    }

    // 거부
    @PatchMapping("/{adminId}/reject")
    public ResponseEntity<DecisionAdminResponse> reject(
            @PathVariable Long adminId,
            @Valid @RequestBody RejectAdminRequest requestBody,
            HttpServletRequest request
    ) {
        sessionAdminContext.requireSuperAdmin(request);

        DecisionAdminResponse response = adminService.rejectAdmin(adminId, requestBody);
        return ResponseEntity.ok(response);
    }

    // 내 프로필 조회
    @GetMapping("/me/profile")
    public ResponseEntity<GetProfileResponse> getProfile(
            HttpServletRequest request
    ) {
        SessionAdmin me = sessionAdminContext.requireLogin(request);
        GetProfileResponse response = adminService.getProfile(me.id());
        return ResponseEntity.ok(response);
    }

    // 내 프로필 수정
    @PatchMapping("/me/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest requestBody,
            HttpServletRequest request
    ) {
        SessionAdmin me = sessionAdminContext.requireLogin(request);
        UpdateProfileResponse response = adminService.updateProfile(me.id(), requestBody);
        return ResponseEntity.ok(response);
    }

    // 내 비밀 번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<UpdatePasswordResponse> changeMyPassword(
            @Valid @RequestBody UpdatePasswordRequest requestBody,
            HttpServletRequest request
    ) {
        SessionAdmin me = sessionAdminContext.requireLogin(request);
        UpdatePasswordResponse response = adminService.changePassword(me.id(), requestBody);
        return ResponseEntity.ok(response);
    }


}



