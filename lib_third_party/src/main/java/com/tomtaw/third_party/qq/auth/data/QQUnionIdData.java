package com.tomtaw.third_party.qq.auth.data;

/**
 * Created by zyl on 2017/5/3.
 */

public class QQUnionIdData {

    /**
     * client_id : YOUR_APPID
     * openid : YOUR_OPENID
     * unionid : YOUR_UNIONID
     */

    private String client_id;
    private String openid;
    private String unionid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnioid(String unionid) {
        this.unionid = unionid;
    }
}
