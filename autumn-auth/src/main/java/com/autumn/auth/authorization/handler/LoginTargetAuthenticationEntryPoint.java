package com.autumn.auth.authorization.handler;

import com.autumn.common.core.result.R;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.core.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author autumn
 * @desc 重定向至登录处理
 * @date 2025/5/12 22:18
 **/
@Slf4j
public class LoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    /**
     * 设备码认证页面
     */
    private final String deviceActivateUri;

    /**
     * APP 登录页面
     */
    private final String appLoginUrl;

    /**
     *
     */

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * @param loginFormUrl      URL where the login page can be found. Should either be
     *                          relative to the web-app context path (include a leading {@code /}) or an absolute
     *                          URL.
     * @param deviceActivateUri 设备码验证页面地址
     */
    public LoginTargetAuthenticationEntryPoint(String loginFormUrl, String deviceActivateUri, String appLoginUrl) {
        super(loginFormUrl);
        this.deviceActivateUri = deviceActivateUri;
        this.appLoginUrl = appLoginUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException,
            ServletException {
        // String deviceVerificationUri = "/oauth2/device_verification";
        // 兼容设备码前后端分离
        // if (request.getRequestURI().equals(deviceVerificationUri)
        //         && request.getMethod().equals(HttpMethod.POST.name())
        //         && UrlUtils.isAbsoluteUrl(deviceActivateUri)) {
        //     // 如果是请求验证设备激活码(user_code)时未登录并且设备码验证页面是前后端分离的那种则写回json
        //     R<String> success = R.fail("登录已失效，请重新打开设备提供的验证地址");
        //     response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        //     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //     response.getWriter().write(JsonUtils.objectCovertToJson(success));
        //     response.getWriter().flush();
        //     return;
        // }

        // 获取登录表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            // 不是绝对路径调用父类方法处理
            super.commence(request, response, authException);
            return;
        }

        R<String> success = R.fail(ResultCodeEnum.LOGIN_INVALID);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtils.objectCovertToJson(success));
        response.getWriter().flush();

        // StringBuffer requestUrl = request.getRequestURL();
        // if (!ObjectUtils.isEmpty(request.getQueryString())) {
        //     requestUrl.append("?").append(request.getQueryString());
        // }
        //
        // // 绝对路径在重定向前添加target参数
        // String targetParameter = URLEncoder.encode(requestUrl.toString(), StandardCharsets.UTF_8);
        // String targetUrl = loginForm + "?target=" + targetParameter;
        // log.debug("重定向至前后端分离的登录页面：{}", targetUrl);
        // this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}