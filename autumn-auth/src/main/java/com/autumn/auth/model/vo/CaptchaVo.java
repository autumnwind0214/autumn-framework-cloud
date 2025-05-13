package com.autumn.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author autumn
 * @desc 获取验证码返回
 * @date 2025年05月07日
 */
@Data
@AllArgsConstructor
public class CaptchaVo {

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码的值
     */
    private String code;

    /**
     * 图片验证码的base64值
     */
    private String imageData;
}
