package com.tomtaw.third_party.api.auth;

/**
 * Created by zyl on 2017/4/28.
 */

public interface IUserInfo {
    int MALE = 1;
    int FEMALE = 2;

    String nickName();
    int sex();
    String avatar();
}
