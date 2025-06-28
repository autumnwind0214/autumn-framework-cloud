package com.autumn.common.core.result;

import lombok.Data;

/**
 * @author autumn
 * @desc 全局统一返回结果类
 * @date 2025年05月07日
 */
@Data
public class R<T> {
    // 返回码
    private Integer code;

    // 返回消息
    private String message;

    // 返回数据
    private T data;

    public R() {
    }

    public R(String message) {
        this.message = message;
    }

    // 返回数据
    protected static <T> R<T> build(T data) {
        R<T> result = new R<T>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> R<T> build(T body, Integer code, String message) {
        R<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> R<T> build(T body, ResultCodeEnum resultCodeEnum) {
        R<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> R<T> success() {
        return R.success(null);
    }

    /**
     * 操作成功
     */
    public static <T> R<T> success(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> R<T> fail() {
        return fail(ResultCodeEnum.BAD_REQUEST);
    }

    /**
     * 操作失败
     */
    public static <T> R<T> fail(ResultCodeEnum resultCodeEnum) {
        return build(null, resultCodeEnum);
    }

    public static <T> R<T> fail(String message) {
        return build(null, ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    public R<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public R<T> code(Integer code) {
        this.setCode(code);
        return this;
    }
}
