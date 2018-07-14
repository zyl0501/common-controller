package com.tomtaw.image_loader;

import android.view.View;

/**
 * Created by yxx on 2015/8/24.
 *
 * @author ohun@live.cn
 */
public abstract class ImageLoadListener implements ImageLoadingListener {
    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    public void onLoadingFailed(String imageUri, View view, Throwable throwable) {

    }

    @Override
    public final void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        onLoadingFailed(imageUri, view, failReason.getCause());
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
