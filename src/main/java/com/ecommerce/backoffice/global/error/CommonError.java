package com.ecommerce.backoffice.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonError {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_001", "조회할 유저가 없습니다."),
    NOT_UPDATE_USER(HttpStatus.NOT_FOUND, "USER_002", "수정할 유저가 없습니다."),
    NOT_DELETE_USER(HttpStatus.NOT_FOUND, "USER_003", "삭제할 유저가 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "ADMIN_001", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_001", "비밀번호가 틀렸습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "AUTH_002", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}

