package com.tomtaw.image_loader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.tomtaw.image_loader.ConfigOptions;

/**
 * glide 3.5 版本之后，在manifest中配置meta-data
 */
public class CustomGlideModule implements GlideModule{
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, ConfigOptions.MAX_DISK_CACHE_SIZE));
    }

    @Override public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
