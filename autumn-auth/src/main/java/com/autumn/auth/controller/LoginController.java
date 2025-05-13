package com.autumn.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.autumn.common.core.result.R;
import com.autumn.common.redis.constant.RedisConstant;
import com.autumn.common.redis.core.RedisOperator;
import com.autumn.auth.model.vo.CaptchaVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author autumn
 * @desc 登录接口
 * @date 2025年05月07日
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final RedisOperator<String> redisOperator;

    @GetMapping("/getCaptcha")
    public R<CaptchaVo> getCaptcha() {
        // 使用huTool-captcha生成图形验证码
        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 34, 4, 2);
        // 生成一个唯一id
        long id = IdWorker.getId();
        // 存入缓存中，5分钟后过期
        redisOperator.set((RedisConstant.IMAGE_CAPTCHA_PREFIX_KEY + id), captcha.getCode(), RedisConstant.DEFAULT_TIMEOUT_SECONDS);
        CaptchaVo vo = new CaptchaVo(String.valueOf(id), captcha.getCode(), captcha.getImageBase64Data());
        return R.success(vo);
    }

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     */
    @GetMapping("/getSmsCaptcha/{mobile}")
    public R<Boolean> getSmsCaptcha(@PathVariable String mobile) {
        String code = "123456";
        // 存入缓存，5分钟后过期
        redisOperator.set((RedisConstant.SMS_CAPTCHA_PREFIX_KEY + mobile), code, RedisConstant.DEFAULT_TIMEOUT_SECONDS);
        return R.success();
    }

    @GetMapping("/getEmailCaptcha/{email}")
    public R<Boolean> getEmailCaptcha(@PathVariable String email) {
        String code = "123456";
        // 存入缓存，5分钟后过期
        redisOperator.set((RedisConstant.EMAIL_CAPTCHA_PREFIX_KEY + email), code, RedisConstant.DEFAULT_TIMEOUT_SECONDS);
        return R.success();
    }

    /**
     * 生成指定位数的短信验证码
     *
     * @param length 验证码长度
     * @return 生成的验证码
     */
    public String generateVerificationCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // 生成0-9之间的随机数字
            int digit = random.nextInt(10);
            code.append(digit);
        }

        return code.toString();
    }
}
