package com.autumn.auth.constant;

/**
 * @author autumn
 * @desc security 常量类
 * @date 2025/5/11 21:06
 **/
public class SecurityConstants {

    /**
     * 登录方式请求参数名
     */
    public static final String LOGIN_TYPE = "loginType";

    /**
     * 用户唯一id
     */
    public static final String TOKEN_UNIQUE_ID = "uniqueId";

    /**
     * 登录方式——短信验证码
     */
    public static final String SMS_LOGIN_TYPE = "sms_type";

    /**
     * 登录方式——账号密码登录
     */
    public static final String PASSWORD_LOGIN_TYPE = "password_type";

    /**
     * 登录方式——邮箱验证码
     */
    public static final String EMAIL_LOGIN_TYPE = "email_type";

    /**
     * 自定义登录方式
     */
    public static final String[] GRANT_TYPES = new String[]{SMS_LOGIN_TYPE, PASSWORD_LOGIN_TYPE, EMAIL_LOGIN_TYPE};

    /**
     * 手机号参数名
     */
    public static final String OAUTH_PARAMETER_NAME_PHONE = "phone";

    /**
     * 手机验证码参数名
     */
    public static final String OAUTH_PARAMETER_NAME_PHONE_CAPTCHA = "phoneCaptcha";

    /**
     * 邮箱参数名
     */
    public static final String OAUTH_PARAMETER_NAME_EMAIL = "email";

    /**
     * 邮箱验证码参数名
     */
    public static final String OAUTH_PARAMETER_NAME_EMAIL_CAPTCHA = "emailCaptcha";

    /**
     * 用户名参数名
     */
    public static final String OAUTH_PARAMETER_NAME_USERNAME = "username";

    /**
     * 密码参数名
     */
    public static final String OAUTH_PARAMETER_NAME_PASSWORD = "password";

    /**
     * 图形验证码参数名
     */
    public static final String OAUTH_PARAMETER_NAME_CODE = "verifyCode";

    /**
     * 图形验证码id参数名
     */
    public static final String OAUTH_PARAMETER_NAME_CAPTCHA = "captchaId";

}
