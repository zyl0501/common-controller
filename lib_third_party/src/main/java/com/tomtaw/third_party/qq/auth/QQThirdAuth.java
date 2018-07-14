package com.tomtaw.third_party.qq.auth;

import android.app.Activity;
import android.content.Intent;

import com.tencent.tauth.Tencent;
import com.tomtaw.third_party.api.auth.IThirdAuth;

/**
 * Created by zyl on 2017/4/27.
 */
public class QQThirdAuth implements IThirdAuth {
    public static Tencent mTencent;

    private Activity activity;
    private String appId;

    public QQThirdAuth(Activity activity, String appId) {
        this.appId = appId;
        this.activity = activity;
    }

    @Override
    public void login(ThirdAuthListener listener) {
        QQAuthLoginActivity.setAuthListener(listener);
        Intent intent = new Intent(activity, QQAuthLoginActivity.class);
        intent.putExtra(QQAuthLoginActivity.ExtraAppId, appId);
        activity.startActivity(intent);
    }

}
