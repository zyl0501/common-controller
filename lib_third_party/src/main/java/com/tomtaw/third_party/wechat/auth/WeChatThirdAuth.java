package com.tomtaw.third_party.wechat.auth;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tomtaw.third_party.api.auth.IThirdAuth;
import com.tomtaw.third_party.wechat.WXConfig;
import com.tomtaw.third_party.wechat.WXGlobal;

import java.util.Random;

/**
 * Created by zyl on 2017/4/27.
 */

public class WeChatThirdAuth implements IThirdAuth {

    private String appId;
    private String secret;
    private Context context;
    private Class<?> wxEntryActivityClz;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public WeChatThirdAuth(Context context) {
        this(context, WXConfig.appId, WXConfig.secret, getEntityActivityClz(context));
    }

    public WeChatThirdAuth(Context context, Class<?> wxEntryActivityClz) {
        this(context, WXConfig.appId, WXConfig.secret, getEntityActivityClz(context));
    }

    private WeChatThirdAuth(Context context, String appId, String secret, Class<?> wxEntryActivityClz) {
        this.appId = appId;
        this.secret = secret;
        this.context = context;
        this.wxEntryActivityClz = wxEntryActivityClz;

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, appId, false);
        // 将该app注册到微信
        api.registerApp(appId);
    }

    @Override
    public void login(ThirdAuthListener listener) {
        if (wxEntryActivityClz == null) {
            Log.w("Third_Party", "请正确配置WXEntryActivity");
            return;
        }
//        WXResponseActivity.setAuthListener(listener);
//        Intent intent = new Intent(context, wxEntryActivityClz);
//        intent.putExtra(WXResponseActivity.ExtraAppId, appId);
//        intent.putExtra(WXResponseActivity.ExtraSecret, secret);
//        if (!(context instanceof Activity)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(intent);

        WXGlobal.authListener = listener;
        sendAuthReq();
    }

    private static Class<?> getEntityActivityClz(Context context) {
        Context appContext = context.getApplicationContext();
        try {
            ApplicationInfo appInfo = appContext.getPackageManager()
                .getApplicationInfo(appContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            String appId = appInfo.metaData.getString("WX_APP_ID");
            return Class.forName(appId + ".wxapi." + "WXEntryActivity");
        } catch (Exception e) {
            Log.w("Third_Party", "WXEntryActivity not found. "+e.getMessage());
        }
        return null;
    }

    private void sendAuthReq() {
        WXGlobal.authState = String.valueOf(new Random().nextInt());
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.transaction = WXGlobal.buildTransAuth();
        req.state = WXGlobal.authState;
        api.sendReq(req);
    }
}
