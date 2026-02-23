package com.ecommerce.backoffice.domain.admin.dto.session;

import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
public final class SessionKey {
    public static final String LOGIN_ADMIN = "LOGIN_ADMIN";

    public static SessionAdmin getLoginAdmin(HttpSession session) {

        SessionAdmin admin = (SessionAdmin) session.getAttribute(LOGIN_ADMIN);

        if (admin == null) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }
        return admin;
    }

    // 슈퍼 관리자 예외 처리
    public static void checkSuperAdmin(HttpSession session) {

        SessionAdmin admin = getLoginAdmin(session);

        if (admin.role() != AdminRole.SUPER_ADMIN) {
            throw new CommonException(CommonError.FORBIDDEN_SUPER_ADMIN_ONLY);
        }
    }

    public static void validateAdmin(HttpSession session) {
        SessionAdmin admin = getLoginAdmin(session);

        Set<AdminRole> allowedRoles = EnumSet.of(
                AdminRole.SUPER_ADMIN,
                AdminRole.OPERATION_ADMIN,
                AdminRole.CS_ADMIN
        );

        if (!allowedRoles.contains(admin.role())) {
            throw new CommonException(CommonError.FORBIDDEN_SUPER_ADMIN_ONLY);
        }
    }

}
