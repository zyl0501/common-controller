package com.ray.common.api;

public interface ApiProxy {
    <T> ApiResponse<T> request(ApiRequest<T> request);
}
