package com.ray.common.encrypt;

import org.junit.Test;

/**
 * Created by zyl on 2017/3/27.
 */
public class MD5Test {
  @Test
  public void encrypt() throws Exception {
    System.out.println(MD5.encrypt("abc123"));
  }

}