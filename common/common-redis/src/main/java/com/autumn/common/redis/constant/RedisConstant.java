package com.autumn.common.redis.constant;

/**
 * @author autumn
 * @desc redis常量
 * @date 2024年11月14日
 */
public class RedisConstant {

    // 用户 token 缓存前缀
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";

    // token 过期时间
    public static final int USER_LOGIN_KEY_TIMEOUT = 60 * 60 * 24 * 100;

    /**
     * 字典信息
     */
    public static final String SYS_DICT = "sys_dict";
    public static final int SYS_DICT_KEY_TIMEOUT = 2;

    /**
     * jwk set缓存前缀
     */
    public static final String AUTHORIZATION_JWS_PREFIX_KEY = "authorization_jws";

    /**
     * 认证信息存储前缀
     */
    public static final String SECURITY_CONTEXT_PREFIX_KEY = "security_context:";

    /**
     * 短信验证码前缀
     */
    public static final String SMS_CAPTCHA_PREFIX_KEY = "mobile_phone:";

    /**
     * 邮箱验证码前缀
     */
    public static final String EMAIL_CAPTCHA_PREFIX_KEY = "email:";

    /**
     * 图形验证码前缀
     */
    public static final String IMAGE_CAPTCHA_PREFIX_KEY = "image_captcha:";

    /**
     * 默认过期时间，默认五分钟
     */
    public static final long DEFAULT_TIMEOUT_SECONDS = 60L * 5;

    // 授权请求的 Redis Key 前缀
    public static final String OAUTH2_AUTHORIZATION_REQUEST_KEY_PREFIX = "oauth2:authorization:request:";

    /**
     * 菜单路由前缀
     */
    public static final String ASYNC_ROUTES_PREFIX_KEY = "async:routes:";

}
