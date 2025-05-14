package com.autumn.auth.authorization.federation;

import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.model.auth.BasicOAuth2User;
import com.autumn.auth.model.auth.BasicOidcUser;
import com.autumn.common.auth.constant.GrantAuthConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author autumn
 * @desc 生成的 OAuth2 令牌添加自定义的属性
 * @date 2025/5/12 22:03
 **/
public class FederatedIdentityIdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Set<String> ID_TOKEN_CLAIMS = Set.of(
            IdTokenClaimNames.ISS,
            IdTokenClaimNames.SUB,
            IdTokenClaimNames.AUD,
            IdTokenClaimNames.EXP,
            IdTokenClaimNames.IAT,
            IdTokenClaimNames.AUTH_TIME,
            IdTokenClaimNames.NONCE,
            IdTokenClaimNames.ACR,
            IdTokenClaimNames.AMR,
            IdTokenClaimNames.AZP,
            IdTokenClaimNames.AT_HASH,
            IdTokenClaimNames.C_HASH
    );

    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> thirdPartyClaims = extractClaims(context.getPrincipal());
            context.getClaims().claims(existingClaims -> {
                // Remove conflicting claims set by this authorization server
                existingClaims.keySet().forEach(thirdPartyClaims::remove);

                // Remove standard id_token claims that could cause problems with clients
                ID_TOKEN_CLAIMS.forEach(thirdPartyClaims::remove);

                // Add all other claims directly to id_token
                existingClaims.putAll(thirdPartyClaims);
            });
        }

        // 检查登录用户信息是不是OAuth2User，在token中添加loginType属性
        if (context.getPrincipal().getPrincipal() instanceof BasicOAuth2User user) {
            JwtClaimsSet.Builder claims = context.getClaims();
            // 同时检验是否为String和是否不为空
            claims.claim(SecurityConstants.LOGIN_TYPE, user.getType());
            claims.claim(SecurityConstants.TOKEN_UNIQUE_ID, user.getUniqueId());

            // 获取用户的权限
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            transferToContext(authorities, context);
        }

        // 检查登录用户信息是不是UserDetails，排除掉没有用户参与的流程
        if (context.getPrincipal().getPrincipal() instanceof UserDetails user) {
            JwtClaimsSet.Builder claims = context.getClaims();
            // 获取用户的权限
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            claims.claim(SecurityConstants.TOKEN_UNIQUE_ID, user.getUsername());
            transferToContext(authorities, context);
        }
    }

    /**
     * 从认证信息中提取 Claims
     *
     * @param principal 当前登录用户认证信息
     * @return 当前用户的信息
     */
    private Map<String, Object> extractClaims(Authentication principal) {
        Map<String, Object> claims;
        if (principal.getPrincipal() instanceof BasicOidcUser oidcUser) {
            OidcIdToken idToken = oidcUser.getIdToken();
            claims = idToken.getClaims();
        } else if (principal.getPrincipal() instanceof BasicOAuth2User oAuth2User) {
            claims = oAuth2User.getAttributes();
        } else {
            claims = Collections.emptyMap();
        }

        return new HashMap<>(claims);
    }

    private void transferToContext(Collection<? extends GrantedAuthority> authorities, JwtEncodingContext context) {
        Set<String> collect = Optional.ofNullable(authorities).orElse(Collections.emptyList()).stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        JwtClaimsSet.Builder claims = context.getClaims();
        claims.claim(GrantAuthConstant.AUTHORITIES_KEY, collect);
    }
}
