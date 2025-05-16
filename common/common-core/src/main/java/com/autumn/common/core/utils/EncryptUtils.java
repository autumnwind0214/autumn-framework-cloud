package com.autumn.common.core.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author autumn
 * @desc 加密解密工具类
 * @date 2025年05月14日
 */
public class EncryptUtils {

    // 16字节密钥
    private static final String KEY = "qwertyuiopasdfgh";
    // 16字节IV
    private static final String IV = "jklzxcvbnm123456";

    /**
     * AES 解密方法 (CBC 模式)
     *
     * @param encryptedData 前端加密后的 Base64 字符串
     * @return 解密后的原始字符串
     */
    public static String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedValue);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES 解密失败", e);
        }
    }

    /**
     * AES 加密方法 (CBC 模式)，与 CryptoJS 兼容
     *
     * @param content 明文字符串
     * @return Base64 编码的加密字符串
     */
    public static String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(content.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES 加密失败", e);
        }
    }


}
