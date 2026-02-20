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

    // 로그인 필수 세션,로그인 정보 없으면 NOT_LOGGED_IN 반환
    public SessionAdmin requireLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);


        if (session == null) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }

        Object value = session.getAttribute(SessionKey.LOGIN_ADMIN);

        if (!(value instanceof SessionAdmin)) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }

        return (SessionAdmin) value;
    }

    // 슈퍼 관리자 필수, 로그인은 requireLogin에서 처리하고, 권한만 여기서 체크
    public SessionAdmin requireSuperAdmin(HttpServletRequest request) {
        SessionAdmin loginAdmin = requireLogin(request);
        if (loginAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CommonException(CommonError.FORBIDDEN_SUPER_ADMIN_ONLY);
        }
        return loginAdmin;
    }

}
