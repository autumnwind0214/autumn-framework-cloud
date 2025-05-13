package com.autumn.auth.model.vo;

import lombok.Data;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.time.Instant;

/**
 * @author autumn
 * @desc 刷新令牌
 * @date 2025/5/12 22:14
 **/
@Data
public class RefreshTokenVo {

    public RefreshTokenVo(OAuth2RefreshToken refreshToken) {
        this.tokenValue = refreshToken.getTokenValue();
        this.expiresAt = refreshToken.getExpiresAt();
        this.issuedAt = refreshToken.getIssuedAt();
    }

    /**
     * 刷新令牌
     */
    private String tokenValue;

    /**
     * 过期时间
     */
    private Instant expiresAt;

    /**
     * 创建时间
     */
    private Instant issuedAt;
}
