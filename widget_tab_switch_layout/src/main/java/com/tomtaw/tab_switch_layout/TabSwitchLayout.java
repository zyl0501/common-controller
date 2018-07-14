package com.tomtaw.tab_switch_layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 二选一的tab
 */
public class TabSwitchLayout extends LinearLayout {
    private View leftView;
    private View rightView;

    private OnClickListener leftClick;
    private OnClickListener rightClick;

  /**
   * perform点击的时候，强制执行
   */
  private boolean forceClick;

    /**
     * tag left view is select
     */
    private Boolean isSelLeft = null;

    public TabSwitchLayout(Context context) {
        this(context, null);
    }

    public TabSwitchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("TabSwitchLayout must have 2 child");
        }
        leftView = getChildAt(0);
        rightView = getChildAt(1);

        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forceClick || isSelLeft == null || !isSelLeft){
                    isSelLeft = true;
                    leftView.setSelected(true);
                    rightView.setSelected(false);
                    if (leftClick != null) {
                        leftClick.onClick(view);
                    }
                }
            }
        });

        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forceClick || isSelLeft == null || isSelLeft){
                    isSelLeft = false;
                    leftView.setSelected(false);
                    rightView.setSelected(true);
                    if(rightClick != null){
                        rightClick.onClick(view);
                    }
                }
            }
        });
    }


    public void setLeftClick(OnClickListener leftClick) {
        this.leftClick = leftClick;
    }

    public void setRightClick(OnClickListener rightClick) {
        this.rightClick = rightClick;
    }

    public void performLeftClick(boolean forceClick){
        boolean old = this.forceClick;
        this.forceClick = forceClick;
        leftView.performClick();
        this.forceClick = old;
    }

    public void performRightClick(boolean forceClick){
        boolean old = this.forceClick;
        this.forceClick = forceClick;
        rightView.performClick();
        this.forceClick = old;
    }

    /**
     * 设置左侧选中，不触发点击事件
     * @param sel 左侧tab是否选中
     */
    public void setLeftSelect(boolean sel){
        leftView.setSelected(sel);
        rightView.setSelected(!sel);
    }
    /**
     * 设置左侧选中，不触发点击事件
     * @param sel 右侧侧tab是否选中
     */
    public void setRightSelect(boolean sel){
        leftView.setSelected(!sel);
        rightView.setSelected(sel);
    }
}
