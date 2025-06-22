package com.autumn.common.auth.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 工具类（使用私钥解密）
 *
 * @author autumn
 **/
@Slf4j
public class RSAUtils {

    // 算法名称
    private static final String ALGORITHM = "RSA";

    public static final String RSA_PUBLIC_KEY = "rsa:public:key";

    public static final String RSA_PRIVATE_KEY = "rsa:private:key";

    /**
     * 使用公钥加密数据
     *
     * @param data      待加密的数据
     * @param publicKey 公钥（Base64 编码字符串）
     * @return 加密后的数据（Base64 编码字符串）
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(spec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 使用私钥解密数据
     *
     * @param encryptedData 加密的数据（Base64 编码字符串）
     * @param privateKeyPem    私钥（Base64 编码字符串）
     * @return 解密后的原始数据
     */
    public static String decrypt(String encryptedData, String privateKeyPem) {
        try {
            // 1. 从 PEM 格式中提取私钥字节（去掉头尾标记和换行）
            String privateKeyContent = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", ""); // 去掉所有空白字符

            // 2. Base64 解码得到私钥的字节表示（PKCS#8 格式）
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);

            // 3. 构造 PKCS8EncodedKeySpec
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

            // 4. 使用 KeyFactory 生成 PrivateKey 对象
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(spec);

            // 5. 初始化 Cipher，使用 RSA/ECB/OAEPWithSHA-1AndMGF1Padding
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            // 6. Base64 解码加密数据
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            // 7. 解密
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 8. 转为字符串返回
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA 解密失败", e); // 如果你使用了日志框架
            // e.printStackTrace(); // 临时打印错误信息
            return "";
        }

    }
}
