package com.tomtaw.imagepreview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tomtaw.image_loader.ImageLoadListener;
import com.tomtaw.photoview.PhotoView;
import com.tomtaw.photoview.PhotoViewAttacher;

import java.util.List;


public class ImagePagerAdapter extends PagerAdapter {
    private List<String> uris;
    private final Activity activity;
    private final ImageLoadListener l;
    ClickListener clickListener;

    ImagePagerAdapter(Activity activity, @NonNull List<String> uris, ImageLoadListener l) {
        this.activity = activity;
        this.uris = uris;
        this.l = l;
    }

    public void setUris(@NonNull List<String> uris) {
        this.uris = uris;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return uris.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object arg2) {
        //viewGroup.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view =  createImageView(position);
        container.addView(view);
        return view;
    }


    private ImageView createImageView(final int position) {
        final PhotoView photoView = new PhotoView(activity);
        ImagePreview.display(position, photoView, l);
        photoView.setOnViewTapListener(clickListener);
        photoView.setOnLongClickListener(clickListener);
        photoView.setOnCreateContextMenuListener(activity);
        return photoView;
    }

    interface ClickListener extends View.OnLongClickListener, PhotoViewAttacher.OnViewTapListener {
    }
}