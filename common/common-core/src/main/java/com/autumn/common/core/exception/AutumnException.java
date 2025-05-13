package com.autumn.common.core.exception;

import com.autumn.common.core.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author autumn
 * @desc 自定义全局异常类
 * @date 2025/5/11 21:04
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AutumnException extends RuntimeException {
    private Integer code;
    private String message;

    public AutumnException() {
        super(ResultCodeEnum.BAD_REQUEST.getMessage());
        this.code = ResultCodeEnum.BAD_REQUEST.getCode();
        this.message = ResultCodeEnum.BAD_REQUEST.getMessage();
    }

    public AutumnException(String message) {
        super(message);
        this.code = ResultCodeEnum.BAD_REQUEST.getCode();
        this.message = message;
    }

    /**
     * 通过状态码和错误消息创建异常对象
     */
    public AutumnException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.code = resultCodeEnum.getCode();
        this.message = message;
    }

    public AutumnException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "AutumnException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
