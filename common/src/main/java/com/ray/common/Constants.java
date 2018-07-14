package com.ray.common;

import java.nio.charset.Charset;

public interface Constants {
    byte[] EMPTY_BYTES = new byte[0];
    Charset UTF_8 = Charset.forName("UTF-8");
    String UTF_8_NAME = "UTF-8";
    String USER_AGENT = "User-Agent";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_TYPE_TEXT = "text/plain";
    String CONTENT_TYPE_STREAM = "application/octet-stream";
}
