package com.ray.common.encrypt;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * 创建时间：2017/2/17
 *
 * @author zyl
 */
public class DesTest {

  @Test
  public void decrypt() throws Exception {
    String data = "DAAA9C8007DB80C2633232FC55E977EC";
    String key = "t0MT@w~1";
    String result = new String(Des.decrypt(Hex.convertHexString(data), key));
    System.out.println("解密后：" + result);
  }

  @Test
  public void encrypt() throws Exception {
    String data = "123zxc自己";
    byte[] b1 = Des.encrypt(data.getBytes(), "t0MT@w~1");
//    b1 = "D7BC06E342EE630940B350D58BF9C927".getBytes();
    String encrypt = new String(b1, Charset.forName("UTF-8"));
    String aa = Hex.toHexString(b1);
    System.out.println("加密后：" + aa.toUpperCase());
    String data2 = new String(Des.decrypt(Hex.convertHexString(aa), "t0MT@w~1"));
    System.out.println("解密后：" + data2);
  }

  @Test
  public void encrypt3() throws Exception {
    String data = "123zxc自己";
    String b1 = Des.encrypt(data, "t0MT@w~1");
//    String aa = Hex.toHexString(b1.getBytes("UTF-8"));
    System.out.println("加密后：" + b1.toUpperCase());
    String data2 = Des.decrypt(b1, "t0MT@w~1");
    System.out.println("解密后：" + data2);
  }
}
