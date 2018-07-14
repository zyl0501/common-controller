package com.tomtaw.third_party.api.share;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分享时，内容的数据对象
 * Created by Ray on 15/9/11.
 */
public final class ShareDO implements Parcelable {
    public String url;
    public String imgUrl;
    public String content;
    public String title;
    public int shareIcon;
    public int shareIconSize;

    public ShareDO() {
        shareIcon = -1;
        shareIconSize = 150;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.imgUrl);
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeInt(this.shareIcon);
        dest.writeInt(this.shareIconSize);
    }

    protected ShareDO(Parcel in) {
        this.url = in.readString();
        this.imgUrl = in.readString();
        this.content = in.readString();
        this.title = in.readString();
        this.shareIcon = in.readInt();
        this.shareIconSize = in.readInt();
    }

    public static final Creator<ShareDO> CREATOR = new Creator<ShareDO>() {
        @Override
        public ShareDO createFromParcel(Parcel source) {
            return new ShareDO(source);
        }

        @Override
        public ShareDO[] newArray(int size) {
            return new ShareDO[size];
        }
    };
}
