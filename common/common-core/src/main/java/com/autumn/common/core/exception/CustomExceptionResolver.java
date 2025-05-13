package com.autumn.common.core.exception;

import com.autumn.common.core.result.R;

/**
 * @author autumn
 * @desc 抽象异常处理机制
 * @date 2025年05月13日
 */
public interface CustomExceptionResolver {

    boolean supports(Throwable exception);

    R<String> resolve(Throwable exception);
}
