package com.ray.common.encrypt;


import com.ray.common.util.IOs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class MD5 {

     static Charset UTF_8 = Charset.forName("UTF-8");

    static Charset UNICODE_LE = Charset.forName("UTF-16LE");  //C# unicode编码格式

    static Charset UNICODE = Charset.forName("UTF-16");

    public static String encrypt(File file) {
        InputStream in = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            byte[] buffer = new byte[10240];//10k
            int readLen;
            while ((readLen = in.read(buffer)) != -1) {
                digest.update(buffer, 0, readLen);
            }
            return toHex(digest.digest());
        } catch (Exception e) {
            return "";
        } finally {
            IOs.close(in);
        }
    }

    public static String encrypt(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes(UNICODE_LE));
            return toHex(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public static String encrypt(String text,Charset codestyle) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes(codestyle));
            return toHex(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public static String encrypt(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return toHex(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            buffer.append(Character.forDigit((bytes[i] & 240) >> 4, 16));
            buffer.append(Character.forDigit(bytes[i] & 15, 16));
        }

        return buffer.toString();
    }


    /**
     * HmacSHA1 加密
     *
     * @param data
     * @param encryptKey
     * @return
     */
    public static String hmacSha1(String data, String encryptKey) {
        final String HMAC_SHA1 = "HmacSHA1";
        SecretKeySpec signingKey = new SecretKeySpec(encryptKey.getBytes(UTF_8), HMAC_SHA1);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            mac.update(data.getBytes(UTF_8));
            return toHex(mac.doFinal());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * HmacSHA1 加密
     *
     * @param data
     * @return
     */
    public static String sha1(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return toHex(digest.digest(data.getBytes(UTF_8)));
        } catch (Exception e) {
            return "";
        }
    }
}
