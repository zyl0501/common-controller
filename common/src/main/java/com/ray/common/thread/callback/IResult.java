package com.ray.common.thread.callback;

/**
 * Created by xiaoxu.yxx on 2015/7/21.
 */
public interface IResult<T> {
    int code();

    T data();

    String msg();

    boolean success();
}
