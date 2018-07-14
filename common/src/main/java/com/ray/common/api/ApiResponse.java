package com.ray.common.api;

public interface ApiResponse<T> {
    ApiResult<T> getResult();

    ApiResult<T> parseResult(ResultParser<T> parse);

    int getStatusCode();

    void cancel();

}
