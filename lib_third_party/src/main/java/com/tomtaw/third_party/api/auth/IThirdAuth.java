package com.tomtaw.third_party.api.auth;

/**
 * Created by zyl on 2017/4/27.
 */

public interface IThirdAuth {
    void login(ThirdAuthListener listener);

    interface ThirdAuthListener {
        void onComplete(AuthResult resp);

        void onError(AuthError uiError);

        void onCancel();
    }
}
