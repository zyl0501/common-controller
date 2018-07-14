/**
 *
 */
package com.ray.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.ray.common.thread.Handlers;
import com.ray.common.thread.Threads;

public final class Toasts {
    /**
     * Toast the short text
     *
     * @param context
     * @param text
     */
    public static void show(final String text, final Context context) {
        makeShow(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * Toast the short text
     *
     * @param context
     * @param text
     */
    public static void show(final Context context,final CharSequence text) {
        makeShow(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(final Context context, final int resId, final Object... args) {
        makeShow(context, resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * Toast the long text
     *
     * @param context
     * @param resId
     * @param args
     */
    public static void showLong(final Context context, final int resId, final Object... args) {
        makeShow(context, resId, Toast.LENGTH_LONG, args);
    }

    /**
     * Toast the long text
     *
     * @param context
     * @param text
     */
    public static void showLong(final Context context, final String text) {
        makeShow(context, text, Toast.LENGTH_LONG);
    }

    /**
     * Toast the short text
     *
     * @param context
     * @param resId
     * @param args
     */
    public static void showShort(final Context context, final int resId, final Object... args) {
        makeShow(context, resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * Toast the short text
     *
     * @param context
     * @param text
     */
    public static void showShort(final Context context, final String text) {
        makeShow(context, text, Toast.LENGTH_SHORT);
    }

    private static void makeShow(final Context context, final int resId, int type, final Object... args) {
        if (context == null) return;
        try {
            final Resources res = context.getResources();
            if (res == null) return;

            String tip;
            if (args != null && args.length > 0) {
                tip = res.getString(resId, args);
            } else {
                tip = res.getString(resId);
            }
            makeShow(context, tip, type);
        } catch (Exception e) {
            Log.e("Toasts", "", e);
        }
    }

    private static void makeShow(final Context context, final CharSequence text, final int type) {
        if (context == null) return;
        if (Threads.inMainThread()) {
            Toast.makeText(context, text, type).show();
        } else {
            Handlers.postMain(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, text, type).show();
                }
            });
        }
    }

    private static Toast failToast;

    public static void showWhenNotExists(final Context context, final String text, final int type) {
        if (failToast == null) {
            failToast = Toast.makeText(context, text, type);
        }
        failToast.show();
    }
}
