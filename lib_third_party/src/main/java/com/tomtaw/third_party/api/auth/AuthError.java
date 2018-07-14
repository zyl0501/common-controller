package com.tomtaw.third_party.api.auth;

/**
 * Created by zyl on 2017/4/27.
 */

public class AuthError {
    public int errorCode;
    public String errorMessage;
    public String errorDetail;

    public AuthError() {
    }

    public AuthError(int errorCode, String errorMessage, String errorDetail) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }
}
