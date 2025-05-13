package com.autumn.auth.authorization.handler;

import com.alibaba.fastjson2.JSONObject;
import com.autumn.common.core.result.R;
import com.autumn.auth.model.vo.AccessTokenVo;
import com.autumn.auth.model.vo.RefreshTokenVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author autumn
 * @desc access_token请求成功处理器
 * @date 2025/5/12 22:13
 **/
@Slf4j
public class AccessTokenSuccessResponseHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 删除当前会话（如果存在）
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.debug("Invalidated existing session during token issuance");
        }
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        AccessTokenVo vo = new AccessTokenVo();
        OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
        vo.setAccessToken(authenticationToken.getAccessToken().getTokenValue());
        vo.setScope(authenticationToken.getAccessToken().getScopes());
        vo.setExpiresAt(authenticationToken.getAccessToken().getExpiresAt());
        vo.setIssuedAt(authenticationToken.getAccessToken().getIssuedAt());
        if (Objects.nonNull(authenticationToken.getRefreshToken())) {
            RefreshTokenVo refreshToken = new RefreshTokenVo(authenticationToken.getRefreshToken());
            vo.setRefreshToken(refreshToken);
        }

        R<AccessTokenVo> success = R.success(vo);
        response.getWriter().write(JSONObject.toJSONString(success));
        response.getWriter().flush();
    }
}
