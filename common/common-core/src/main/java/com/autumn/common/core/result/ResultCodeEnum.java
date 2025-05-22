package com.autumn.common.core.result;

import com.autumn.common.core.utils.I18nUtils;
import lombok.Getter;

import java.util.Locale;

/**
 * 100 => “HTTP/1.1 100 Continue” //继续
 * 101 => “HTTP/1.1 101 Switching Protocols” //分组交换协议
 * 200 => “HTTP/1.1 200 OK” //OK
 * 201 => “HTTP/1.1 201 Created” //被创建
 * 202 => “HTTP/1.1 202 Accepted” //被采纳
 * 203 => “HTTP/1.1 203 Non-Authoritative Information” //非授权信息
 * 204 => “HTTP/1.1 204 No Content” //无内容
 * 205 => “HTTP/1.1 205 Reset Content” //重置内容
 * 206 => “HTTP/1.1 206 Partial Content” //部分内容
 * 300 => “HTTP/1.1 300 Multiple Choices” //多选项
 * 301 => “HTTP/1.1 301 Moved Permanently” //永久地传送
 * 302 => “HTTP/1.1 302 Found” //找到
 * 303 => “HTTP/1.1 303 See Other” //参见其他
 * 304 => “HTTP/1.1 304 Not Modified” //未改动
 * 305 => “HTTP/1.1 305 Use Proxy” //使用代理
 * 307 => “HTTP/1.1 307 Temporary Redirect” //暂时重定向
 * 400 => “HTTP/1.1 400 Bad Request” //错误请求
 * 401 => “HTTP/1.1 401 Unauthorized” //未授权
 * 402 => “HTTP/1.1 402 Payment Required” //要求付费
 * 403 => “HTTP/1.1 403 Forbidden” //禁止
 * 404 => “HTTP/1.1 404 Not Found” //未找到
 * 405 => “HTTP/1.1 405 Method Not Allowed” //不允许的方法
 * 406 => “HTTP/1.1 406 Not Acceptable” //不被采纳
 * 407 => “HTTP/1.1 407 Proxy Authentication Required” //要求代理授权
 * 408 => “HTTP/1.1 408 Request Time-out” //请求超时
 * 409 => “HTTP/1.1 409 Conflict” //冲突
 * 410 => “HTTP/1.1 410 Gone” //过期的
 * 411 => “HTTP/1.1 411 Length Required” //要求的长度
 * 412 => “HTTP/1.1 412 Precondition Failed” //前提不成立
 * 413 => “HTTP/1.1 413 Request Entity Too Large” //请求实例太大
 * 414 => “HTTP/1.1 414 Request-URI Too Large” //请求URI太大
 * 415 => “HTTP/1.1 415 Unsupported Media Type” //不支持的媒体类型
 * 416 => “HTTP/1.1 416 Requested range not satisfiable” //无法满足的请求范围
 * 417 => “HTTP/1.1 417 Expectation Failed” //失败的预期
 * 500 => “HTTP/1.1 500 Internal Server Error” //内部服务器错误
 * 501 => “HTTP/1.1 501 Not Implemented” //未被使用
 * 502 => “HTTP/1.1 502 Bad Gateway” //网关错误
 * 503 => “HTTP/1.1 503 Service Unavailable” //不可用的服务
 * 504 => “HTTP/1.1 504 Gateway Time-out” //网关超时
 * 505 => “HTTP/1.1 505″ //HTTP版本未被支持
 */

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
    // 请求参数错误
    PARAM_ERROR(400, "PARAM_ERROR"),
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

    /* 登录异常 */
    // 验证码错误
    CAPTCHA_INCORRECT(400, "CAPTCHA_INCORRECT"),
    // 验证码为空
    CAPTCHA_NOT_EMPTY(400, "CAPTCHA_NOT_EMPTY"),
    // 验证码已过期
    CAPTCHA_NOT_EXPIRED(400, "CAPTCHA_NOT_EXPIRED"),
    // 身份验证失败
    AUTHENTICATION_FAILED(401, "AUTHENTICATION_FAILED"),
    // 权限不能为空
    SCOPE_NOT_EMPTY(400, "SCOPE_NOT_EMPTY"),
    // 用户名不能为空
    USERNAME_NOT_EMPTY(400, "USERNAME_NOT_EMPTY"),
    // 密码不能为空
    PASSWORD_NOT_EMPTY(400, "PASSWORD_NOT_EMPTY"),
    // 手机号不能为空
    MOBILE_NOT_EMPTY(400, "MOBILE_NOT_EMPTY"),
    // 邮箱不能为空
    EMAIL_NOT_EMPTY(400, "EMAIL_NOT_EMPTY"),
    // 不支持的登录方式
    UNSUPPORTED_LOGIN_TYPE(400, "UNSUPPORTED_LOGIN_TYPE"),
    // 账号不存在
    ACCOUNT_NOT_EXIST(400, "ACCOUNT_NOT_EXIST"),


    /* 文件上传 */
    FILE_UPLOAD_ERROR(400, "FILE_UPLOAD_ERROR"),
    IMG_TYPE_ERROR(400, "IMG_TYPE_ERROR"),
    VIDEO_TYPE_ERROR(400, "VIDEO_TYPE_ERROR"),
    AUDIO_TYPE_ERROR(400, "AUDIO_TYPE_ERROR"),
    DOC_TYPE_ERROR(400, "DOC_TYPE_ERROR"),
    FILE_MERGE_ERROR(400, "FILE_MERGE_ERROR"),

    // 5XX服务器错误响应
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),




    ;

    private final Integer code;

    private final String key;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.key = message;
    }

    public String getMessage() {
        return I18nUtils.getMessage(this.key, Locale.CHINA);
    }
}
