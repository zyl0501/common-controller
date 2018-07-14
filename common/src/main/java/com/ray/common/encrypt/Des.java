

package com.ray.common.encrypt;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class Des {

  /**
   * 加密
   * @return 结果为16进制
   */
  public static String encrypt(String data, String password) {
    if (data == null || data.length() <= 0 || TextUtils.isEmpty(password)) return null;
    byte[] resultByte = new byte[0];
    try {
      resultByte = encrypt(data.getBytes("UTF-8"), password);
    } catch (UnsupportedEncodingException e) {
      Log.e("Des", e.getMessage(), e);
    }
    return resultByte == null ? null : Hex.toHexString(resultByte);
  }

  /**
   * 加密
   *
   * @param datasource byte[]
   * @param password   String
   * @return byte[]
   */
  public static byte[] encrypt(byte[] datasource, String password) {
    try {
      SecureRandom random = new SecureRandom();
      DESKeySpec desKey = new DESKeySpec(password.getBytes());
      //创建一个密匙工厂，然后用它把DESKeySpec转换成
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey securekey = keyFactory.generateSecret(desKey);
      //Cipher对象实际完成加密操作
      Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
      //用密匙初始化Cipher对象
      IvParameterSpec iv = new IvParameterSpec(password.getBytes("UTF-8"));
      cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
      //现在，获取数据并加密
      //正式执行加密操作
      return cipher.doFinal(datasource);
    } catch (Throwable e) {
      Log.e("Des", e.getMessage(), e);
      return null;
    }
  }

  /**
   * 解密
   * @param data 为加密后的16进制数据
   * @param password
   * @return
   */
  public static String decrypt(String data, String password){
    if (data == null || data.length() <= 0 || TextUtils.isEmpty(password)) return null;
    try {
      byte[] resultByte = decrypt(Hex.convertHexString(data), password);
      return resultByte == null ? null : new String(resultByte);
    }catch (Exception e){
      Log.e("Des", e.getMessage(), e);
      return null;
    }
  }

  /**
   * 解密
   *
   * @param src      byte[]
   * @param passworHex.convertHexString(data)d String
   * @return byte[]
   */
  public static byte[] decrypt(byte[] src, String password) {
    try {
      // DES算法要求有一个可信任的随机数源
      SecureRandom random = new SecureRandom();
      // 创建一个DESKeySpec对象
      DESKeySpec desKey = new DESKeySpec(password.getBytes("UTF-8"));
      // 创建一个密匙工厂
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      // 将DESKeySpec对象转换成SecretKey对象
      SecretKey securekey = keyFactory.generateSecret(desKey);
      // Cipher对象实际完成解密操作
      Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
      // 用密匙初始化Cipher对象
      IvParameterSpec iv = new IvParameterSpec(password.getBytes("UTF-8"));
      cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
      // 真正开始解密操作
      return cipher.doFinal(src);
    }catch (Exception e){
      Log.e("Des", e.getMessage(), e);
      return null;
    }
  }

}
