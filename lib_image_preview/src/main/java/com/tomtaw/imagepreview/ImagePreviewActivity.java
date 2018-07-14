package com.tomtaw.imagepreview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tomtaw.image_loader.ImageLoadListener;
import com.tomtaw.photoview.PhotoView;
import com.tomtaw.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by yxx on 2015/12/3.
 *
 * @author ohun@live.cn
 */
public final class ImagePreviewActivity extends Activity
        implements ViewPager.OnPageChangeListener, IPreviewView, ImagePagerAdapter.ClickListener {

    public static ImagePreview builder;
    PhotoView imageView;
    View progress;
    ViewPager viewPager;
    TextView pageIndicator;

    ImageLoadListener imageLoadListener;
    ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutID());
        initViewsAndEvents(savedInstanceState);
        transFitWindow();
    }

    protected void transFitWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public void initViewsAndEvents(Bundle savedInstanceState) {
        imageView = (PhotoView) findViewById(R.id.imageView);
        progress = findViewById(android.R.id.progress);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageIndicator = (TextView) findViewById(R.id.pageIndicator);

        imageLoadListener = new ImageLoadCompleteListener(progress);
        viewPager.addOnPageChangeListener(this);
        refresh();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int i) {
        pageIndicator.setText(String.format(Locale.getDefault(), "%d/%d", i + 1, builder.uris.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void removeUri(String uri) {
        if (builder.uris != null) {
            builder.uris.remove(uri);
            if (builder.uris.size() <= 0) {
                finish();
            } else {
                refresh();
            }
        }
    }

    @Override
    public void removeUri(int index) {
        if (builder.uris != null && index < builder.uris.size()) {
            builder.uris.remove(index);
            if (builder.uris.size() <= 0) {
                finish();
            } else {
                refresh();
            }
        }
    }

    @Override
    public void addUri(String uri) {
        if (builder.uris == null) {
            builder.uris = new ArrayList<>(1);
        }
        builder.uris.add(uri);
        refresh();
    }

    @Override
    public void addUri(String uri, int index) {
        if (builder.uris == null) {
            builder.uris = new ArrayList<>(1);
        }
        builder.uris.add(index, uri);
        refresh();
    }

    @Override
    public List<String> uris() {
        return builder.uris;
    }

    @Override
    public void refresh() {
        if (builder.uris.size() == 1) {
            viewPager.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            ImagePreview.display(0, imageView, imageLoadListener);
            imageView.setOnViewTapListener(this);
            if (builder.menuItems.size() > 0) {
                imageView.setOnLongClickListener(this);
                imageView.setOnCreateContextMenuListener(this);
            }
            pageIndicator.setText(String.format(Locale.getDefault(), "%d/%d", 1, 1));
        } else {
            imageView.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new ImagePagerAdapter(this, builder.uris, imageLoadListener);
                adapter.clickListener = this;
            } else {
                adapter.setUris(builder.uris);
            }
            viewPager.setAdapter(adapter);
            if (builder.firstIndex > 0) {
                viewPager.setCurrentItem(builder.firstIndex);
            }
            pageIndicator.setText(String.format(Locale.getDefault(), "%d/%d", viewPager.getCurrentItem() + 1, builder.uris.size()));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        v.showContextMenu();
        return true;
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        finish();
    }

    public static class ImageLoadCompleteListener extends ImageLoadListener {
        View progress;

        public ImageLoadCompleteListener(View progress) {
            this.progress = progress;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            if (imageUri.startsWith("http:")) {
                progress.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, Throwable throwable) {
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        for (String name : builder.menuItems.keySet()) {
            menu.add(0, 0, 0, name);
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        MenuAction action = builder.menuItems.get(item.getTitle().toString());
        if (action != null) {
            boolean isSingle = builder.uris.size() == 1;
            action.onAction(this, this, isSingle ? 0 : viewPager.getCurrentItem());
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        builder = null;
    }

    protected int getContentViewLayoutID() {
        return R.layout.ip_activity_img_preview;
    }

    @Override
    public void finish() {
        super.finish();
    }
}
