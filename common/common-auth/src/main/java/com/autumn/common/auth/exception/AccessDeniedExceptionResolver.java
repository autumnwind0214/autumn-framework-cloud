package com.autumn.common.auth.exception;

import com.autumn.common.core.exception.CustomExceptionResolver;
import com.autumn.common.core.result.R;
import com.autumn.common.core.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * @author autumn
 * @desc 权限不足异常
 * @date 2025年05月13日
 */
@Component
public class AccessDeniedExceptionResolver implements CustomExceptionResolver {

    @Override
    public boolean supports(Throwable exception) {
        // 判断异常是否为 AccessDeniedException
        return exception instanceof AccessDeniedException;
    }

    @Override
    public R<String> resolve(Throwable exception) {
        return R.fail(ResultCodeEnum.FORBIDDEN);
    }
}
