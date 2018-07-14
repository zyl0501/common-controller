package com.ray.common.log;

import com.orhanobut.logger.Logger;

/**
 * Created by xiaoxu.yxx on 2015/7/21.
 */
public final class Logs {

    public static void v(String tag, String msg, Object... args) {
        Logger.t(tag).v(msg, args);
    }


    public static void d(String tag, String msg, Object... args) {
        Logger.t(tag).d(msg, args);
    }


    public static void i(String tag, String msg, Object... args) {
        Logger.t(tag).i(msg, args);
    }


    public static void w(String tag, String msg, Object... args) {
        Logger.t(tag).w(msg, args);
    }


    public static void w(String tag, Throwable tr) {
        Logger.w(tag, tr);
    }

    public static void e(String tag, String msg, Object... args) {
        Logger.t(tag).e(msg, args);
    }

    public static void e(String tag, Throwable tr, String msg, Object... args) {
        Logger.t(tag).e(tr, msg, args);
    }
}
