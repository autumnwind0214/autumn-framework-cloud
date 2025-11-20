package com.autumn.common.core.utils;

import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;

/**
 * 校验检查工具类
 */
public class VerifyCheckUtils {

    /**
     * 校验是否修改管理员
     */
    public static void checkAdminEdit(Long userId) {
        if (userId == 1) {
            throw new AutumnException(ResultCodeEnum.BAN_OPERATION_USER);
        }
    }

    /**
     * 校验密码是否一致
     */
    public static void checkPassword(String password, String newPassword) {
        if (!password.equals(newPassword)) {
            throw new AutumnException(ResultCodeEnum.PASSWORD_NOT_EQUALS);
        }
    }


}
