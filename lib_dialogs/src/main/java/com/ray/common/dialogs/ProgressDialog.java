package com.ray.common.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by zyl on 2016/4/11.
 */
public class ProgressDialog extends BaseDialog {
    private TextView contentTv;
    private CharSequence content;

    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected View getContentView(ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dialog_layout_dialog_progress, parent, false);
    }

    @Override
    protected void bindView(View contentView) {
        setMessage(content);
    }

    public ProgressDialog setMessage(CharSequence content) {
        this.content = content;
        if(contentView != null) {
            contentTv = (TextView) findViewById(R.id.message);
            contentTv.setText(content);
        }
        return this;
    }

    public static class Builder extends AbsBuilder {
        CharSequence message;

        public Builder(Context context) {
            this.context = context;
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

        public ProgressDialog show() {
            ProgressDialog dialog = create();
            dialog.show();
            return dialog;
        }

        @Override
        public ProgressDialog create() {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setWidth(width);
            dialog.setHeight(height);
            dialog.setMessage(message);
            dialog.setCancelable(mCancelable);
            return dialog;
        }
    }
}
