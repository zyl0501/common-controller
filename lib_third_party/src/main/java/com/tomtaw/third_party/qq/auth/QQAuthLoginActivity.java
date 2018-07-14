package com.tomtaw.third_party.qq.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ray.common.json.Jsons;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tomtaw.third_party.api.auth.AuthError;
import com.tomtaw.third_party.api.auth.IThirdAuth.ThirdAuthListener;
import com.tomtaw.third_party.api.auth.IUserInfo;
import com.tomtaw.third_party.qq.auth.data.QQUnionIdData;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tomtaw.third_party.utils.JsonParse.getStringByInputStream;


/**
 * Created by zyl on 2017/5/3.
 */

public class QQAuthLoginActivity extends Activity {
    public static final String ExtraAppId = "appid";
    private static Tencent mTencent;
    private static ThirdAuthListener authListener;

    String appId;

    IUiListener loginListener;

    TaskRequestUnionId unionIdTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appId = getIntent().getStringExtra(ExtraAppId);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(appId, getApplicationContext());
        }

        if (mTencent != null) {
            loginListener = new AuthListener(this, authListener);
            mTencent.login(this, "all", loginListener);
        } else {
            finish();
            notifyError("调用QQ登录失败");
            return;
        }
    }

    private void notifyError(String result) {
        if (authListener != null) {
            AuthError error = new AuthError();
            error.errorMessage = result;
            authListener.onError(error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
            requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        authListener = null;
        if (unionIdTask != null) {
            unionIdTask.cancel(true);
            unionIdTask = null;
        }
        super.onDestroy();
    }

    private class AuthListener implements IUiListener {
        ThirdAuthListener listener;
        Activity activity;

        AuthListener(Activity activity, ThirdAuthListener listener) {
            this.activity = activity;
            this.listener = listener;
        }

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Utils.showResultDialog(activity, "返回为空", "登录失败");
                AuthError authError = new AuthError();
                authError.errorMessage = "登录失败：" + "返回为空";
                listener.onError(authError);
                finish();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                Utils.showResultDialog(activity, "返回为空", "登录失败");
                AuthError authError = new AuthError();
                authError.errorMessage = "登录失败：" + "返回为空";
                listener.onError(authError);
                finish();
                return;
            }
            try {
                JSONObject jsonResp = (JSONObject) response;
                String openId = jsonResp.getString(Constants.PARAM_OPEN_ID);
                String token = jsonResp.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonResp.getString(Constants.PARAM_EXPIRES_IN);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                }
                getUserInfo(openId, token, listener);
            } catch (Exception e) {
                AuthError authError = new AuthError();
                authError.errorMessage = "内部错误：" + e.getMessage();
                listener.onError(authError);
                finish();
            }
        }

        @Override
        public void onError(UiError uiError) {
            AuthError authError = new AuthError();
            authError.errorCode = uiError.errorCode;
            authError.errorMessage = uiError.errorMessage;
            authError.errorDetail = uiError.errorDetail;
            listener.onError(authError);
            finish();
        }

        @Override
        public void onCancel() {
            listener.onCancel();
            finish();
        }
    }

    private void getUserInfo(String openId, String accessToken, ThirdAuthListener listener) {
        new UserInfo(this, mTencent.getQQToken())
            .getUserInfo(new UserInfoListener(this, openId, accessToken, listener));
    }

    private class UserInfoListener implements IUiListener {
        ThirdAuthListener listener;
        String openId;
        String accessToken;

        Activity activity;

        UserInfoListener(Activity activity, String openId, String accessToken, ThirdAuthListener listener) {
            this.activity = activity;
            this.openId = openId;
            this.accessToken = accessToken;
            this.listener = listener;
        }

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Utils.showResultDialog(activity, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                Utils.showResultDialog(activity, "返回为空", "登录失败");
                return;
            }
            try {
                JSONObject jsonResp = (JSONObject) response;
                String avatar = jsonResp.optString("figureurl_qq_2");//优先拿100*100的头像
                if (TextUtils.isEmpty(avatar)) {
                    //没有则那40*40的
                    avatar = jsonResp.optString("figureurl_qq_1");
                }
                QQAuthResp authResp = new QQAuthResp();
                authResp.openid = openId;
                authResp.unionId = openId;
                authResp.avatar = avatar;
                authResp.nickname = jsonResp.optString("nickname");
                //获取不到会默认返回“男”
                authResp.sex = "男".equals(jsonResp.optString("gender")) ? IUserInfo.MALE : IUserInfo.FEMALE;
                unionIdTask = new TaskRequestUnionId(accessToken, authResp);
                unionIdTask.execute();
            } catch (Exception e) {
                AuthError authError = new AuthError();
                authError.errorMessage = "内部错误：" + e.getMessage();
                listener.onError(authError);
            }
            finish();
        }

        @Override
        public void onError(UiError uiError) {
            AuthError authError = new AuthError();
            authError.errorCode = uiError.errorCode;
            authError.errorMessage = uiError.errorMessage;
            authError.errorDetail = uiError.errorDetail;
            listener.onError(authError);
            finish();
        }

        @Override
        public void onCancel() {
            listener.onCancel();
            finish();
        }
    }

    public static void setAuthListener(ThirdAuthListener authListener) {
        QQAuthLoginActivity.authListener = authListener;
    }

    private class TaskRequestUnionId extends AsyncTask<Void, Void, String> {
        private String access_token;
        private QQAuthResp qqAuthResp;

        public TaskRequestUnionId(String access_token, QQAuthResp qqAuthResp) {
            this.access_token = access_token;
            this.qqAuthResp = qqAuthResp;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = "https://graph.qq.com/oauth2.0/me?" +
                "access_token=" + access_token +
                "&unionid=" + 1;
            try {
                InputStream is = request(urlStr);
                String unionIdCallBack = getStringByInputStream(is);
                return getUnionId(unionIdCallBack);
            } catch (Exception e) {
                Log.d("qq", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unionId) {
            if (isCancelled()) return;
            if (TextUtils.isEmpty(unionId)) {
                if (authListener != null) {
                    AuthError authError = new AuthError(-1, "获取qq unionId失败", "");
                    authListener.onError(authError);
                }
            } else {
                if (authListener != null) {
                    qqAuthResp.unionId = unionId;
                    authListener.onComplete(qqAuthResp);
                }
            }
            finish();
        }
    }

    private String getUnionId(String unionIdCallBack) {
        if (TextUtils.isEmpty(unionIdCallBack)) {
            return null;
        }
        String pattern = "callback\\((.*)\\);";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(unionIdCallBack);
        if (m.find()) {
            String json = m.group(1);
            QQUnionIdData unionIdData = Jsons.fromJson(json, QQUnionIdData.class);
            return unionIdData != null ? unionIdData.getUnionid() : null;
        } else {
            return null;
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
