package com.autumn.auth.authorization.handler;

import jakarta.annotation.Nullable;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * @author autumn
 * @desc 自定义令牌刷新
 * @date 2025/5/12 22:12
 **/
public class RefreshTokenGeneratorHandler implements OAuth2TokenGenerator<OAuth2RefreshToken> {

    private final OAuth2RefreshTokenGenerator delegate = new OAuth2RefreshTokenGenerator();

    @Nullable
    @Override
    public OAuth2RefreshToken generate(OAuth2TokenContext context) {
        // if (context.getAuthorizedScopes().contains(OidcScopes.OPENID) && !context.getAuthorizedScopes().contains("offline_access")) {
        //     return null;
        // }
        // // 生成 refresh_token 逻辑
        // String tokenValue = UuidUtils.getRandomUuid();
        // Instant issuedAt = Instant.now();
        // // 30 days
        // Instant expiresAt = issuedAt.plusSeconds(2592000);
        // return new OAuth2RefreshToken(tokenValue, issuedAt, expiresAt);
        return delegate.generate(context);
    }
}
