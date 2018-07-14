package com.tomtaw.third_party.wechat.share;

import android.content.Context;

import com.tomtaw.third_party.api.share.ShareType;


/**
 * Created by Ray on 15/9/11.
 */
public final class ShareWeChatUser extends AbsShareWeChat {
    public ShareWeChatUser(Context context) {
        super(context);
    }

    @Override
    protected boolean isShareToUser() {
        return true;
    }

    @Override
    public int getType() {
        return ShareType.TYPE_WECHAT_USER;
    }
}
