package com.ecommerce.backoffice.domain.admin.service;

import com.ecommerce.backoffice.domain.admin.dto.request.AdminLoginRequest;
import com.ecommerce.backoffice.domain.admin.dto.request.AdminSignUpRequest;
import com.ecommerce.backoffice.domain.admin.dto.response.AdminLoginResponse;
import com.ecommerce.backoffice.domain.admin.dto.response.AdminSignUpResponse;
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

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminSignUpResponse signUp(AdminSignUpRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new CommonException(CommonError.INVALID_PASSWORD);
        }

        String encodePassword = passwordEncoder.encode(request.password());
        Admin newAdmin = repository.save(
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
        Admin admin = repository.findByEmail(request.email()).orElseThrow(
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

}
