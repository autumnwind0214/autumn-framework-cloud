package com.autumn.auth.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author autumn
 * @desc RSA密钥对工具类
 * @date 2025年05月14日
 */
public class RsaKeyUtils {
    private static final String PUBLIC_KEY_PATH = "classpath:rsa/public.key";
    private static final String PRIVATE_KEY_PATH = "classpath:rsa/private.key";

    public static KeyPair loadKeyPair() throws Exception {
        Resource publicKeyResource = new ClassPathResource("rsa/public.key");
        Resource privateKeyResource = new ClassPathResource("rsa/private.key");

        if (!publicKeyResource.exists() || !privateKeyResource.exists()) {
            return generateAndSaveKeyPair();
        }

        String publicKeyPEM = readKey(publicKeyResource);
        String privateKeyPEM = readKey(privateKeyResource);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM));
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));

        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");

        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    private static String readKey(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return StreamUtils.copyToString(is, java.nio.charset.StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
        }
    }

    private static KeyPair generateAndSaveKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        saveKeyToFile(keyPair.getPublic().getEncoded(), "rsa/public.key", "PUBLIC");
        saveKeyToFile(keyPair.getPrivate().getEncoded(), "rsa/private.key", "PRIVATE");

        return keyPair;
    }

    private static void saveKeyToFile(byte[] keyBytes, String path, String type) throws IOException {
        File file = new File("src/main/resources/" + path);
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(("-----BEGIN " + type + " KEY-----\n")
                    .getBytes());
            fos.write(Base64.getEncoder().encode(keyBytes));
            fos.write(("\n-----END " + type + " KEY-----\n")
                    .getBytes());
        }
    }

    public static void main(String[] args) throws Exception {
        loadKeyPair();
    }
}
