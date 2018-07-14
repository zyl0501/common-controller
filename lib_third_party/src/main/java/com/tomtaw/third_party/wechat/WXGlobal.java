package com.tomtaw.third_party.wechat;

import android.text.TextUtils;

import com.tomtaw.third_party.api.auth.IThirdAuth;
import com.tomtaw.third_party.api.share.ShareCallback;

/**
 * Created by zyl on 2017/9/15.
 */

public class WXGlobal {
    public static String authState;
    public static IThirdAuth.ThirdAuthListener authListener;
    public static ShareCallback shareCallback;

    public static boolean isAuth(String transaction) {
        return !TextUtils.isEmpty(transaction) && transaction.startsWith("wx_auth");
    }

    public static boolean isShare(String transaction) {
        return !TextUtils.isEmpty(transaction) && transaction.startsWith("wx_share");
    }

    public static boolean isShareText(String transaction) {
        return !TextUtils.isEmpty(transaction) && transaction.startsWith("wx_share_text");
    }

    public static boolean isShareUrl(String transaction) {
        return !TextUtils.isEmpty(transaction) && transaction.startsWith("wx_share_url");
    }

    public static String buildTransAuth() {
        return buildTransaction("wx_auth");
    }

    public static String buildTransShareText() {
        return buildTransaction("wx_share_text");
    }

    public static String buildTransShareUrl() {
        return buildTransaction("wx_share_url");
    }

    private static String buildTransaction(String tag) {
        return (tag == null) ? String.valueOf(System.currentTimeMillis()) : tag + System.currentTimeMillis();
    }

    public static void clear(){
        authState = null;
        authListener = null;
        shareCallback = null;
    }
}
