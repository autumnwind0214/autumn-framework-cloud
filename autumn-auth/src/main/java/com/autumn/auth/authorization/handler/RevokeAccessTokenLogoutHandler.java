package com.autumn.auth.authorization.handler;

import com.autumn.common.core.result.R;
import com.autumn.common.core.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author autumn
 * @desc 登出处理-撤销token
 * @date 2025/5/12 22:18
 **/
@Slf4j
@RequiredArgsConstructor
public class RevokeAccessTokenLogoutHandler implements LogoutHandler {

    private static final String HOST_URL = "http://127.0.0.1:";

    private final ServerProperties serverProperties;

    private final AuthorizationServerSettings authorizationServerSettings;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @SneakyThrows
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 获取token(可以是Access Token，也可以是Refresh Token)
        String token = request.getParameter(OAuth2ParameterNames.TOKEN);
        if (ObjectUtils.isEmpty(token)) {
            // 写回json数据
            R<String> result = R.fail("token is empty");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtils.objectCovertToJson(result));
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        // 撤销token逻辑
        Integer port = serverProperties.getPort();

        // 撤销token地址
        String revokeUrl = HOST_URL
                + (port == null ? "8080" : port)
                // 默认是/oauth2/revoke
                + authorizationServerSettings.getTokenRevocationEndpoint();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(OAuth2ParameterNames.TOKEN, token);

        // 准备请求的header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("messaging-client", "123456", StandardCharsets.UTF_8));

        // 封装请求
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
        // 撤销token，默认无响应
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(revokeUrl, entity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("revoked access token");
            }
        } catch (Exception e) {
            log.warn("revoked access token failed, cause: {}", e.getMessage());
            // 撤销token失败写回json数据
            R<String> result = R.fail("revoked access token failed, cause: " + e.getMessage());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtils.objectCovertToJson(result));
            response.getWriter().flush();
            response.getWriter().close();
        }

    }
}
