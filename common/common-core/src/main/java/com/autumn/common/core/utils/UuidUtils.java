package com.autumn.common.core.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author autumn
 * @desc uuid工具类
 * @date 2025年05月14日
 */
public class UuidUtils {

    public static String getUuid() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    public static String getRandomUuid() {
        return UUID.randomUUID().toString().trim();
    }

    /**
     * 随机生成一个中国手机号码.
     *
     * @return 生成的手机号码字符串
     */
    public static String generatePhoneNumber() {
        // 创建一个Random对象用于生成随机数
        Random random = new Random();

        // 可能的前三位号码
        int[] firstThreeDigits = {130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                145, 147, 149,
                150, 151, 152, 153, 155, 156, 157, 158, 159,
                162, 165, 166,
                170, 171, 172, 173, 175, 176, 177, 178, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189,
                190, 191, 195, 197, 198, 199};

        // 随机选择前三位号码
        int prefix = firstThreeDigits[random.nextInt(firstThreeDigits.length)];

        // 随机生成后八位号码
        int suffix = random.nextInt(99999999);

        // 拼接完整的电话号码
        // 使用 %07d 和 %08d 确保不会出现前导零
        return String.format("%d%08d", prefix, suffix);
    }
}
