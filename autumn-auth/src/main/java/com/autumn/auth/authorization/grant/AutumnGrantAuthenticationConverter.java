package com.autumn.auth.authorization.grant;


import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 短信验证码登录Token转换器
 *
 * @author vains
 */
public class AutumnGrantAuthenticationConverter implements AuthenticationConverter {

    static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!Arrays.asList(SecurityConstants.GRANT_TYPES).contains(grantType)) {
            return null;
        }

        // 这里目前是客户端认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 获取请求中的参数
        MultiValueMap<String, String> parameters = SecurityUtils.getParameters(request);

        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throw new AutumnException(ResultCodeEnum.SCOPE_NOT_EMPTY);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        parameterVerification(parameters, grantType);

        // 提取附加参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        // 构建AbstractAuthenticationToken子类实例并返回
        return new AutumnGrantAuthenticationToken(new AuthorizationGrantType(grantType), clientPrincipal, requestedScopes, additionalParameters);
    }

    private void parameterVerification(MultiValueMap<String, String> parameters, String grantType) {
        switch (grantType) {
            case (SecurityConstants.SMS_LOGIN_TYPE) -> {
                // Mobile phone number (REQUIRED)
                String phone = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE);
                if (!StringUtils.hasText(phone) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.MOBILE_NOT_EMPTY);
                }

                // SMS verification code (REQUIRED)
                String smsCode = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE_CAPTCHA);
                if (!StringUtils.hasText(smsCode) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE_CAPTCHA).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.CAPTCHA_NOT_EMPTY);
                }
            }
            case (SecurityConstants.EMAIL_LOGIN_TYPE) -> {
                // Email (REQUIRED)
                String email = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL);
                if (!StringUtils.hasText(email) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.EMAIL_NOT_EMPTY);
                }
                // Email verification code (REQUIRED)
                String emailCode = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL_CAPTCHA);
                if (!StringUtils.hasText(emailCode) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL_CAPTCHA).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.CAPTCHA_NOT_EMPTY);
                }
            }
            case (SecurityConstants.PASSWORD_LOGIN_TYPE) -> {
                // Username (REQUIRED)
                String account = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_ACCOUNT);
                if (!StringUtils.hasText(account) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_ACCOUNT).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.ACCOUNT_NOT_EMPTY);
                }
                String password = parameters.getFirst(SecurityConstants.OAUTH_PARAMETER_NAME_PASSWORD);
                if (!StringUtils.hasText(password) || parameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PASSWORD).size() != 1) {
                    throw new AutumnException(ResultCodeEnum.PASSWORD_NOT_EMPTY);
                }
            }
            default -> {
                // 不支持的登录方式
                throw new AutumnException(ResultCodeEnum.UNSUPPORTED_LOGIN_TYPE);
            }
        }
    }
}
