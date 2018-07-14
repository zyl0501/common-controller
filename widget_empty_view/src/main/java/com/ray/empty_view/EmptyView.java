package com.ray.empty_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by Ray on 15/11/3.
 * 参照芒果ui规范-->异常情况.jpg
 * 分情况显示不同的异常页面：1.网络不给力 2.网络连接错误 3.暂无内容 4.敬请期待
 */
public class EmptyView extends FrameLayout {

    //以下为界面完整的情况下，从上往下一次排列的4个控件(实际情况可能缺失)
    private FontIcon iconImg;
    private View iconImgLayout;
    private TextView titleTv;
    private TextView msgTv;
    private Button mainBtn;

    private View loadingView;
    private View emptyRootView;

    private int loadingRes;
    //    private boolean showMsg,
    private boolean showMainBtn;
    private CharSequence title, message, mainBtnText;
    private OnClickListener mainBtnClickListener;
    private CharSequence iconFontStr;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
//        showMsg = t.getBoolean(R.styleable.EmptyView_showMsg, false);
        showMainBtn = t.getBoolean(R.styleable.EmptyView_showMainBtn, false);
        title = t.getText(R.styleable.EmptyView_emptyTitle);
        message = t.getText(R.styleable.EmptyView_emptyMessage);
        mainBtnText = t.getText(R.styleable.EmptyView_emptyMainBtnText);
        loadingRes = t.getResourceId(R.styleable.EmptyView_loadingRes, R.layout.empty_item_common_loading_trans);
        setBackgroundColor(getResources().getColor(R.color.empty_b_background));
        t.recycle();
        iconFontStr = context.getText(R.string.empty_icon_font_search);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //empty view 在需要显示的时候才渲染出来
    }

    private void initViews() {
        iconImg = (FontIcon) findViewById(R.id.iconImg);
        titleTv = (TextView) findViewById(R.id.emptyView_title);
        msgTv = (TextView) findViewById(R.id.emptyView_message);
        mainBtn = (Button) findViewById(R.id.emptyView_btn_main);
        iconImgLayout = findViewById(R.id.iconImgLayout);
    }

    private void refreshEmptyViews() {
        //标题
        titleTv.setText(title);
        titleTv.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        //副标题
        msgTv.setText(message);
        msgTv.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        //icon
        iconImg.setText(iconFontStr);
        iconImgLayout.setVisibility(TextUtils.isEmpty(iconFontStr) ? View.GONE : View.VISIBLE);
        //按钮
        mainBtn.setText(mainBtnText);
        mainBtn.setVisibility(showMainBtn ? View.VISIBLE : View.GONE);
        mainBtn.setOnClickListener(mainBtnClickListener);
    }

    public void showLoading() {
        showLoading(null);
    }

    /**
     * 画面变化为正在加载中
     */
    public void showLoading(String text) {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(getContext()).inflate(loadingRes, this, false);
            this.addView(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (TextUtils.isEmpty(text)) {
            text = getResources().getString(R.string.empty_default_loading_msg);
        }

        TextView loadingText = (TextView) loadingView.findViewById(R.id.loadingText);
        if (loadingText != null) {
            loadingText.setText(text);
        }
        loadingView.setVisibility(View.VISIBLE);
        if (emptyRootView != null) {
            emptyRootView.setVisibility(View.GONE);
        }

        bringChildToFront(loadingView);
        requestLayout();
        invalidate();
        this.setVisibility(View.VISIBLE);
    }


    /**
     * 隐藏异常页面，即页面显示正常时调用
     */
    public void hideAbove() {
        setVisibility(View.GONE);
    }

    /**
     * 网络错误
     */
    public void showNetworkError() {
        Context ctx = getContext();
        title = ctx.getText(R.string.empty_common_overtime);
        message = "";
        iconFontStr = ctx.getText(R.string.empty_icon_font_overtime);
        mainBtnText = ctx.getText(R.string.empty_reload);
        showMainBtn = true;
        showEmpty();
    }

    /**
     * 网络错误
     */
    public void showNetworkError(String msg) {
        Context ctx = getContext();
        title = msg;
        message = "";
        iconFontStr = ctx.getText(R.string.empty_icon_font_overtime);
        mainBtnText = ctx.getText(R.string.empty_reload);
        showMainBtn = true;
        showEmpty();
    }

    /**
     * 网络错误
     *
     * @param listener
     */
    public void showNetworkError(OnClickListener listener) {
        mainBtnClickListener = listener;
        showNetworkError();
    }

    /**
     * 网络错误
     *
     * @param listener
     */
    public void showNetworkError(String msg,OnClickListener listener) {
        mainBtnClickListener = listener;
        showNetworkError(msg);
    }

    /**
     * url 无效
     */
    public void showUrlInvalid(String msg){
        Context ctx = getContext();
        title = msg;
        message = "";
        iconFontStr = ctx.getText(R.string.empty_icon_font_overtime);
        mainBtnText = ctx.getText(R.string.empty_return_to_prepage);
        showMainBtn = true;
        showEmpty();
    }

    /**
     * url 无效
     */
    public void showUrlInvalid(String msg,OnClickListener listener){

        mainBtnClickListener = listener;
        if (msg == null ||msg.isEmpty()  ){
            showUrlInvalid((String)(getContext().getText(R.string.empty_common_urlinvalid)));
        }else {
            showUrlInvalid(msg);
        }
    }


    /**
     * 没有病患
     */
    private void showNoPatient() {
        Context ctx = getContext();
        title = ctx.getString(R.string.empty_common_no_data_1, "患者");
        ;
        message = "快去添加患者吧";
        iconFontStr = ctx.getText(R.string.empty_icon_font_avatar);
        mainBtnText = "添加患者";
        showMainBtn = true;
        showEmpty();
    }

    /**
     * 没有病患
     *
     * @param listener
     */
    public void showNoPatient(OnClickListener listener) {
        mainBtnClickListener = listener;
        showNoPatient();
    }

    /**
     * 没有搜索结果
     */
    public void showNoSearchResult() {
        Context ctx = getContext();
        title = "没有搜索结果";
        message = "换个关键字试试";
        iconFontStr = ctx.getText(R.string.empty_icon_font_search);
        showMainBtn = false;
        showEmpty();
    }

    /**
     * 敬请期待
     */
    public void showExpect() {
        Context ctx = getContext();
        title = ctx.getText(R.string.empty_please_expect);
        message = "更多精彩等你发现";
        iconFontStr = ctx.getText(R.string.empty_icon_font_gift);
        showMainBtn = false;
        showEmpty();
    }

    /**
     * 没有记录
     */
    public void showNoRecord() {
        showNoData("记录");
    }

    public void showNoData(CharSequence iconStr, String titleStr, String msg) {
        title = titleStr;
        message = msg;
        showMainBtn = false;
        iconFontStr = iconStr;
        showEmpty();
    }

    /**
     * 没有数据的情况
     * <p/>
     * 你还没有{*}哦
     * 还不赶快行动
     */
    public void showNoData(String t) {
        Context ctx = getContext();
        title = ctx.getString(R.string.empty_common_no_data_1, t);
        message = ctx.getText(R.string.empty_common_to_do);
        iconFontStr = ctx.getText(R.string.empty_icon_font_no_data);
        showMainBtn = false;
        showEmpty();
    }

    private void showEmpty() {
        if (this.emptyRootView == null) {
            emptyRootView = LayoutInflater.from(getContext()).inflate(R.layout.empty_widget_empty_view, this, false);
            this.addView(emptyRootView);
            initViews();
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        emptyRootView.setVisibility(View.VISIBLE);
        refreshEmptyViews();
        bringChildToFront(emptyRootView);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * UI那边不统一empty view，该方法只是临时应对UI的要求
     */
    public void showEmpty(int layoutRes){
        removeAllViews();
        this.emptyRootView = null;
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this, false);
        this.addView(view);
        bringChildToFront(view);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * 设置图标下面的大标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        if (!TextUtils.equals(this.title, title)) {
            this.title = title;
            //可能还没有渲染
            if (titleTv != null) {
                titleTv.setText(title);
            }
        }
    }

    /**
     * 设置大标题下面的小标题文字
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (!TextUtils.equals(this.message, message)) {
            this.message = message;
            msgTv.setText(message);
        }
    }

    /**
     * 设置界面上方的提示图标
     *
     * @param iconFontStr IconFont的字符串资源
     */
    public void setIconView(CharSequence iconFontStr) {
        this.iconFontStr = iconFontStr;
        //可能还没有渲染
        if (iconImg != null) {
            iconImg.setText(iconFontStr);
        }
    }

    /**
     * 给界面底下的按钮设置按钮文字和点击监听
     *
     * @param txt      按钮上显示的文字
     * @param listener 要设置的点击监听器
     */
    public void setMainBtn(CharSequence txt, OnClickListener listener) {
        this.mainBtnText = txt;
        this.mainBtnClickListener = listener;
        //可能还没有渲染
        if (mainBtn != null) {
            mainBtn.setText(txt);
            mainBtn.setOnClickListener(listener);
        }
    }

    public void setMainBtn(int strId, OnClickListener listener) {
        setMainBtn(getContext().getString(strId), listener);
    }

    /**
     * 仅给按钮设置点击监听，如果按钮没有文字，默认显示"重新加载"
     *
     * @param listener 要设置的点击监听器
     */
    public void setMainBtn(OnClickListener listener) {
        setMainBtn(TextUtils.isEmpty(mainBtnText) ? getContext().getString(R.string.empty_reload) : mainBtnText, listener);
    }

    /**
     * 是否显示界面底下的按钮
     */
    public void setShowMainBtn(boolean show) {
        if (this.showMainBtn != show) {
            this.showMainBtn = show;
            //可能还没有渲染
            if (mainBtn != null) {
                mainBtn.setVisibility(this.showMainBtn ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * 不允许父view响应事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 允许向子view传递事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
