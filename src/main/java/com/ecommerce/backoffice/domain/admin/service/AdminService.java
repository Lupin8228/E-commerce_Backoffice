package com.ecommerce.backoffice.domain.admin.service;

import com.ecommerce.backoffice.domain.admin.dto.request.AdminSignUpRequest;
import com.ecommerce.backoffice.domain.admin.dto.response.AdminSignUpResponse;
import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
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
        if (repository.existsByEmail(request.getEmail())) {
            throw new CommonException(CommonError.INVALID_PASSWORD);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        Admin newAdmin = repository.save(
                Admin.builder()
                        .name(request.getName())
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(encodePassword)
                        .phone(request.getPhone())
                        .role(AdminRole.CS_ADMIN)
                        .status(AdminStatus.PENDING)
                        .build()
        );

        return AdminSignUpResponse.from(newAdmin);



    }

}
