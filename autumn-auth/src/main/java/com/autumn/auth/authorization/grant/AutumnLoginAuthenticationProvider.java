package com.autumn.auth.authorization.grant;


import com.autumn.auth.authorization.captcha.CaptchaAuthenticationProvider;
import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.exception.InvalidCaptchaException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.redis.constant.RedisConstant;
import com.autumn.common.redis.core.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 短信验证码校验实现
 *
 * @author vains
 */
@Slf4j
@Component
public class AutumnLoginAuthenticationProvider extends CaptchaAuthenticationProvider {

    private final RedisOperator<String> redisOperator;

    /**
     * 利用构造方法在通过{@link Component}注解初始化时
     * 注入UserDetailsService和passwordEncoder，然后
     * 设置调用父类关于这两个属性的set方法设置进去
     *
     * @param userDetailsService 用户服务，给框架提供用户信息
     * @param passwordEncoder    密码解析器，用于加密和校验密码
     */
    public AutumnLoginAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                             RedisOperator<String> redisOperator) {
        super(userDetailsService, passwordEncoder, redisOperator);
        this.redisOperator = redisOperator;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("Authenticate sms captcha...");

        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(ResultCodeEnum.CAPTCHA_NOT_EMPTY.getMessage());
        }

        // 获取当前request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BadCredentialsException(ResultCodeEnum.BAD_REQUEST.getMessage());
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录方式
        // String loginType = request.getParameter("loginType");
        // 获取grant_type
        String grantType = request.getParameter("grant_type");
        // 短信登录和自定义短信认证grant type会走下方认证
        // 如果是自定义密码模式则下方的认证判断只要判断下loginType即可
        // if (Objects.equals(loginType, SecurityConstants.SMS_LOGIN_TYPE)) {}
        if (Objects.equals(grantType, SecurityConstants.SMS_LOGIN_TYPE)) {
            String captcha = redisOperator.get(RedisConstant.SMS_CAPTCHA_PREFIX_KEY + authentication.getPrincipal());
            if (!Objects.equals(captcha, authentication.getCredentials())) {
                throw new InvalidCaptchaException(ResultCodeEnum.CAPTCHA_INCORRECT);
            }
        } else if (Objects.equals(grantType, SecurityConstants.EMAIL_LOGIN_TYPE)) {
            String captcha = redisOperator.get(RedisConstant.EMAIL_CAPTCHA_PREFIX_KEY + authentication.getPrincipal());
            if (!Objects.equals(captcha, authentication.getCredentials())) {
                throw new InvalidCaptchaException(ResultCodeEnum.CAPTCHA_INCORRECT);
            }
        } else {
            log.info("Not sms captcha loginType, exit.");
            // 其它调用父类默认实现的密码方式登录
            super.additionalAuthenticationChecks(userDetails, authentication);
        }

        log.info("Authenticated sms captcha.");
    }
}
