package com.tomtaw.third_party.wechat.share;

import android.content.Context;
import android.media.ThumbnailUtils;
import android.text.TextUtils;

import com.ray.common.lang.Strings;
import com.ray.common.log.Tags;
import com.ray.common.util.Toasts;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tomtaw.third_party.api.share.BaseResponse;
import com.tomtaw.third_party.api.share.IShare;
import com.tomtaw.third_party.api.share.ShareCallback;
import com.tomtaw.third_party.api.share.ShareDO;
import com.tomtaw.third_party.utils.PackageUtils;
import com.tomtaw.third_party.utils.ThumbUtils;
import com.tomtaw.third_party.wechat.WXConfig;
import com.tomtaw.third_party.wechat.WXGlobal;

/**
 * Created by Ray on 15/9/11.
 */
public abstract class AbsShareWeChat implements IShare {
    private static final String WECHAT_PKG = "com.tencent.mm";

    protected Context mCtx;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public AbsShareWeChat(Context context) {
        this.mCtx = context;
        if (TextUtils.isEmpty(WXConfig.appId)) {
            Tags.App.w("wechat app id is empty.");
        }
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, WXConfig.appId, false);
        // 将该app注册到微信
        api.registerApp(WXConfig.appId);
    }

    @Override
    public void share(final ShareDO shareDO, ShareCallback callback) {
        if (!PackageUtils.checkApkExist(mCtx, WECHAT_PKG)) {
            Toasts.show("未安装微信", mCtx);
            return;
        }
        WXGlobal.shareCallback = callback;

        WXWebpageObject webpage = null;
        if (!Strings.isBlank(shareDO.url)) {
            webpage = new WXWebpageObject();
            webpage.webpageUrl = shareDO.url;
        }
        String transaction;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareDO.title;
        if (webpage == null) {
            // 初始化一个WXTextObject对象
            WXTextObject textObj = new WXTextObject();
            textObj.text = shareDO.content;
            msg.mediaObject = textObj;
            transaction = WXGlobal.buildTransShareText();
        } else {
            msg.description = shareDO.content;
            transaction = WXGlobal.buildTransShareUrl();
        }
        if (shareDO.shareIcon > 0) {
            msg.thumbData = ThumbUtils.getThumb(mCtx, shareDO.shareIcon, shareDO.shareIconSize);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = isShareToUser() ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    protected abstract boolean isShareToUser();

    @Override
    public void onResponse(BaseResponse resp) {
        switch (resp.errCode) {
            case BaseResponse.ErrCode.ERR_OK:
                Toasts.show("分享成功", mCtx);
                break;
            case BaseResponse.ErrCode.ERR_USER_CANCEL:
                Toasts.show("取消分享", mCtx);
                break;
            case BaseResponse.ErrCode.ERR_AUTH_DENIED:
                Toasts.show("分享被拒绝", mCtx);
                break;
            default:
                break;
        }

        if (WXGlobal.shareCallback != null) {
            String platform = isShareToUser() ? "weixin" : "wechatDiscover";
            WXGlobal.shareCallback.onComplete(platform, resp.errCode == BaseResponse.ErrCode.ERR_OK);
            WXGlobal.shareCallback = null;
        }
    }

}
