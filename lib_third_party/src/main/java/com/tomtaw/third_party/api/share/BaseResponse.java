package com.tomtaw.third_party.api.share;

/**
 * Created by Ray on 16/1/6.
 */
public class BaseResponse {
    public int errCode;
    public String errMsg;

    public interface ErrCode {
        int ERR_OK = 0;
        int ERR_USER_CANCEL = -2;
        int ERR_SENT_FAILED = -3;
        int ERR_AUTH_DENIED = -4;
    }
}
