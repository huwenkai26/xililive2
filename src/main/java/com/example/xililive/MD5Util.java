package com.example.xililive;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String MD5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}