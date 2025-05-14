package com.autumn.auth.exception;

import com.autumn.common.core.result.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;

/**
 * @author autumn
 * @desc 校验验证码异常时抛出
 * @date 2025年05月13日
 */
public class InvalidCaptchaException extends AuthenticationException {

    public InvalidCaptchaException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
    }
}
