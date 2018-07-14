package com.ray.common.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpBody {
    ContentType getContentType();

    long getContentLength();

    InputStream getInputStream() throws IOException;

    byte[] toByteArray();

    void writeTo(OutputStream out) throws IOException;

    boolean isStreaming();
}
