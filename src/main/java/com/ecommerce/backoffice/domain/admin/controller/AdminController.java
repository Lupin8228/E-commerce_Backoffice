package com.ecommerce.backoffice.domain.admin.controller;


import com.ecommerce.backoffice.domain.admin.dto.request.UpdateAdminRequest;
import com.ecommerce.backoffice.domain.admin.dto.request.UpdateAdminRoleRequest;
import com.ecommerce.backoffice.domain.admin.dto.response.DecisionAdminResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.GetAdminDetailResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.UpdateAdminResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.UpdateAdminRoleResponse;
import com.ecommerce.backoffice.domain.admin.dto.session.SessionAdminContext;
import com.ecommerce.backoffice.domain.admin.service.AdminService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final SessionAdminContext sessionAdminContext;

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



}



