package com.autumn.common.core.exception;

import lombok.Getter;

/**
 * @author autumn
 * @desc 通用错误信息
 * @date 2025年05月13日
 */
@Getter
public enum CommonError {

    UN_KNOW_ERROR("执行过程异常，请重试。"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private final String errMessage;

    // 单例模式
    CommonError(String errMessage) {
        this.errMessage = errMessage;
    }
}
