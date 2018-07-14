package com.lzy.imagepicker.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.lzy.imagepicker.R;

/**
 * Created by xiaodigua on 2016/9/7.
 */
public class DialogHintSimpleTwo extends Dialog {
    public DialogHintSimpleTwo(Context context) {
        super(context);
    }

    public DialogHintSimpleTwo(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogHintSimpleTwo(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private Activity mActivity;

    /**
     * dialog 服务选择
     *
     * @param context
     * @param cancelable
     */
    public DialogHintSimpleTwo(Context context, boolean cancelable, String showInfo, final OnSelectListener listener) {
        super(context);
        mActivity = (Activity) context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        //getWindow().setWindowAnimations(R.style.AnimBottom);
        View rootView = getLayoutInflater().inflate(
                R.layout.pk_dialog_hint_simple_two, null);
        int screenWidth = mActivity.getWindowManager().getDefaultDisplay()
                .getWidth();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        super.setContentView(rootView, params);
        this.setCancelable(cancelable);


        TextView tvInfo = (TextView) rootView.findViewById(R.id.tv_info);

        tvInfo.setText(showInfo);

        rootView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onNegative();
                }
                dismiss();
            }
        });


        rootView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onPositive();
                }
                dismiss();
            }
        });
    }


    public interface OnSelectListener {
        void onPositive();

        void onNegative();
    }
}
