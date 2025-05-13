package com.autumn.common.core.result;

import lombok.Getter;

/**
 * @author autumn
 * @desc 统一返回结果状态信息类
 * @date 2025年05月07日
 */
@Getter
public enum ResultCodeEnum {

    // 2XX 请求成功
    SUCCESS(200, "SUCCESS"),

    // 4XX 客户端错误
    BAD_REQUEST(400, "BAD_REQUEST"),
    OBJECT_NOTNULL(400, "OBJECT_NOTNULL"),
    LOGIN_INVALID(401, "LOGIN_INVALID"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT_FOUND"),

    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED"),
    NOT_ACCEPTABLE(406, "NOT_ACCEPTABLE"),
    CONFLICT(409, "CONFLICT"),
    DUPLICATE_ACCOUNTS(409, "DUPLICATE_ACCOUNTS"),
    DUPLICATE_ROLES(409, "DUPLICATE_ROLES"),
    ACCOUNT_LOCK(410, "ACCOUNT_LOCK"),
    UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED_MEDIA_TYPE"),

    // captcha
    CAPTCHA_INCORRECT(400, "CAPTCHA_INCORRECT"),
    CAPTCHA_NOT_EMPTY(404, "CAPTCHA_NOT_EMPTY"),
    CAPTCHA_NOT_EXPIRED(404, "CAPTCHA_NOT_EXPIRED"),

    // 5XX服务器错误响应
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),


    ;

    private final Integer code;

    private final String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
