package com.ray.common.encrypt;

/**
 * 创建时间：2017/2/17
 *
 * @author zyl
 */
public class Hex {

  /**
   * 16进制字符串转换为字符串
   *
   * @return
   */
  public static byte[] convertHexString(String ss) {
    byte digest[] = new byte[ss.length() / 2];
    for (int i = 0; i < digest.length; i++) {
      String byteString = ss.substring(2 * i, 2 * i + 2);
      int byteValue = Integer.parseInt(byteString, 16);
      digest[i] = (byte) byteValue;
    }

    return digest;
  }

  /**
   * 字符串转换为16进制字符串
   *
   * @return
   */
  public static String toHexString(byte b[]) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      String plainText = Integer.toHexString(0xff & b[i]);
      if (plainText.length() < 2)
        plainText = "0" + plainText;
      hexString.append(plainText);
    }

    return hexString.toString();
  }
}
