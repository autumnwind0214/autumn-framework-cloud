package com.autumn.auth.authorization.handler;

import com.alibaba.fastjson2.JSONObject;
import com.autumn.common.core.result.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author autumn
 * @desc access_token请求失败处理器
 * @date 2025/5/12 22:12
 **/
@Slf4j
public class AccessTokenFailureResponseHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("请求失败, 原因: {}", exception.getMessage());
        // 登录失败, 写回401与具体的异常
        R<String> fail = R.fail(exception.getMessage());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSONObject.toJSONString(fail));
        response.getWriter().flush();
    }
}
