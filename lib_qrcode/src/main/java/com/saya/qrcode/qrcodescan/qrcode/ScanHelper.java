package com.saya.qrcode.qrcodescan.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zyl on 2017/5/22.
 */
public class ScanHelper {
    private static ScanHelper defaultInstance;
    private ScanCallBack callBack;

    public static ScanHelper I() {
        if (defaultInstance == null) {
            synchronized (ScanHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ScanHelper();
                }
            }
        }
        return defaultInstance;
    }

    public void openScan(Context context){
        Intent intent = new Intent(context, CaptureActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public ScanHelper setCallBack(ScanCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    ScanCallBack getCallBack() {
        return callBack;
//        return callBack != null ? callBack.get() : null;
    }
}
