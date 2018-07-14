package com.ray.common.api;

public interface  ApiResult<T> {
    int code();

    T data();

    String msg();

    boolean success();
}
