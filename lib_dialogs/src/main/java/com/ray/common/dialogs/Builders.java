package com.ray.common.dialogs;

import android.content.Context;

/**
 * Created by zyl on 2016/4/1.
 */
public class Builders {
    static int defaultTheme = R.style.dia_NoTitleDialog;

    public static void setDefaultTheme(int theme){
        defaultTheme = theme;
    }

    public static AlertDialog.Builder A(Context context){
        return new AlertDialog.Builder(context);
    }
    public static ProgressDialog.Builder P(Context context){

        return new ProgressDialog.Builder(context);
    }

    public static ListDialog.Builder L(Context context){
        return new ListDialog.Builder(context);
    }
}
