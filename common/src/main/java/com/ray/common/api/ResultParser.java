package com.ray.common.api;

public interface ResultParser<T> {
    ApiResult<T> parse(String result);
}
