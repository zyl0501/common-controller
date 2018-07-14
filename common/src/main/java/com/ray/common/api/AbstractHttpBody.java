package com.ray.common.api;

import java.nio.charset.Charset;

public abstract class AbstractHttpBody implements HttpBody {
    protected final ContentType contentType;

    public AbstractHttpBody(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getMimeType() {
        return contentType.getMimeType();
    }


    public Charset getCharset() {
        return contentType.getCharset();
    }

    @Override
    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }
}
