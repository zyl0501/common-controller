package com.ray.common.api;

import com.ray.common.Constants;

import java.nio.charset.Charset;

public final class ContentType {
    private final String mimeType;
    private final Charset charset;

    public ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public ContentType(String mimeType) {
        this.mimeType = mimeType;
        this.charset = Constants.UTF_8;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append(this.mimeType);
        if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

    // constants
    public static final ContentType APP_FORM_URLENCODED
            = new ContentType("application/x-www-form-urlencoded");
    public static final ContentType APP_JSON
            = new ContentType("application/json");
    public static final ContentType APP_OCTET_STREAM
            = new ContentType("application/octet-stream");
    public static final ContentType MULTIPART_FORM_DATA
            = new ContentType("multipart/form-data");
    public static final ContentType TEXT_HTML
            = new ContentType("text/html");
    public static final ContentType TEXT_PLAIN
            = new ContentType("text/plain");
    public static final ContentType WILDCARD
            = new ContentType("*/*");
}
