package com.ray.common.encrypt;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNotEquals;

public class RsaTest {
  @Test
  public void encryptC() throws Exception {
    String LoginPwdKey="yzD2IAHWgOX5LAtEFI/5XAYGfHV1kK5dYHwFcVCBIZlzyIDBni1BN/FAi+bsXpK2bF4B7hV2HTcGMx/p54mFxTds3aCT23fmtOBg3U8pv7HT7BCYOA0otFeZBBZ9Rt6/tev6W96lSYt/0x5+AN2Zz4r+ql40NRAwTGVCSIWHntM=";
    String s = Rsa.encryptC("abc123", LoginPwdKey);
    System.out.println(s);
    assertNotEquals(s, null);
  }

  @Test
  public void test() throws UnsupportedEncodingException {
    String content = "abc";
    String encrypt = Rsa.encrypt(content,
        "yzD2IAHWgOX5LAtEFI/5XAYGfHV1kK5dYHwFcVCBIZlzyIDBni1BN/FAi+bsXpK2bF4B7hV2HTcGMx/p54mFxTds3aCT23fmtOBg3U8pv7HT7BCYOA0otFeZBBZ9Rt6/tev6W96lSYt/0x5+AN2Zz4r+ql40NRAwTGVCSIWHntM=",
        "AQAB");
    System.out.println("加密后：" + encrypt);
    String content2 = Rsa.decrypt(Base64.decode(encrypt),
        "yzD2IAHWgOX5LAtEFI/5XAYGfHV1kK5dYHwFcVCBIZlzyIDBni1BN/FAi+bsXpK2bF4B7hV2HTcGMx/p54mFxTds3aCT23fmtOBg3U8pv7HT7BCYOA0otFeZBBZ9Rt6/tev6W96lSYt/0x5+AN2Zz4r+ql40NRAwTGVCSIWHntM=",
        "AQAB",
        "Gc6GeWWNBzgDiqWE/buU0dbuEfafz5gQbLBece2QnmItWMTe/SZa8dj7+AjZ8opduFZu66c20Npr4326foQdRMdEFjrFWt6kBPO99uRlIn/SJ48smfBKgLlmdGWEzTn/OemCfvxHOGojNjiL8Gz0Mr9GQKyjsquCdX/saBGyPXk=",
        "0NMFG6csLAzL3I46bzaIMYoyeWkaVoc+Bylt7TT2cw96ejqs6d7QTK/LdLmIHn59C5TEiiyLU8vjyi0p5Eve9Q==",
        "+RgqI5CaYe/sXUiLU020CD+8+UzKtQcw8/AUbKQ9jZkWEvUcfotMg/5eIpIivyYoTpA7LbZYg5e/802L2a5Zpw==",
        "NI12kp/+mNNblpTglc5mdfkufKl7rFz6ujit5m1WvFwf2ZTcoTqmtzXigOyUGgCBHaIB2DgflCYSnHXDPiXt5Q==",
        "AQuT6bBKpYyPLlkGlKPn1H0H+vfShe3wy6U/QLFzvKtex1eV9gX5nTgtBvIcCTdMxf+e7/Mq7epuGMRDiPdNUw==",
        "e9Ueo48lrmcTVUorPPezsBjfeYL8MYuAp1gatMe1pldXjH2zJvprW2qWd4tB9V0dsgIy2swTBWe21Ak6xXP+UA=="
        );
    System.out.println("解密后：" + content2);
    Assert.assertEquals(content, content2);
  }

  @Test
  public void fuck(){
    String encrypt = "D7BC06E342EE630940B350D58BF9C927";
    System.out.println("加密后：" + encrypt);
    String content2 = Rsa.decrypt(Base64.decode(encrypt),
        "yzD2IAHWgOX5LAtEFI/5XAYGfHV1kK5dYHwFcVCBIZlzyIDBni1BN/FAi+bsXpK2bF4B7hV2HTcGMx/p54mFxTds3aCT23fmtOBg3U8pv7HT7BCYOA0otFeZBBZ9Rt6/tev6W96lSYt/0x5+AN2Zz4r+ql40NRAwTGVCSIWHntM=",
        "AQAB",
        "Gc6GeWWNBzgDiqWE/buU0dbuEfafz5gQbLBece2QnmItWMTe/SZa8dj7+AjZ8opduFZu66c20Npr4326foQdRMdEFjrFWt6kBPO99uRlIn/SJ48smfBKgLlmdGWEzTn/OemCfvxHOGojNjiL8Gz0Mr9GQKyjsquCdX/saBGyPXk=",
        "0NMFG6csLAzL3I46bzaIMYoyeWkaVoc+Bylt7TT2cw96ejqs6d7QTK/LdLmIHn59C5TEiiyLU8vjyi0p5Eve9Q==",
        "+RgqI5CaYe/sXUiLU020CD+8+UzKtQcw8/AUbKQ9jZkWEvUcfotMg/5eIpIivyYoTpA7LbZYg5e/802L2a5Zpw==",
        "NI12kp/+mNNblpTglc5mdfkufKl7rFz6ujit5m1WvFwf2ZTcoTqmtzXigOyUGgCBHaIB2DgflCYSnHXDPiXt5Q==",
        "AQuT6bBKpYyPLlkGlKPn1H0H+vfShe3wy6U/QLFzvKtex1eV9gX5nTgtBvIcCTdMxf+e7/Mq7epuGMRDiPdNUw==",
        "e9Ueo48lrmcTVUorPPezsBjfeYL8MYuAp1gatMe1pldXjH2zJvprW2qWd4tB9V0dsgIy2swTBWe21Ak6xXP+UA=="
    );
    System.out.println("解密后：" + content2);
  }
}