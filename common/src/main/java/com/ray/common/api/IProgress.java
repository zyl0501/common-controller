package com.ray.common.api;

public interface IProgress<Result> {

    void onBeforeCall();

    void onAfterCall();

    void onCancelled();

    void onException(Throwable t);

    void onComplete(Result t);

    void onProgress(long total, long current, Object... progress);
}
