package com.tomtaw.third_party.api.share;

/**
 * Created by Ray on 15/9/11.
 */
public interface IShare {
    void share(ShareDO shareDO, ShareCallback callback);

    /**
     * {@link ShareType}
     * @return
     */
    int getType();

    void onResponse(BaseResponse resp);
}
