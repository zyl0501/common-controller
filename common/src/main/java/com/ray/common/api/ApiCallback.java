package com.ray.common.api;

public interface ApiCallback<T> extends IProgress<T> {

    void onResponse(ApiResult<T> t);

    void onException(Throwable t);

}
