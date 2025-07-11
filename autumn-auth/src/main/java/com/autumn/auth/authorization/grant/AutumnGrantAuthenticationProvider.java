package com.autumn.auth.authorization.grant;


import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.local.GrantThreadLocal;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.auth.utils.RSAUtils;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.core.utils.EncryptUtils;
import com.autumn.common.redis.core.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 短信验证码登录认证提供者
 *
 * @author vains
 */
@Slf4j
public class AutumnGrantAuthenticationProvider implements AuthenticationProvider {

    private OAuth2TokenGenerator<?> tokenGenerator;

    private AuthenticationManager authenticationManager;

    private OAuth2AuthorizationService authorizationService;

    private RedisOperator<String> redisOperator;

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AutumnGrantAuthenticationToken authenticationToken = (AutumnGrantAuthenticationToken) authentication;

        // Ensure the client is authenticated
        OAuth2ClientAuthenticationToken clientPrincipal =
                SecurityUtils.getAuthenticatedClientElseThrowInvalidClient(authenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        // Ensure the client is configured to use this authorization grant type
        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes().contains(authenticationToken.getAuthorizationGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // 验证scope
        Set<String> authorizedScopes = getAuthorizedScopes(registeredClient, authenticationToken.getScopes());

        // 进行认证
        Authentication authenticate = getAuthenticatedUser(authenticationToken);

        // 以下内容摘抄自OAuth2AuthorizationCodeAuthenticationProvider
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticate)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(authenticationToken.getAuthorizationGrantType())
                .authorizationGrant(authenticationToken);

        // Initialize the OAuth2Authorization
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                // 2023-07-15修改逻辑，加入当前用户认证信息，防止刷新token时因获取不到认证信息而抛出空指针异常
                // 存入授权scope
                .authorizedScopes(authorizedScopes)
                // 当前授权用户名称
                .principalName(authenticate.getName())
                // 设置当前用户认证信息
                .attribute(Principal.class.getName(), authenticate)
                .authorizationGrantType(authenticationToken.getAuthorizationGrantType());

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        if (log.isTraceEnabled()) {
            log.trace("Generated access token");
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated refresh token");
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        // ----- ID token -----
        OidcIdToken idToken;
        if (authorizedScopes.contains(OidcScopes.OPENID)) {
            tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    // ID token customizer may need access to the access token and/or refresh token
                    .authorization(authorizationBuilder.build())
                    .build();
            // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated id token");
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }

        OAuth2Authorization authorization = authorizationBuilder.build();

        // Save the OAuth2Authorization
        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = new HashMap<>(1);
        if (idToken != null) {
            // 放入idToken
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }

    /**
     * 获取认证过的scope
     *
     * @param registeredClient 客户端
     * @param requestedScopes  请求中的scope
     * @return 认证过的scope
     */
    private Set<String> getAuthorizedScopes(RegisteredClient registeredClient, Set<String> requestedScopes) {
        // Default to configured scopes
        Set<String> authorizedScopes = registeredClient.getScopes();
        if (!ObjectUtils.isEmpty(requestedScopes)) {
            Set<String> unauthorizedScopes = requestedScopes.stream()
                    .filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
                    .collect(Collectors.toSet());
            if (!ObjectUtils.isEmpty(unauthorizedScopes)) {
                throw new AutumnException(ResultCodeEnum.AUTHENTICATION_FAILED);
            }

            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }

        if (log.isTraceEnabled()) {
            log.trace("Validated token request parameters");
        }
        return authorizedScopes;
    }

    /**
     * 获取认证过的用户信息
     *
     * @param authenticationToken converter构建的认证信息，这里是包含手机号与验证码的
     * @return 认证信息
     */
    public Authentication getAuthenticatedUser(AutumnGrantAuthenticationToken authenticationToken) {
        Authentication authenticate = null;
        UsernamePasswordAuthenticationToken unauthenticated = null;
        Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
        String grantType = authenticationToken.getAuthorizationGrantType().getValue();
        GrantThreadLocal.setGrantType(grantType);
        switch (grantType) {
            case SecurityConstants.SMS_LOGIN_TYPE -> {

                // 短信验证码登录
                String phone = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE);
                String smsCaptcha = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PHONE_CAPTCHA);
                // 构建UsernamePasswordAuthenticationToken通过AbstractUserDetailsAuthenticationProvider及其子类对手机号与验证码进行校验
                unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(phone, smsCaptcha);
            }
            case SecurityConstants.PASSWORD_LOGIN_TYPE -> {
                // 密码登录
                String username = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_USERNAME);
                String password = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_PASSWORD);
                String privateKey = redisOperator.get(RSAUtils.RSA_PRIVATE_KEY);
                // 密码解密
                password = RSAUtils.decrypt(password, privateKey);
                // 构建UsernamePasswordAuthenticationToken通过AbstractUserDetailsAuthenticationProvider及其子类对用户名与密码进行校验
                unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            }
            case SecurityConstants.EMAIL_LOGIN_TYPE -> {
                // 邮箱验证码登录
                String email = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL);
                String emailCaptcha = (String) additionalParameters.get(SecurityConstants.OAUTH_PARAMETER_NAME_EMAIL_CAPTCHA);
                // 构建UsernamePasswordAuthenticationToken通过AbstractUserDetailsAuthenticationProvider及其子类对邮箱与验证码进行校验
                unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(email, emailCaptcha);
            }
            default -> {
                log.info("Not loginType, exit.");
                // 其它调用父类默认实现的密码方式登录
                unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(authenticationToken.getPrincipal(),
                        authenticationToken.getCredentials());
            }
        }

        try {
            authenticate = authenticationManager.authenticate(unauthenticated);
        } catch (Exception e) {
            log.error("Authentication failed.", e);
            SecurityUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    StringUtils.hasText(e.getMessage()) ? e.getMessage() : "认证失败",
                    ERROR_URI
            );
        } finally {
            GrantThreadLocal.clear();
        }
        return authenticate;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AutumnGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setTokenGenerator(OAuth2TokenGenerator<?> tokenGenerator) {
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.tokenGenerator = tokenGenerator;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        Assert.notNull(authorizationService, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
    }

    public void setAuthorizationService(OAuth2AuthorizationService authorizationService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.authorizationService = authorizationService;
    }

    public void setRedisOperator(RedisOperator<String> redisOperator) {
        Assert.notNull(redisOperator, "redisOperator cannot be null");
        this.redisOperator = redisOperator;
    }
}
