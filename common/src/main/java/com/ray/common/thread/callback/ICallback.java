package com.ray.common.thread.callback;

import com.ray.common.api.IProgress;

/**
 * Created by xiaoxu.yxx on 2015/7/30.
 */
public interface ICallback<T> extends IProgress<T> {

    void onResponse(IResult<T> t);

    void onException(Throwable t);

}
