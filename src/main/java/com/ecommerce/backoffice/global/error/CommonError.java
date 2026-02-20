package com.ecommerce.backoffice.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CommonError {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_001", "조회할 유저가 없습니다."),
    NOT_UPDATE_USER(HttpStatus.NOT_FOUND, "USER_002", "수정할 유저가 없습니다."),
    NOT_DELETE_USER(HttpStatus.NOT_FOUND, "USER_003", "삭제할 유저가 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_001", "비밀번호가 틀렸습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "AUTH_002", "접근 권한이 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "A001", "이미 사용 중인 이메일입니다."),

    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "존재하지 않는 관리자입니다."),
    ADMIN_NOT_LOGGED_IN(UNAUTHORIZED, "A106", "로그인이 필요합니다."),
    FORBIDDEN_SUPER_ADMIN_ONLY(FORBIDDEN, "A201", "슈퍼 관리자만 접근할 수 있습니다."),
    INVALID_UPDATE_REQUEST(BAD_REQUEST, "A302", "수정할 값이 없습니다."),
    ADMIN_NOT_PENDING(BAD_REQUEST, "A401", "승인대기 상태에서만 처리할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}

