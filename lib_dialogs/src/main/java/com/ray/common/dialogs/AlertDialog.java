package com.ray.common.dialogs;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by zyl on 2016/4/1.
 */
public class AlertDialog extends BaseDialog {
    private CharSequence message;
    private View mCustomView;

    protected AlertDialog(Context context) {
        super(context);
    }

    @Override
    protected View getContentView(ViewGroup parent) {
        return mCustomView;
    }

    @Override
    protected void bindView(View contentView) {
        setMessage(message);
    }

    @Override
    protected View setView(View view) {
        return mCustomView = super.setView(view);
    }

    @Override
    protected View setView(int layoutId) {
        return mCustomView = super.setView(layoutId);
    }

    public AlertDialog setMessage(CharSequence message) {
        this.message = message;
        if(contentView != null && contentView instanceof TextView) {
            ((TextView) contentView).setText(message);
        }
        return this;
    }

    public AlertDialog setMessageGravity(int gravity) {
        if(contentView != null && contentView instanceof TextView) {
            ((TextView) contentView).setGravity(gravity);
        }
        return this;
    }

    public static class Builder extends AbsBuilder {
        CharSequence message;
        int msgGravity;
        View mView;
        int mLayoutResId;

        public Builder(Context context) {
            this.context = context;
            //默认用这个layout
            mLayoutResId = R.layout.dialog_layout_dialog_alert;
            msgGravity = GravityCompat.START;
            width = INVALID_SIZE;
            height = INVALID_SIZE;
        }

        public Builder setTitle(CharSequence title) {
            return (Builder) super.setTitle(title);
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int msgRes) {
            this.message = context.getText(msgRes);
            return this;
        }

        /**
         * @see {@link android.view.Gravity}
         * @param gravity
         * @return
         */
        public Builder setMessageGravity(int gravity){
            this.msgGravity = gravity;
            return this;
        }

        public Builder setView(View view) {
            this.mView = view;
            this.mLayoutResId = INVALID;
            return this;
        }

        public Builder setView(int resId) {
            this.mView = null;
            this.mLayoutResId = resId;
            return this;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        @Override
        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(context);
            if (mView == null) {
                dialog.setView(mLayoutResId);
            } else {
                dialog.setView(mView);
            }
            dialog.setWidth(width);
            dialog.setHeight(height);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setMessageGravity(msgGravity);
            if (rightBtnRes> 0 ){
                dialog.setPositiveButton(rightBtnRes, rightClick);
            }else {
                dialog.setPositiveButton(rightBtnStr, rightClick);
            }

            dialog.setNegativeButton(leftBtnRes, leftClick);
            dialog.setCancelable(mCancelable);
            return dialog;
        }
    }

}
