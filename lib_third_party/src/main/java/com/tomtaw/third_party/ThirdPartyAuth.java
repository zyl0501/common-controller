package com.tomtaw.third_party;

import com.tomtaw.third_party.api.auth.IThirdAuth;

/**
 * Created by zyl on 2017/4/27.
 */

public class ThirdPartyAuth {
    private IThirdAuth thirdLogin;

    public void registerLogin(IThirdAuth login) {
        this.thirdLogin = login;
    }

    public void login(IThirdAuth.ThirdAuthListener listener) {
        if(thirdLogin != null) {
            thirdLogin.login(listener);
        }
    }
}
