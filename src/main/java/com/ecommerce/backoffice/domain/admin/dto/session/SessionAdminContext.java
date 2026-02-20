package com.ecommerce.backoffice.domain.admin.dto.session;


import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionAdminContext {

    // 세션 없으면 401, role 아니면 403
    public SessionAdmin requireLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);


        if (session == null) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }

        Object value = session.getAttribute(SessionKey.LOGIN_ADMIN);
        if (value == null) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }

        return (SessionAdmin) value;
    }

    public SessionAdmin requireSuperAdmin(HttpServletRequest request) {
        SessionAdmin loginAdmin = requireLogin(request);
        if (loginAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CommonException(CommonError.FORBIDDEN_SUPER_ADMIN_ONLY);
        }
        return loginAdmin;
    }

}
