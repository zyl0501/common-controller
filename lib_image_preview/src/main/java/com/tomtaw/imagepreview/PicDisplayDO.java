package com.tomtaw.imagepreview;

import android.os.Parcel;
import android.os.Parcelable;

public class PicDisplayDO implements Parcelable {
    String[] uris;
    boolean showDelete;
    boolean showSaveToArchive;
    boolean showTitle;
    int firstIndex = 1;

    public PicDisplayDO setUris(String... uris) {
        this.uris = uris;
        return this;
    }

    public PicDisplayDO setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        return this;
    }

    public PicDisplayDO setShowSaveToArchive(boolean showSaveToArchive) {
        this.showSaveToArchive = showSaveToArchive;
        return this;
    }

    public PicDisplayDO setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }


    public PicDisplayDO(String... uris) {
        this.uris = uris;
        showDelete = false;
        showSaveToArchive = false;
        showTitle = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.uris);
        dest.writeByte(showDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(showSaveToArchive ? (byte) 1 : (byte) 0);
        dest.writeByte(showTitle ? (byte) 1 : (byte) 0);
    }

    protected PicDisplayDO(Parcel in) {
        int N = in.readInt();
        uris = new String[N];
        for (int i = 0; i < N; i++) {
            uris[i] = in.readString();
        }
        this.showDelete = in.readByte() != 0;
        this.showSaveToArchive = in.readByte() != 0;
        this.showTitle = in.readByte() != 0;
    }

    public static final Creator<PicDisplayDO> CREATOR = new Creator<PicDisplayDO>() {
        public PicDisplayDO createFromParcel(Parcel source) {
            return new PicDisplayDO(source);
        }

        public PicDisplayDO[] newArray(int size) {
            return new PicDisplayDO[size];
        }
    };
}