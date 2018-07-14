package com.ray.common.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public final class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";



    private static PublicKey getPublicKeyFromX509(String var0, String var1) throws NoSuchAlgorithmException, Exception {
        byte[] var2 = Base64.decode(var1);
        X509EncodedKeySpec var3 = new X509EncodedKeySpec(var2);
        KeyFactory var4 = KeyFactory.getInstance(var0);
        return var4.generatePublic(var3);
    }

  /**
   * 公钥加密
   * @param content
   * @param modulus
   * @param exponent
   * @return
   */
    public static String encrypt(String content, String modulus, String exponent) {
            RSAPublicKeySpec keySpec = getPublicKey(modulus, exponent);
            return encrypt(content, keySpec);
    }

    public static String encrypt(String content, RSAPublicKeySpec publicKeySpec) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
            return encrypt(content, keyFactory.generatePublic(publicKeySpec));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static RSAPublicKeySpec getPublicKey(String modulus, String exponent){
        byte[] m = Base64.decode(modulus);
        byte[] e = Base64.decode(exponent);
        BigInteger b1 = new BigInteger(1, m);
        BigInteger b2 = new BigInteger(1, e);
        return new RSAPublicKeySpec(b1, b2);
    }

  /**
   * 私钥解密
   * @param content
   * @param privateKeySpec
   * @return
   */
    public static String decrypt(byte[] content, RSAPrivateCrtKeySpec privateKeySpec){
        try {
            PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(content));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(byte[] content,
                                 String modulus,
                                 String publicExponent,
                                 String privateExponent,
                                 String primeP,
                                 String primeQ,
                                 String primeExponentP,
                                 String primeExponentQ,
                                 String crtCoefficient){
        RSAPrivateCrtKeySpec privateKeySpec = getPrivateKey(
            new BigInteger(1, Base64.decode(modulus)),
            new BigInteger(1, Base64.decode(publicExponent)),
            new BigInteger(1, Base64.decode(privateExponent)),
            new BigInteger(1, Base64.decode(primeP)),
            new BigInteger(1, Base64.decode(primeQ)),
            new BigInteger(1, Base64.decode(primeExponentP)),
            new BigInteger(1, Base64.decode(primeExponentQ)),
            new BigInteger(1, Base64.decode(crtCoefficient))
        );
        return decrypt(content, privateKeySpec);
    }

    private static RSAPrivateCrtKeySpec getPrivateKey(BigInteger modulus,
                                                      BigInteger publicExponent,
                                                      BigInteger privateExponent,
                                                      BigInteger primeP,
                                                      BigInteger primeQ,
                                                      BigInteger primeExponentP,
                                                      BigInteger primeExponentQ,
                                                      BigInteger crtCoefficient){
        return new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent,
            primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
    }

    /**
     * 针对C#的Rsa的加密
     *
     * @param encryptrtext 待加密字符串
     * @param publickey  公钥
     * @return
     */
    public static String encryptC(String encryptrtext, String publickey) {
        try {
            byte[] m = Base64.decode(publickey);
            byte[] e = Base64.decode("AQAB");
            BigInteger b1 = new BigInteger(1, m);
            BigInteger b2 = new BigInteger(1, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return encrypt(encryptrtext,(RSAPublicKey)keyFactory.generatePublic(keySpec));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encrypt(String var0, String var1) {
        String var2 = null;
        ByteArrayOutputStream var3 = null;
        try {
            PublicKey var4 = getPublicKeyFromX509("RSA", var1);
            Cipher var5 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            var5.init(1, var4);
            byte[] var6 = var0.getBytes("UTF-8");
            int var7 = var5.getBlockSize();
            var3 = new ByteArrayOutputStream();

            for (int var8 = 0; var8 < var6.length; var8 += var7) {
                var3.write(var5.doFinal(var6, var8, var6.length - var8 < var7 ? var6.length - var8 : var7));
            }

            var2 = new String(Base64.encode(var3.toByteArray()));
        } catch (Exception var17) {
        } finally {
            if (var3 != null) {
                try {
                    var3.close();
                } catch (IOException var16) {
                }
            }
        }
        return var2;
    }

    public static String encrypt(String var0,PublicKey publicKey) {
        String var2 = null;
        ByteArrayOutputStream var3 = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, publicKey);
            byte[] var6 = var0.getBytes("UTF-8");
            int blockSize = cipher.getBlockSize();
            if(blockSize > 0) {
                var3 = new ByteArrayOutputStream();
                for (int var8 = 0; var8 < var6.length; var8 += blockSize) {
                    var3.write(cipher.doFinal(var6, var8, var6.length - var8 < blockSize ? var6.length - var8 : blockSize));
                }
                var2 = Base64.encode(var3.toByteArray());
            }else{
                var2 = Base64.encode(cipher.doFinal(var6));
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        } finally {
            if (var3 != null) {
                try {
                    var3.close();
                } catch (IOException var16) {
                }
            }
        }
        return var2;
    }

    public static byte[] encryptToByteArray(String var0, String var1) {
        byte[] var2 = null;
        ByteArrayOutputStream var3 = null;

        try {
            PublicKey var4 = getPublicKeyFromX509("RSA", var1);
            Cipher var5 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            var5.init(1, var4);
            byte[] var6 = var0.getBytes("UTF-8");
            int var7 = var5.getBlockSize();
            var3 = new ByteArrayOutputStream();

            for (int var8 = 0; var8 < var6.length; var8 += var7) {
                var3.write(var5.doFinal(var6, var8, var6.length - var8 < var7 ? var6.length - var8 : var7));
            }

            var2 = var3.toByteArray();
        } catch (Exception var17) {
        } finally {
            if (var3 != null) {
                try {
                    var3.close();
                } catch (IOException var16) {
                }
            }
        }
        return var2;
    }

    public static String decrypt(String var0, String var1) {
        ByteArrayOutputStream var2 = null;
        String var3 = null;

        try {
            PKCS8EncodedKeySpec var4 = new PKCS8EncodedKeySpec(Base64.decode(var1));
            KeyFactory var5 = KeyFactory.getInstance("RSA");
            PrivateKey var6 = var5.generatePrivate(var4);
            Cipher var7 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            var7.init(2, var6);
            byte[] var8 = Base64.decode(var0);
            int var9 = var7.getBlockSize();
            var2 = new ByteArrayOutputStream();

            for (int var10 = 0; var10 < var8.length; var10 += var9) {
                var2.write(var7.doFinal(var8, var10, var8.length - var10 < var9 ? var8.length - var10 : var9));
            }

            var3 = new String(var2.toByteArray());
        } catch (Exception var19) {
        } finally {
            if (var2 != null) {
                try {
                    var2.close();
                } catch (IOException var18) {
                }
            }
        }

        return var3;
    }

    public static String sign(String var0, String var1) {
        String var2 = "utf-8";

        try {
            PKCS8EncodedKeySpec var3 = new PKCS8EncodedKeySpec(Base64.decode(var1));
            KeyFactory var4 = KeyFactory.getInstance("RSA");
            PrivateKey var5 = var4.generatePrivate(var3);
            Signature var6 = Signature.getInstance("SHA1WithRSA");
            var6.initSign(var5);
            var6.update(var0.getBytes(var2));
            byte[] var7 = var6.sign();
            return Base64.encode(var7);
        } catch (Exception var8) {
            return null;
        }
    }

    public static boolean doCheck(String var0, String var1, String var2) {
        try {
            KeyFactory var3 = KeyFactory.getInstance("RSA");
            byte[] var4 = Base64.decode(var2);
            PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(var4));
            Signature var6 = Signature.getInstance("SHA1WithRSA");
            var6.initVerify(var5);
            var6.update(var0.getBytes("utf-8"));
            boolean var7 = var6.verify(Base64.decode(var1));
            return var7;
        } catch (Exception var8) {
            return false;
        }
    }
}
