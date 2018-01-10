package com.fafabtc.common.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class HashUtils {

    private static final char[] HEX_DIGIT = "0123456789abcdef".toCharArray();

    /**
     * 字节数组转16进制字符串表示。
     *
     * @param data
     * @return
     */
    public static String hexToString(byte[] data) {
        int len = data.length;
        char[] hexChars = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            int v = data[i] & 0xff;
            hexChars[j++] = HEX_DIGIT[(v >>> 4) & 0x0F];
            hexChars[j++] = HEX_DIGIT[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * md5哈希函数。
     *
     * @param input
     * @return
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(input.getBytes());
            return hexToString(result);
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    /**
     * sha256哈希函数。
     *
     * @param input
     * @return
     */
    public static String sha256(String input) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] result = sha.digest(input.getBytes("UTF-8"));
            return hexToString(result);
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public static String hmacSha256(String key, String input) {
        try {
            final String algorithm = "HmacSHA256";
            Mac mac = Mac.getInstance(algorithm);
            final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
            mac.init(keySpec);
            byte[] result = mac.doFinal(input.getBytes("UTF-8"));
            return hexToString(result);
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (InvalidKeyException e) {

        }
        return null;
    }
}
