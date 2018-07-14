package com.ray.common.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ray.common.dialogs.utils.Utils;

/**
 */
public abstract class BaseDialog extends AppCompatDialog {
    protected static final int INVALID = -1;
    protected static final int INVALID_SIZE = -3;
    protected Context context;
    protected DialogLayout dialogLayout;
    protected View contentView;
    protected int width = INVALID_SIZE;
    protected int height = INVALID_SIZE;

    public BaseDialog(Context context) {
        this(context, Builders.defaultTheme);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        if (!(context instanceof Activity)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            if (window != null) {
                //需要添加的语句
                window.setType(isHigh71() ? WindowManager.LayoutParams.TYPE_PHONE : WindowManager.LayoutParams.TYPE_TOAST);
            } else {
                Log.w("dialog", "dialog window is null!");
            }
        }

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);

        dialogLayout = (DialogLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_dialog_basic, null);
        setContentView(dialogLayout);
        dialogLayout.setAttachDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentView = dialogLayout.setView(getContentView(dialogLayout));
        bindView(contentView);

        DisplayMetrics metrics = Utils.getScreenHeightAndWidth(context);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        android.view.WindowManager.LayoutParams p = this.getWindow().getAttributes();
        if (this.width == INVALID_SIZE) {
            p.width = (int) ((width < height ? width : height) * 0.95);
        } else {
            p.width = this.width;
        }
        if (this.height != INVALID_SIZE) {
            p.height = this.height;
        }
        getWindow().setAttributes(p);
        super.onCreate(savedInstanceState);
        if (dialogLayout != null) {
            dialogLayout.setLayoutParams(
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT)
            );
        }
    }

    /**
     * 显示内容的layout
     *
     * @return
     */
    protected abstract View getContentView(ViewGroup parent);

    protected abstract void bindView(View contentView);

    @Override
    public void setTitle(CharSequence chars) {
        if (!TextUtils.isEmpty(chars))
            dialogLayout.setTitle(chars);
    }

    public void setTitle(int textId) {
        if (textId > 0)
            dialogLayout.setTitle(textId);
    }

    protected View setView(int layoutId) {
        if (layoutId > 0)
            return contentView = dialogLayout.setView(layoutId);
        return null;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected View setView(View view) {
        return contentView = dialogLayout.setView(view);
    }

    public BaseDialog setRightButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setRightButton(textId, onClickListener);
        return this;
    }

    public BaseDialog setPositiveButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setPositiveButton(textId, onClickListener);
        return this;
    }

    public BaseDialog setPositiveButton(String text, OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text))
            dialogLayout.setPositiveButton(text, onClickListener);
        return this;
    }

    public BaseDialog setMiddleButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setMiddleButton(textId, onClickListener);
        return this;
    }

    public BaseDialog setNeutralButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setNeutralButton(textId, onClickListener);
        return this;
    }

    public BaseDialog setNegativeButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setNegativeButton(textId, onClickListener);
        return this;
    }

    public BaseDialog setLeftButton(int textId, OnClickListener onClickListener) {
        if (textId > 0)
            dialogLayout.setLeftButton(textId, onClickListener);
        return this;
    }

    private boolean isHigh71() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

}
