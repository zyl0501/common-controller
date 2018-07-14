package com.tomtaw.third_party.api.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyl on 2017/4/28.
 */

public class AuthResult implements IAuthResp, IUserInfo, Parcelable {
    public String openid;
    public String unionId;
    public String nickname;
    public int sex;
    public String avatar;

    @Override
    public String getOpenId() {
        return openid;
    }

    @Override
    public String getTargetId() {
        return unionId;
    }

    @Override
    public String nickName() {
        return nickname;
    }

    @Override
    public int sex() {
        return sex;
    }

    @Override
    public String avatar() {
        return avatar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.openid);
        dest.writeString(this.unionId);
        dest.writeString(this.nickname);
        dest.writeInt(this.sex);
        dest.writeString(this.avatar);
    }

    public AuthResult() {
    }

    protected AuthResult(Parcel in) {
        this.openid = in.readString();
        this.unionId = in.readString();
        this.nickname = in.readString();
        this.sex = in.readInt();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<AuthResult> CREATOR = new Parcelable.Creator<AuthResult>() {
        @Override
        public AuthResult createFromParcel(Parcel source) {
            return new AuthResult(source);
        }

        @Override
        public AuthResult[] newArray(int size) {
            return new AuthResult[size];
        }
    };
}
