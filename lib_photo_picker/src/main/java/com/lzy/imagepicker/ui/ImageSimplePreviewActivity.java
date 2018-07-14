package com.lzy.imagepicker.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.adapter.ImageSimplePageAdapter;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 * 简单的图片预览，通过加载的时候图片的url进行图片的加载
 * ================================================
 */
public class ImageSimplePreviewActivity extends ImageBaseActivity implements View.OnClickListener {


    protected ArrayList<ImageItem> mImageItems;      //跳转进ImagePreviewFragment的图片文件夹
    protected int mCurrentPosition = 0;              //跳转进ImagePreviewFragment时的序号，第几个图片
    protected TextView mTitleCount;                  //显示当前图片的位置  例如  5/31
    protected View content;
    //protected View topBar;
    protected ViewPagerFixed mViewPager;
    protected ImageSimplePageAdapter mAdapter;
    public int mCurrentStart = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pk_activity_image_simple_preview);

        mCurrentPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        mCurrentStart = getIntent().getIntExtra(ImagePicker.WHO_START_WITH, -1);
        mImageItems = getIntent().getParcelableArrayListExtra(ImagePicker.EXTRA_IMAGE_ITEMS);

        //初始化控件
        content = findViewById(R.id.content);

        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
        //topBar = findViewById(R.id.top_bar);

        //隐藏删除按钮
//        ImageView mBtnDel = (ImageView) findViewById(R.id.btn_del);
//        mBtnDel.setVisibility(View.GONE);

        //topBar.findViewById(R.id.btn_back).setOnClickListener(this);

        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
        //params.topMargin = Utils.getStatusHeight(this);
//        topBar.setLayoutParams(params);
//        topBar.findViewById(R.id.btn_ok).setVisibility(View.GONE);
//        topBar.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        mTitleCount = (TextView) findViewById(R.id.tv_des);

        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);

        mAdapter = new ImageSimplePageAdapter(this, mImageItems);



//        mAdapter.setPhotoViewClickListener(new ImageSimplePageAdapter.PhotoViewClickListener() {
//            @Override
//            public void OnPhotoTapListener(View view, float v, float v1) {
//                onImageSingleTap();
//            }
//        });
        mViewPager.setAdapter(mAdapter);

        tintManager.setStatusBarTintResource(R.color.pk_transparent);//通知栏所需颜色
        //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
        if (Build.VERSION.SDK_INT >= 16)
            content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //初始化当前页面的状态
        mTitleCount.setText(getString(R.string.pk_preview_image_count, mCurrentPosition + 1, mImageItems.size()));

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTitleCount.setText(getString(R.string.pk_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });
        mViewPager.setCurrentItem(mCurrentPosition, false);
    }

    /**
     * 单击时，隐藏头和尾
     */
//    public void onImageSingleTap() {
//        if (topBar.getVisibility() == View.VISIBLE) {
//            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
//            topBar.setVisibility(View.GONE);
//            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16)
//                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        } else {
//            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
//            topBar.setVisibility(View.VISIBLE);
//            tintManager.setStatusBarTintResource(R.color.status_bar);//通知栏所需颜色
//            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16)
//                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {                  //按下回退键时候，进行回退操作
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}