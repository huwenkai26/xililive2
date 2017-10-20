package com.example.xililive;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    public static final String DEFAULT_KEY = "1400009129000000";

    public static String decrypt(String content, String key) {
        try {
            byte[] contentBytes = Base64.getDecoder().decode(content);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, skeySpec);
            byte[] decryptResult = cipher.doFinal(contentBytes);
            if (decryptResult != null) {
                return new String(decryptResult, "UTF-8");
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content) {
        return decrypt(content, DEFAULT_KEY);
    }

    public static String encrypt(String content) {
        return encrypt(content, DEFAULT_KEY);
    }

    public static String encrypt(String content, String key) {
        byte[] encryptResult = null;
        try {
            byte[] contentBytes = content.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, skeySpec);
            encryptResult = cipher.doFinal(contentBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (encryptResult != null) {
            return Base64.getEncoder().encodeToString(encryptResult);
        }
        return null;
    }
}