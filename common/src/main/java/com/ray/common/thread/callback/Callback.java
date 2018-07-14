package com.ray.common.thread.callback;

import com.ray.common.BuildConfig;
import com.ray.common.thread.Handlers;
import com.ray.common.thread.task.ITaskCallback;

import java.util.concurrent.Callable;

/**
 * 应用通用callback,可以在task和Api通用
 * <p>
 * 如果是在Task里使用:
 * ----1.任务执行时调用doBackground
 * ----2.任务执行成功调用onComplete
 * ----3.任务执行异常调用onException
 * ----4.任务被取消会调用onCancelled
 * <p>
 * 如果是在Api里调用
 * ----1.请求正常返会回调onResponse
 * ----2.请求异常返会回调onException
 * ----3.返回结果success==true会回调onComplete
 * ----4.返回结果success=false会回调onFailure
 * <p>
 * 如果要更新进度条请在doBackground手动调用progress方法更新进度
 * <p>
 */
public abstract class Callback<Result> implements Callable<Result>, ITaskCallback<Result>, ICallback<Result> {

    /**
     * callback被执行前调用
     */
    @Override
    public void onBeforeCall() {

    }

    /**
     * callback被执行后调用，
     * 无论执行成功或失败此方法都将被调用
     */
    @Override
    public void onAfterCall() {

    }

    /**
     * Callable.call的别名，表明该方法在后台线程中执行
     *
     * @throws Exception
     */
    public Result doBackground() throws Exception {
        return null;
    }

    /**
     * 任务执行完毕或Api调用成功的回调方法，在主线程里执行
     *
     * @param t
     */
    @Override
    public void onComplete(Result t) {

    }


    /**
     * 任务执行进度的回调方法，在主线程里执行
     *
     */
    @Override
    public void onProgress(long total, long current, Object... progress) {

    }

    /**
     * 任务执行失败的回调方法，在主线程里执行
     *
     */
    @Override
    public void onException(Throwable t) {
        if (BuildConfig.DEBUG) {
           // Toasts.showLong(App.ctx(), Exceptions.getFullStackTrace(t));
        }
    }

    /**
     * 任务执行被取消的回调方法，在主线程里执行
     *
     */
    @Override
    public void onCancelled() {
       // if (BuildConfig.DEBUG) //Toasts.showLong(App.ctx(), "task cancelled");
    }

    /**
     * Api调用失败的回调方法，在主线程里执行
     *
     */
    public void onFailure(int errorCode, String errorMsg) {
       // if (!Strings.isBlank(errorMsg)) Toasts.showLong(App.ctx(), errorMsg);
    }

    /**
     * Api调用结束的回调方法，在主线程里执行
     *
     */
    @Override
    public void onResponse(IResult<Result> result) {
        if (result.success()) this.onComplete(result.data());
        else onFailure(result.code(), result.msg());
    }

    /**
     * 如果要更新进度条请在doBackground调用此方法
     *
     * @throws Exception
     */
    protected final void progress(final long total, final long current, final Object... progress) throws Exception {
        Handlers.postMain(new Runnable() {
            @Override
            public void run() {
                onProgress(total, current, progress);
            }
        });
    }


    /**
     * 此方法不允许调用，可能产生死循环
     *
     * @throws Exception
     */
    @Override
    @Deprecated
    public final Result call() throws Exception {
        return doBackground();
    }
}
