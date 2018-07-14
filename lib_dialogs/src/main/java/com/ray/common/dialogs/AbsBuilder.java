package com.ray.common.dialogs;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zyl on 2016/4/12.
 */
public abstract class AbsBuilder implements IBuilder {
    protected Context context;
    protected CharSequence title;
    protected int width;
    protected int height;
    protected int leftBtnRes,midBtnRes, rightBtnRes;
    protected String rightBtnStr;
    protected DialogInterface.OnClickListener rightClick,midClick, leftClick;
    protected boolean mCancelable = true;
    protected DialogInterface.OnCancelListener cancelListener;

    public AbsBuilder setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public AbsBuilder setPositiveButton(int textId, DialogInterface.OnClickListener onClickListener) {
        rightBtnRes = textId;
        rightClick = onClickListener;
        return this;
    }

    public AbsBuilder setPositiveButton(String text, DialogInterface.OnClickListener onClickListener) {
        rightBtnStr = text;
        rightClick = onClickListener;
        return this;
    }

    public AbsBuilder setNeutralButton(int textId, DialogInterface.OnClickListener onClickListener) {
        midBtnRes = textId;
        midClick = onClickListener;
        return this;
    }

    public AbsBuilder setNegativeButton(int textId, DialogInterface.OnClickListener onClickListener) {
        leftBtnRes = textId;
        leftClick = onClickListener;
        return this;
    }

    public AbsBuilder setCancelable(boolean flag) {
        mCancelable = flag;
        return this;
    }

    public AbsBuilder setOnCancelListener(DialogInterface.OnCancelListener cancelListener){
        this.cancelListener = cancelListener;
        return this;
    }

    public AbsBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public AbsBuilder setHeight(int height) {
        this.height = height;
        return this;
    }
}
