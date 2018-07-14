package com.tomtaw.third_party.wechat.share;

import android.content.Context;

import com.tomtaw.third_party.api.share.ShareType;


/**
 * Created by Ray on 15/9/11.
 */
public final class ShareWeChatTimeLine extends AbsShareWeChat {

    public ShareWeChatTimeLine(Context context) {
        super(context);
    }

    @Override
    protected boolean isShareToUser() {
        return false;
    }

    @Override
    public int getType() {
        return ShareType.TYPE_WECHAT_MOMENTS;
    }
}
