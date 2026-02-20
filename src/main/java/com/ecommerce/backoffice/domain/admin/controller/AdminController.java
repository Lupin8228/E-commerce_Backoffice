package com.ecommerce.backoffice.domain.admin.controller;

import com.ecommerce.backoffice.domain.admin.dto.request.AdminLoginRequest;
import com.ecommerce.backoffice.domain.admin.dto.request.AdminSignUpRequest;
import com.ecommerce.backoffice.domain.admin.dto.response.AdminLoginResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.AdminSignUpResponse;
import com.ecommerce.backoffice.domain.admin.service.AdminService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService service;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminSignUpResponse>> signUp(
            @Valid @RequestBody AdminSignUpRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.signUp(request))
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest sessionRequest
    ) {
        AdminLoginResponse response = service.login(request, sessionRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }
}
