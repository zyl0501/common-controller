package com.tomtaw.third_party.qq.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

/**
 * Created by zyl on 2017/4/27.
 */

public class Utils {
    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if(msg == null) return;
        String rmsg = msg.replace(",", "\n");
        Log.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
            .setNegativeButton("知道了", null).create().show();
    }
}
