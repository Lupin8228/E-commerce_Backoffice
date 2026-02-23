package com.ecommerce.backoffice.domain.admin.dto.session;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSuperAdmin implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final TimeProvider timeProvider;
    private static final Logger log = LoggerFactory.getLogger(CreateSuperAdmin.class);

    @Override
    public void run(String... args) {
        String email = "super@admin.com";

        if (adminRepository.existsByEmail(email)) {
            log.warn("\uD83E\uDDB8 슈퍼 관리자가 이미 생성 되었습니다!!");
            return;
        }

        Admin superAdmin = Admin
                .builder()
                .name("susususupernova")
                .email("super@admin.com")
                .password(passwordEncoder.encode("superpassword1234"))
                .phone("010-0000-0000")
                .role(AdminRole.SUPER_ADMIN)
                .status(AdminStatus.APPROVED)
                .build();

        superAdmin.approve(timeProvider.now());
        adminRepository.save(superAdmin);
        log.warn("\uD83E\uDDB8 슈퍼 관리자 더미 데이터 생성 완료~~");
    }

}
