package com.ecommerce.backoffice.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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

    // -- 1000:  --

    // -- 2000: ADMIN --
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "A2000", "존재하지 않는 계정입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "A2001", "비밀번호가 일치하지 않습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A2002", "로그인에 실패했습니다."),
    PENDING_ACCOUNT(HttpStatus.FORBIDDEN, "A2003", "승인 대기 중인 계정입니다."),
    SUSPENDED_ACCOUNT(HttpStatus.FORBIDDEN, "A2004", "활동이 정지된 계정입니다."),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "A2005", "비활성화된 계정입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A2006", "이미 존재하는 이메일입니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "A2007", "존재하지 않는 관리자입니다."),
    ADMIN_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "A2008", "로그인이 필요합니다."),
    FORBIDDEN_SUPER_ADMIN_ONLY(HttpStatus.FORBIDDEN, "A2009", "슈퍼 관리자만 접근할 수 있습니다."),
    INVALID_UPDATE_REQUEST(HttpStatus.BAD_REQUEST, "A2010", "수정할 값이 없습니다."),
    ADMIN_NOT_PENDING(HttpStatus.BAD_REQUEST, "A2011", "승인대기 상태에서만 처리할 수 있습니다."),
    CURRENT_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "A2012", "현재 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A2014", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A2015", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "A2016", "지원되지 않는 토큰 형식입니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "A2017", "토큰이 존재하지 않습니다."),

    // -- 3000: Customer --
    CUSTOMER_NOT_FOUND(BAD_REQUEST,"C3001","존재하지 않는 유저입니다."),
    INVALID_CUSTOMER_UPDATE(BAD_REQUEST,"C3002","유효하지 않은 유저 정보 또는 상태 변경 요청입니다."),

    // -- 4000: ORDER --
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O4001", "존재하지 않는 주문입니다."),
    ORDER_ALREADY_DELIVERED(BAD_REQUEST, "O4002", "배송완료된 주문은 상태를 변경할 수 없습니다."),
    ORDER_ALREADY_CANCELLED(BAD_REQUEST, "O4003", "취소된 주문은 상태를 변경할 수 없습니다."),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "O4004", "해당 주문에 접근할 권한이 없습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O4005", "잘못된 주문 양식입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "O4006", "상품을 찾을 수 없습니다."),
    PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "O4007", "해당 상품의 재고가 없습니다."),
    ORDER_NOT_PREPARING(BAD_REQUEST, "O4008", "준비중 상태에서만 취소할 수 있습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}

