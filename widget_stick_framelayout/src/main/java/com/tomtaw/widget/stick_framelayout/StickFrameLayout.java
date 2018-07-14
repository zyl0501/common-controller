package com.tomtaw.widget.stick_framelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tomtaw.widget.stick_framelayout.ViewUtils.INVALID_MARGIN;


/**
 * 用于滚动时view悬浮停留的父控件
 * 支持的view有{@link AbsListView}， {@link RecyclerView}
 * tip: 这里的stick，表示的是item内容stick，不是header的stick
 */
public class StickFrameLayout extends FrameLayout {
    private static final int INVALID_TOP = Integer.MIN_VALUE;
    public static final int INVALID_POS = -1;

    View view;
    /**
     * item 里面需要stick的view的id
     */
    int itemStickId;
    List<Integer> stickPositions;

    View headerStickView;

    StickListener mListener;

    int oldStickPos = -1;

    public StickFrameLayout(Context context) {
        super(context);
        init();
    }

    public StickFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StickFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        stickPositions = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        registerScroll(this);
    }

    private boolean registerScroll(ViewGroup parent) {
        int size = parent.getChildCount();
        if (size <= 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof AbsListView) {
                view = child;
                registerListScroll();
                return true;
            } else if (child instanceof RecyclerView) {
                view = child;
                registerRecyclerScroll();
                return true;
            } else if (child instanceof ViewGroup) {
                return registerScroll((ViewGroup) child);
            }
        }
        return false;
    }

    private void registerListScroll() {
    }

    private void registerRecyclerScroll() {
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                tryStickRecycler(false);
            }
        });
    }

    private void tryStickRecycler(boolean animate) {
        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (!(lm instanceof LinearLayoutManager) || stickPositions == null || stickPositions.isEmpty()) {
            //只LinearLayoutManager的情况
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) lm;
        int firstVisiblePos = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePos = layoutManager.findLastVisibleItemPosition();
        int firstTarget = INVALID_POS;
        int secondTarget = INVALID_POS;
        int pastRecentTarget = INVALID_POS;
        //第一个可见的position是否超过第一个target
        boolean isOverFirstTarget = false;
        //找出大于等于第一个可见的target
        for (int i = 0, size = stickPositions.size(); i < size; i++) {
            int value = stickPositions.get(i);
            if (i == 0 && value < firstVisiblePos) {
                isOverFirstTarget = true;
            }
            if (value < firstVisiblePos) {
                pastRecentTarget = value;
            }
            if (value >= firstVisiblePos && value <= lastVisiblePos) {
                firstTarget = value;
                if (i < size - 1) {
                    int secondValue = stickPositions.get(i + 1);
                    if (secondValue <= lastVisiblePos) {
                        secondTarget = secondValue;
                    }
                }
                break;
            }
        }
        //页面内没有stick的position
        if (firstTarget == INVALID_POS) {
            if (isOverFirstTarget) {
                notifyStick(pastRecentTarget);
                ViewUtils.setMargin(headerStickView, INVALID_MARGIN, 0, INVALID_MARGIN, INVALID_MARGIN);
                setViewVisibility(headerStickView, View.VISIBLE, animate);
            } else {
                setViewVisibility(headerStickView, View.GONE, animate);
            }
            return;
        }
        //下面为页面内至少存在一个stick的情况
        int offPos = firstTarget - firstVisiblePos;
        View firstStickItem = recyclerView.getChildAt(offPos);
        View firstStickView = firstStickItem.findViewById(itemStickId);
        int firstTop = firstStickItem.getId() == itemStickId ?
                firstStickItem.getTop() : firstStickItem.getTop() + firstStickView.getTop();
        //页面中只出现了一个stick
        if (secondTarget == INVALID_POS) {
            if (firstTop < 0) {
                notifyStick(firstTarget);
                ViewUtils.setMargin(headerStickView, INVALID_MARGIN, 0, INVALID_MARGIN, INVALID_MARGIN);
                setViewVisibility(headerStickView, View.VISIBLE, animate);
            } else {
                if (isOverFirstTarget) {
                    notifyStick(pastRecentTarget);
                    int marginTop = 0;
                    if (firstTop < headerStickView.getHeight()) {
                        marginTop = -(headerStickView.getHeight() - firstTop);
                    }
                    ViewUtils.setMargin(headerStickView, INVALID_MARGIN, marginTop, INVALID_MARGIN, INVALID_MARGIN);
                    setViewVisibility(headerStickView, View.VISIBLE, animate);
                } else {
                    setViewVisibility(headerStickView, View.GONE, animate);
                }
            }
        } else/*页面出现了大于等于2个的stick*/ {
            if (firstTop < 0) {
                int secondOffPos = secondTarget - firstVisiblePos;
                View secondStickItem = recyclerView.getChildAt(secondOffPos);
                View secondStickView = firstStickItem.findViewById(itemStickId);
                int secondTop = secondStickItem.getId() == itemStickId ?
                        secondStickItem.getTop() : secondStickItem.getTop() + secondStickView.getTop();
                int marginTop = 0;
                if (secondTop < headerStickView.getHeight()) {
                    marginTop = -(headerStickView.getHeight() - secondTop);
                }
                notifyStick(firstTarget);
                ViewUtils.setMargin(headerStickView, INVALID_MARGIN, marginTop, INVALID_MARGIN, INVALID_MARGIN);
                setViewVisibility(headerStickView, View.VISIBLE, animate);
            } else {
                if (isOverFirstTarget) {
                    notifyStick(pastRecentTarget);
                    int marginTop = 0;
                    if (firstTop < headerStickView.getHeight()) {
                        marginTop = -(headerStickView.getHeight() - firstTop);
                    }
                    ViewUtils.setMargin(headerStickView, INVALID_MARGIN, marginTop, INVALID_MARGIN, INVALID_MARGIN);
                    setViewVisibility(headerStickView, View.VISIBLE, animate);
                } else {
                    setViewVisibility(headerStickView, View.GONE, animate);
                }
            }
        }
    }

    private void notifyStick(int newPos) {
        if (mListener != null && oldStickPos != newPos) {
            oldStickPos = newPos;
            mListener.onStickChange(headerStickView, newPos);
        }
    }

    private static void setViewVisibility(final View view, final int visibility, boolean animate) {
        if (visibility == View.VISIBLE) {
            view.bringToFront();
        }
        if (animate) {
            long duration = 100;
            int alpha = visibility == View.VISIBLE ? 1 : 0;
            view.animate().cancel();
            view.animate().alpha(alpha).setDuration(duration).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(visibility);
                }
            });
        } else {
            int alpha = visibility == View.VISIBLE ? 1 : 0;
            view.setAlpha(alpha);
            view.setVisibility(visibility);
        }
    }

    /**
     * 需要stick显示的view的layout
     *
     * @param resId
     */
    public void setStickRes(int resId) {
        if (headerStickView != null) {
            removeView(headerStickView);
        }
        headerStickView = LayoutInflater.from(getContext()).inflate(resId, this, false);
        headerStickView.setVisibility(GONE);
        addView(headerStickView);
    }

    /**
     * 在item里面，需要stick的view的id
     *
     * @param id
     */
    public void setItemStickId(int id) {
        this.itemStickId = id;
    }

    /**
     * 需要stick的position
     *
     * @param positions
     */
    public void setStickPositions(List<Integer> positions) {
        this.stickPositions.clear();
        this.stickPositions.addAll(positions);
        Collections.sort(this.stickPositions);
        tryStickRecycler(false);
    }

    /**
     * 设置单个stick position
     * 会清空之前的stick
     *
     * @param position
     */
    public void setStickPosition(int position) {
        this.stickPositions.clear();
        this.stickPositions.add(position);
        tryStickRecycler(false);
    }

    public void addStickPosition(int position) {
        this.stickPositions.add(position);
        Collections.sort(this.stickPositions);
        tryStickRecycler(false);
    }

    public void clearStick(){
        this.stickPositions.clear();
        tryStickRecycler(false);
    }

    public void setListener(StickListener listener) {
        this.mListener = listener;
    }

    public interface StickListener {
        /**
         * @param stickView
         * @param newPos
         */
        void onStickChange(View stickView, int newPos);
    }
}
