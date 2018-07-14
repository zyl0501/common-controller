package com.ray.common.dialogs;

import android.content.Context;

/**
 * Created by zyl on 2016/4/1.
 */
public class Dialogs {
    private static ProgressDialog dialog;

    public static ProgressDialog showProgressDialog(Context context, String msg) {
        return showProgressDialog(context, msg, true);
    }

    public static ProgressDialog showProgressDialog(Context context, int resId) {
        CharSequence msg = context.getText(resId);
        return showProgressDialog(context, msg, true);
    }

    public static ProgressDialog showProgressDialog(Context context, CharSequence msg, boolean cancelable) {
        dismiss();
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
