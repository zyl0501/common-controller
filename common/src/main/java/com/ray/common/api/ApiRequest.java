package com.ray.common.api;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.Map;

public interface ApiRequest<T> {
    byte GET = 0, POST = 1, PUT = 2, DELETE = 3;

    byte getMethod();

    @NonNull
    String getApi();

    @NonNull
    Map<String, String> getHeaders();

    Map<String, String> getParams();

    ContentType getContentType();

    int getConnectTimeout();

    HttpBody getBody();

    ApiCallback<T> getCallback();

    ResultParser<T> getParser();

    Type getResultType();
}
