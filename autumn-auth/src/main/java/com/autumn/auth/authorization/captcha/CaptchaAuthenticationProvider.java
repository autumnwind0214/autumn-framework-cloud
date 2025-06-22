package com.autumn.auth.authorization.captcha;

import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.exception.InvalidCaptchaException;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.redis.constant.RedisConstant;
import com.autumn.common.redis.core.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author autumn
 * @desc 验证码校验
 * @date 2025/5/11 21:02
 **/
@Slf4j
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {

    private final RedisOperator<String> redisOperator;

    /**
     * 利用构造方法在通过{@link Component}注解初始化时
     * 注入UserDetailsService和passwordEncoder，然后
     * 设置调用父类关于这两个属性的set方法设置进去
     *
     * @param userDetailsService 用户服务，给框架提供用户信息
     * @param passwordEncoder    密码解析器，用于加密和校验密码
     */
    public CaptchaAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, RedisOperator<String> redisOperator) {
        super.setPasswordEncoder(passwordEncoder);
        super.setUserDetailsService(userDetailsService);
        this.redisOperator = redisOperator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticate captcha...");

        // 获取当前request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new InvalidCaptchaException(ResultCodeEnum.BAD_REQUEST);
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录方式
        String grantType = request.getParameter(SecurityConstants.GRANT_TYPE);
        if (Objects.equals(grantType, SecurityConstants.PASSWORD_LOGIN_TYPE)) {
            // 只要不是密码登录都不需要校验图形验证码
            log.info("It isn't necessary captcha authenticate.");
            return super.authenticate(authentication);
        }

        // 获取参数中的验证码
        String code = request.getParameter(SecurityConstants.OAUTH_PARAMETER_NAME_CODE);
        if (ObjectUtils.isEmpty(code)) {
            throw new InvalidCaptchaException(ResultCodeEnum.CAPTCHA_NOT_EMPTY);
        }

        String captchaId = request.getParameter(SecurityConstants.OAUTH_PARAMETER_NAME_CAPTCHA);
        // 获取缓存中存储的验证码
        String captchaCode = redisOperator.get((RedisConstant.IMAGE_CAPTCHA_PREFIX_KEY + captchaId));
        // String captchaCode = "1234";
        if (!ObjectUtils.isEmpty(captchaCode)) {
            if (!captchaCode.equalsIgnoreCase(code)) {
                throw new InvalidCaptchaException(ResultCodeEnum.CAPTCHA_INCORRECT);
            }
            redisOperator.delete(RedisConstant.IMAGE_CAPTCHA_PREFIX_KEY + captchaId);
        } else {
            throw new InvalidCaptchaException(ResultCodeEnum.CAPTCHA_NOT_EXPIRED);
        }

        log.info("Captcha authenticated.");
        try {
            return super.authenticate(authentication);
        } catch (Exception e) {
            log.error("err: ", e);
            throw new AutumnException(ResultCodeEnum.AUTHENTICATION_FAILED);
        }
    }
}
