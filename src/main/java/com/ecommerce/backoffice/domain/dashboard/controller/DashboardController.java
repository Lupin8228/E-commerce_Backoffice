package com.ecommerce.backoffice.domain.dashboard.controller;

import com.ecommerce.backoffice.domain.dashboard.dto.DashboardResponse;
import com.ecommerce.backoffice.domain.dashboard.service.DashboardService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 대시보드 조회
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(dashboardService.getDashboard()));
    }
}