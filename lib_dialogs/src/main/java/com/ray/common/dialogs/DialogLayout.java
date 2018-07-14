package com.ray.common.dialogs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 对话框的layout，包含头和底部按钮
 * 中间的内容格局自己需求定制
 */
public class DialogLayout extends LinearLayout {

    private static final int STATE_LEFT = 0x1;
    private static final int STATE_MIDDLE = 0x2;
    private static final int STATE_RIGHT = 0x4;

    private TextView titleTv;
    private View titleLayout;
    private View btnMsgDivider;
    private Button rightBtn, middleBtn, leftBtn;
    private View rootView, contentView;
    private ViewGroup contentLayout;

    private CharSequence title;
    private CharSequence leftBtnStr, midBtnStr, rightBtnStr;
    private int btnState;

    private Dialog attachDialog;

    public DialogLayout(Context context) {
        this(context, null);
    }

    public DialogLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.dialog_widget_dialog_layout, this, true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DialogLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        if (childCount > 1) {
            throw new IllegalStateException("DialogLayout can host only one direct child");
        }
        //将xml中的content view 移到dialog layout中
        View content = getChildAt(0);
        removeViewAt(0);
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_widget_dialog_layout, this, true);
        initViews();
        setView(content);
    }

    private void initViews() {
        rootView = findViewById(R.id.dialog_root_view);
        titleTv = (TextView) findViewById(R.id.dialog_title);
        titleLayout = findViewById(R.id.dialog_title_layout);
        btnMsgDivider = findViewById(R.id.dialog_btn_msg_divider);
        rightBtn = (Button) findViewById(R.id.dialog_btn_confirm);
        middleBtn = (Button) findViewById(R.id.dialog_btn_middle);
        leftBtn = (Button) findViewById(R.id.dialog_btn_cancel);
        contentLayout = (ViewGroup) findViewById(R.id.dialog_content_layout);
    }

    public void setAttachDialog(Dialog dialog){
        this.attachDialog = dialog;
    }

    public void setTitle(int textId) {
        CharSequence title = getContext().getText(textId);
        setTitle(title);
    }

    public void setTitle(CharSequence title) {
        titleLayout.setVisibility(View.VISIBLE);
        titleTv.setText(title);
        this.title = title;
    }

    public View setView(int layoutId) {
        contentLayout.removeAllViews();
        View view = LayoutInflater.from(getContext()).inflate(layoutId, contentLayout, false);
        setView(view);
        return view;
    }

    public View setView(View view) {
        //防止重复设置
        if(contentView == view){
            return contentView;
        }
        contentLayout.removeAllViews();
        contentLayout.addView(view);
        contentView = view;
        return contentView;
    }

    public void setRightButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setButtonOnClick(rightBtn, textId, onClickListener);
        btnState |= STATE_RIGHT;
        setBtnNoBg();
    }

    public void setRightButton(String text, DialogInterface.OnClickListener onClickListener) {
        setButtonOnClick(rightBtn, text, onClickListener);
        btnState |= STATE_RIGHT;
        setBtnNoBg();
    }

    public void setPositiveButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setRightButton(textId, onClickListener);
    }

    public void setPositiveButton(String text, DialogInterface.OnClickListener onClickListener) {
        setRightButton(text, onClickListener);
    }

    public void setMiddleButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setButtonOnClick(middleBtn, textId, onClickListener);
        btnState |= STATE_MIDDLE;
        setBtnNoBg();
    }

    public void setNeutralButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setMiddleButton(textId, onClickListener);
    }

    public void setNegativeButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setLeftButton(textId, onClickListener);
    }

    public void setLeftButton(int textId, DialogInterface.OnClickListener onClickListener) {
        setButtonOnClick(leftBtn, textId, onClickListener);
        btnState |= STATE_LEFT;
        setBtnNoBg();
    }

    private void setButtonOnClick(Button btn, int textId, final DialogInterface.OnClickListener onClickListener) {
        btnMsgDivider.setVisibility(View.VISIBLE);
        final int which;
        final boolean dismissAfter;
        if (btn == middleBtn) {
            findViewById(R.id.dialog_btn_middle_divider).setVisibility(View.VISIBLE);
            which = DialogInterface.BUTTON_NEUTRAL;
            dismissAfter = false;
        } else if (btn == leftBtn) {
            findViewById(R.id.dialog_btn_cancel_divider).setVisibility(View.VISIBLE);
            which = DialogInterface.BUTTON_NEGATIVE;
            dismissAfter = false;
        }else{
            which = DialogInterface.BUTTON_POSITIVE;
            dismissAfter = false;
        }

        btn.setVisibility(View.VISIBLE);
        btn.setText(textId);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachDialog == null){
                    throw  new RuntimeException("DialogLayout 必须设置依附的 Dialog");
                }
                if(onClickListener != null){
                    onClickListener.onClick(attachDialog, which);
                }
                if(dismissAfter){
                    attachDialog.dismiss();
                }
            }
        });
    }

    private void setButtonOnClick(Button btn, String text, final DialogInterface.OnClickListener onClickListener) {
        btnMsgDivider.setVisibility(View.VISIBLE);
        final int which;
        final boolean dismissAfter;
        if (btn == middleBtn) {
            findViewById(R.id.dialog_btn_middle_divider).setVisibility(View.VISIBLE);
            which = DialogInterface.BUTTON_NEUTRAL;
            dismissAfter = false;
        } else if (btn == leftBtn) {
            findViewById(R.id.dialog_btn_cancel_divider).setVisibility(View.VISIBLE);
            which = DialogInterface.BUTTON_NEGATIVE;
            dismissAfter = false;
        }else{
            which = DialogInterface.BUTTON_POSITIVE;
            dismissAfter = false;
        }

        btn.setVisibility(View.VISIBLE);
        btn.setText(text);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachDialog == null){
                    throw  new RuntimeException("DialogLayout 必须设置依附的 Dialog");
                }
                if(onClickListener != null){
                    onClickListener.onClick(attachDialog, which);
                }
                if(dismissAfter){
                    attachDialog.dismiss();
                }
            }
        });
    }

    private void setBtnNoBg(){
        leftBtn.setBackgroundColor(Color.TRANSPARENT);
        middleBtn.setBackgroundColor(Color.TRANSPARENT);
        rightBtn.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setBtnBg() {
        boolean isLeftShow = (btnState & STATE_LEFT) == STATE_LEFT;
        boolean isMiddleShow = (btnState & STATE_MIDDLE) == STATE_MIDDLE;
        boolean isRightShow = (btnState & STATE_RIGHT) == STATE_RIGHT;

        //三个全部不显示
        if (!isLeftShow && !isMiddleShow && !isRightShow) {
            return;
        }
        //三个全部显示
        if (isLeftShow && isMiddleShow && isRightShow) {
            leftBtn.setBackgroundResource(R.drawable.dialog_btn_left);
            middleBtn.setBackgroundResource(R.drawable.dialog_btn_middle);
            rightBtn.setBackgroundResource(R.drawable.dialog_btn_right);
            return;
        }

        //单个按钮显示
        if (onlySingle(isLeftShow, isMiddleShow, isRightShow)) {
            leftBtn.setBackgroundResource(R.drawable.dialog_btn_single);
            middleBtn.setBackgroundResource(R.drawable.dialog_btn_single);
            rightBtn.setBackgroundResource(R.drawable.dialog_btn_single);
        } else {//两个按钮显示
            if (isLeftShow) {
                leftBtn.setBackgroundResource(R.drawable.dialog_btn_left);
                if (isMiddleShow) {
                    middleBtn.setBackgroundResource(R.drawable.dialog_btn_right);
                } else {
                    rightBtn.setBackgroundResource(R.drawable.dialog_btn_right);
                }
            } else {
                middleBtn.setBackgroundResource(R.drawable.dialog_btn_left);
                rightBtn.setBackgroundResource(R.drawable.dialog_btn_right);
            }
        }
    }

    private boolean atLeastTwo(boolean a, boolean b, boolean c) {
        return a ? (b || c) : (b && c);
    }

    private boolean onlySingle(boolean a, boolean b, boolean c) {
        return a ? !(b || c) : b || c;
    }

}
