package com.tomtaw.third_party;

import android.app.Activity;

import com.tomtaw.third_party.api.share.IShare;
import com.tomtaw.third_party.api.share.ShareCallback;
import com.tomtaw.third_party.api.share.ShareDO;

/**
 * Created by Ray on 15/9/11.
 */
public final class ShareActionHelper {

    private IShare shareAction;

    public ShareActionHelper() {
    }

    public void setShareAction(IShare share) {
        this.shareAction = share;
    }

    public void doShare(ShareDO shareDO, ShareCallback callback) {
        if (shareAction != null) {
            shareAction.share(shareDO, callback);
        }
    }
}
