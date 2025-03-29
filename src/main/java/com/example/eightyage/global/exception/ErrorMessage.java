package com.example.eightyage.global.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOT_INVALID_USER_ROLE("유효하지 않은 UserRole"),
    NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다."),
    PASSWORD_CONFIRMATION_MISMATCH("비밀번호가 비밀번호 확인과 일치하지 않습니다."),
    DEACTIVATED_USER_EMAIL("탈퇴한 유저 이메일입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    EXPIRED_REFRESH_TOKEN("사용이 만료된 refresh token 입니다."),
    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰을 찾을 수 없습니다."),
    DUPLICATE_EMAIL("등록된 이메일입니다."),
    USER_EMAIL_NOT_FOUND("가입한 유저의 이메일이 아닙니다."),
    USER_ID_NOT_FOUND("해당 유저의 Id를 찾을 수 없습니다."),
    REFRESH_TOKEN_MUST_BE_STRING("@RefreshToken과 String 타입은 함께 사용되어야 합니다."),

    DEFAULT_UNAUTHORIZED("인증이 필요합니다."),
    DEFAULT_BAD_REQUEST("잘못된 요청입니다."),
    DEFAULT_NOT_FOUND("찾지 못했습니다."),
    DEFAULT_FORBIDDEN("권한이 없습니다."),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다."),

    INVALID_JWT_SIGNATURE("유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다."),

    EVENT_NOT_FOUND("이벤트를 찾을 수 없습니다."),
    INVALID_EVENT_PERIOD("이벤트 기간이 아닙니다."),
    CAN_NOT_ACCESS("잠시후 다시 시도해주세요"),
    COUPON_ALREADY_ISSUED("이미 쿠폰 발급 받은 사용자입니다."),
    COUPON_OUT_OF_STOCK("쿠폰 수량이 소진되었습니다."),
    COUPON_NOT_FOUND("쿠폰을 찾을 수 없습니다."),
    COUPON_ALREADY_USED("이미 사용된 쿠폰입니다."),
    COUPON_FORBIDDEN("본인의 쿠폰이 아닙니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
