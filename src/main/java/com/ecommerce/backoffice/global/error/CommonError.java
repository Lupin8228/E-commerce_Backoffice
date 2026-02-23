package com.ecommerce.backoffice.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * [HTTP Status Code 기반 에러 정의 가이드]
 * * 400 (Bad Request): "데이터 형식 오류"
 * - 클라이언트가 필수 값을 누락하거나, 유효하지 않은 형식(이메일 규칙 등)을 보냈을 때 사용.
 * * 401 (Unauthorized): "인증 실패"
 * - 아이디/비밀번호 불일치, 토큰 누락 또는 만료 등 '누구인지' 증명하지 못한 경우.
 * * 403 (Forbidden): "권한 부족 / 거부"
 * - 로그인(인증)은 했으나 해당 리소스에 접근 권한이 없는 경우 (예: 승인 대기, 활동 정지).
 * * 404 (Not Found): "리소스 없음"
 * - 요청한 URL 자체가 잘못되었거나, DB에 해당 ID(PK)를 가진 데이터가 존재하지 않을 때.
 * * 409 (Conflict): "비즈니스 충돌"
 * - 요청은 정상이나 서버 상태와 충돌될 때 (예: 이메일 중복 가입, 중복 데이터 등록 시도).
 * * 500 (Internal Server Error): "서버 내부 오류"
 * - 비즈니스 로직에서 처리하지 못한 예외(NullPointerException 등)가 발생한 경우.
 */
@Getter
@RequiredArgsConstructor
public enum CommonError {

    // -- 2000: ADMIN --
    // 21xx: 인증 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "A2101", "존재하지 않는 계정입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "A2102", "비밀번호가 일치하지 않습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A2103", "로그인에 실패했습니다."),

    // 22xx: 계정 상태
    PENDING_ACCOUNT(HttpStatus.FORBIDDEN, "A2201", "승인 대기 중인 계정입니다."),
    SUSPENDED_ACCOUNT(HttpStatus.FORBIDDEN, "A2202", "활동이 정지된 계정입니다."),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "A2203", "비활성화된 계정입니다."),

    // 23xx: 중복
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A2301", "이미 존재하는 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_001", "조회할 유저가 없습니다."),
    NOT_UPDATE_USER(HttpStatus.NOT_FOUND, "USER_002", "수정할 유저가 없습니다."),
    NOT_DELETE_USER(HttpStatus.NOT_FOUND, "USER_003", "삭제할 유저가 없습니다."),
//    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_001", "비밀번호가 틀렸습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "AUTH_002", "접근 권한이 없습니다."),
//    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "A001", "이미 사용 중인 이메일입니다."),

    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "존재하지 않는 관리자입니다."),

    ADMIN_NOT_LOGGED_IN(UNAUTHORIZED, "A106", "로그인이 필요합니다."),
    FORBIDDEN_SUPER_ADMIN_ONLY(FORBIDDEN, "A201", "슈퍼 관리자만 접근할 수 있습니다."),
    INVALID_UPDATE_REQUEST(BAD_REQUEST, "A302", "수정할 값이 없습니다."),
    ADMIN_NOT_PENDING(BAD_REQUEST, "A401", "승인대기 상태에서만 처리할 수 있습니다."),
    CURRENT_PASSWORD_MISMATCH(BAD_REQUEST, "A501", "현재 비밀번호가 일치하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}

