package com.ray.common.api;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonResult<T> implements ApiResult<T> {
    protected int code;
    protected String msg;
    protected T data;
    protected boolean success = false;

    public JsonResult(IErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.code = 200;
        this.data = data;
        this.success = true;
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = true;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public T data() {
        return data;
    }

    @Override
    public String msg() {
        return msg;
    }

    @Override
    public boolean success() {
        return success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("msg", msg);
            json.put("data", data);
            json.put("success", success);
        } catch (JSONException e) {
        }
        return json.toString();
    }

    public static <T> ApiResult<T> failure(int code, String msg) {
        return new JsonResult<>(code, msg);
    }

    public static <T> ApiResult<T> failure(IErrorCode errorCode) {
        return new JsonResult<>(errorCode);
    }
}
