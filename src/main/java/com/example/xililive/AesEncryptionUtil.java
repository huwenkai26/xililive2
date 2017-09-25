package com.example.xililive;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionUtil {
//    d9q9Ipl6Cj5gKhyBuRHZO0fA6ie/SZHwWKSCn0UzT5xXIoyWTu7LmeXRatGrhz/Dl6oeGqka8eonoLSeqPsLFXA+BqvE9Amnbtyg2e5gk1pRqeEPyK0orjr06c0UO3xPfseruav053PvLddI2swj2zgfzRvBUjAQYhbwkbLM+sQ=
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    public static boolean DEBUG_LOG_ENABLED = false;
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String TAG = "AESCrypt";

    private static final String theIV = "0983506863098336";
    private static final byte[] ivBytes = theIV.getBytes();
    private static final String theKEY = "302skfjna443ude5";

    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] key = password.getBytes("UTF-8");
        log("SHA-256 key ", key);
        return new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String password, String message) throws GeneralSecurityException {
        try {
            SecretKeySpec key = generateKey(password);
            log("message", message);
            String encoded = Base64.getEncoder().encodeToString(encrypt(key, ivBytes, message.getBytes("UTF-8")));
            log("Base64.NO_WRAP", encoded);
            return encoded;
        } catch (UnsupportedEncodingException e) {

        }
        return "";
    }

    public static byte[] encrypt(SecretKeySpec key, byte[] iv, byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(1, key, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(message);
        log("cipherText", cipherText);
        return cipherText;
    }

    public static String decrypt(String password, String base64EncodedCipherText) throws GeneralSecurityException {
        try {
            SecretKeySpec key = generateKey(password);
            log("base64EncodedCipherText", base64EncodedCipherText);
            byte[] decodedCipherText = Base64.getDecoder().decode(base64EncodedCipherText);
            log("decodedCipherText", decodedCipherText);
            byte[] decryptedBytes = decrypt(key,"9485871786457623".getBytes(), decodedCipherText);
            log("decryptedBytes", decryptedBytes);
            String message = new String(decryptedBytes, "UTF-8");
            log("message", message);
            return message;
        } catch (UnsupportedEncodingException e) {
            if (DEBUG_LOG_ENABLED) {

            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] decrypt(SecretKeySpec key, byte[] iv, byte[] decodedCipherText) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(2, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
        log("decryptedBytes", decryptedBytes);
        return decryptedBytes;
    }

    private static void log(String what, byte[] bytes) {
        if (DEBUG_LOG_ENABLED) {

        }
    }

    private static void log(String what, String value) {
        if (DEBUG_LOG_ENABLED) {

        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    private AesEncryptionUtil() {
    }

    public static String encrypt(String message) {
        try {
            return encrypt(theKEY, message);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt(String base64EncodedCipherText) {
        try {
            return decrypt("y19tf7wjntpnxo62", base64EncodedCipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}