package com.tomtaw.third_party.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ray.common.log.Tags;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tomtaw.third_party.api.auth.AuthError;
import com.tomtaw.third_party.api.auth.IThirdAuth.ThirdAuthListener;
import com.tomtaw.third_party.api.auth.IUserInfo;
import com.tomtaw.third_party.R;
import com.tomtaw.third_party.api.share.ShareCallback;
import com.tomtaw.third_party.wechat.auth.WeChatAuthResp;
import com.tomtaw.third_party.wechat.auth.data.WeChatAccessToken;
import com.tomtaw.third_party.wechat.auth.data.WeChatUserInfo;
import com.tomtaw.third_party.utils.JsonParse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by zyl on 2017/4/27.
 */

public class WXResponseActivity extends Activity implements IWXAPIEventHandler {
    private ThirdAuthListener authListener;
    private ShareCallback shareCallback;

    private IWXAPI iwxapi;

    private String appId;
    private String secret;

    AsyncTask accessTokenTask;
    AsyncTask userInfoTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appId = WXConfig.appId;
        secret = WXConfig.secret;

        iwxapi = WXAPIFactory.createWXAPI(this, appId, true);
        boolean registerFlag = iwxapi.registerApp(appId);
        if (!registerFlag) {
            notifyError(R.string.err_register);
            finish();
            return;
        }

        authListener = WXGlobal.authListener;
        shareCallback = WXGlobal.shareCallback;
        try {
            boolean flag = iwxapi.handleIntent(getIntent(), this);
//            if (!flag) {
//                notifyError("调用微信登录失败：handleIntent false");
//                finish();
//                return;
//            }
        } catch (Exception e) {
            Log.w("weixin", e.getMessage());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        try {
            boolean flag = iwxapi.handleIntent(getIntent(), this);
            if (!flag) finish();
        } catch (Exception e) {
            Log.w("weixin", e.getMessage());
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("weixin", "onReq" + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                if(WXGlobal.isAuth(resp.transaction)) {
                    if (resp instanceof SendAuth.Resp) {
                        String state = ((SendAuth.Resp) resp).state;
                        if (TextUtils.equals(state, WXGlobal.authState)) {
                            requestAccessToken(((SendAuth.Resp) resp).code);
                        } else {
                            Log.d("weixin", "onResp state " + state + " 与传入的不一致");
                            finish();
                        }
                    }
                } else if (WXGlobal.isShare(resp.transaction)) {
                    if (shareCallback != null) {
                        shareCallback.onComplete("wx", true);
                    }
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
//                notifyError(result);
                if (authListener != null) {
                    authListener.onCancel();
                }
                if (shareCallback != null) {
                    shareCallback.onComplete("wx", false);
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                notifyError(result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                notifyError(result);
                finish();
                break;
            default:
                result = R.string.errcode_unknown;
                notifyError(result);
                finish();
                break;
        }
    }

    private void notifyError(int result) {
        if (authListener != null) {
            AuthError error = new AuthError();
            error.errorMessage = getString(result);
            authListener.onError(error);
        }

        if (shareCallback != null) {
            shareCallback.onComplete("wx", false);
        }
    }

    /**
     * code 换 access_token
     */
    private void requestAccessToken(String code) {
        accessTokenTask = new TaskRequestAccessToken(appId, secret, code)
                .execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        authListener = null;
        shareCallback = null;
        WXGlobal.clear();
        if (accessTokenTask != null) {
            accessTokenTask.cancel(true);
        }
        if (userInfoTask != null) {
            userInfoTask.cancel(true);
        }
        super.onDestroy();
    }

    private class TaskRequestAccessToken extends AsyncTask<String, Void, WeChatAccessToken> {

        private String appId;
        private String secret;
        private String code;

        public TaskRequestAccessToken(String appId, String secret, String code) {
            this.appId = appId;
            this.secret = secret;
            this.code = code;
        }

        @Override
        protected WeChatAccessToken doInBackground(String... params) {
            String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=" + appId + "" +
                    "&secret=" + secret + "" +
                    "&code=" + code + "" +
                    "&grant_type=authorization_code";
            try {
                InputStream is = request(urlStr);
                return JsonParse.parseResult(is, WeChatAccessToken.class);
            } catch (Exception e) {
                Log.d("weixin", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeChatAccessToken accessTokenDO) {
            if (isCancelled()) return;
            if (accessTokenDO == null) {
                if (authListener != null) {
                    AuthError authError = new AuthError(-1, "请求微信授权失败", "");
                    authListener.onError(authError);
                }
                finish();
            } else {
                userInfoTask = new TaskRequestUserInfo(accessTokenDO.getAccess_token(), appId)
                        .execute();
            }
        }
    }

    private class TaskRequestUserInfo extends AsyncTask<Void, Void, WeChatUserInfo> {
        private String access_token;
        private String openid;

        public TaskRequestUserInfo(String access_token, String openid) {
            this.access_token = access_token;
            this.openid = openid;
        }

        @Override
        protected WeChatUserInfo doInBackground(Void... params) {
            String urlStr = "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=" + access_token +
                    "&openid=" + openid;
            try {
                InputStream is = request(urlStr);
                return JsonParse.parseResult(is, WeChatUserInfo.class);
            } catch (Exception e) {
                Log.d("weixin", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeChatUserInfo userInfo) {
            if (isCancelled()) return;
            if (userInfo == null) {
                if (authListener != null) {
                    AuthError authError = new AuthError(-1, "获取微信用户信息失败", "");
                    authListener.onError(authError);
                }
            } else {
                if (authListener != null) {
                    WeChatAuthResp resp = new WeChatAuthResp();
                    resp.openid = openid;
                    resp.unionId = userInfo.getUnionid();
                    resp.nickname = userInfo.getNickname();
                    resp.sex = userInfo.getSex() == 1 ? IUserInfo.MALE : IUserInfo.FEMALE;
                    resp.avatar = userInfo.getHeadimgurl();
                    authListener.onComplete(resp);
                }
            }
            finish();
        }
    }

    private InputStream request(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
        conn.setRequestMethod("GET");
        //HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
        conn.setDoInput(true);
        //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
        //禁用网络缓存
        conn.setUseCaches(false);
        //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
        //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
        conn.connect();
        //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
        return conn.getInputStream();
    }

}
