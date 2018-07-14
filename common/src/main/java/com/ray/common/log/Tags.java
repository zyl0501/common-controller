package com.ray.common.log;

import com.orhanobut.logger.Logger;
import com.ray.common.exception.Exceptions;

/**
 * Created by xiaoxu.yxx on 2015/7/21.
 */
public enum Tags {
    App("应用"),
    Config("配置中心"),
    TASK("任务调度"),
    API("网络请求"),
    PUSH("消息推送"),
    Event("事件EventBus"),
    DB("数据库"),
    Manager("业务管理层");

    public final String tag;
    public final String desc;

    Tags(String desc) {
        this.tag = "Doctor." + this.name();
        this.desc = desc;
    }


    public long getMask() {
        return 2 << this.ordinal();
    }

    public boolean isEnabled() {
        return true;
    }

    public void v(String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).v(msg, args);
    }

    public void d(String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).d(msg, args);
    }

    public void i(String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).i(msg, args);
    }

    public void w(String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).w(msg, args);
    }

    public void e(String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).e(msg, args);
    }

    public void e(Throwable t, String msg, Object... args) {
        if (this.isEnabled()) Logger.t(tag).e(msg + '\n' + Exceptions.getFullStackTrace(t), args);
    }
}
