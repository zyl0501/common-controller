package com.ray.common.thread.task;

import com.ray.common.api.IProgress;

public interface ITaskCallback<Result> extends IProgress<Result> {

    void onBeforeCall();

    void onAfterCall();

    void onComplete(Result data);

    void onException(Throwable t);

    void onCancelled();

}
