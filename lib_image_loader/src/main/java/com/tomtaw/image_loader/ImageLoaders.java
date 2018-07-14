package com.tomtaw.image_loader;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.tomtaw.image_loader.glide.GlideRequestManager;
import com.tomtaw.image_loader.inner.ImageDisplayLoader;
import com.tomtaw.image_loader.inner.RequestManager;


/**
 * Created by xiaoxu.yxx on 2015/7/29.
 */
public final class ImageLoaders {
    static RequestManager DEFAULT;
    static GlideRequestManager glideImpl;
    static ConfigOptions CONFIG;

    public static RequestManager getDefault() {
        if (DEFAULT == null) {
            CONFIG = new ConfigOptions();
            DEFAULT = newGlide(CONFIG);
        }
        return DEFAULT;
    }

    public void init(Context context){
        CONFIG.init(context);
    }

    public static GlideRequestManager newGlide(ConfigOptions options) {
        return new GlideRequestManager(options);
    }

    public static GlideRequestManager getGlide() {
        if (glideImpl == null) {
            glideImpl = newGlide(CONFIG);
        }
        return glideImpl;
    }

    public static void clearCache() {
        if (DEFAULT != null) {
        }
    }

    public static ImageDisplayLoader with(Fragment fragment) {
        return getDefault().with(fragment);
    }

    public static ImageDisplayLoader with(android.app.Fragment fragment) {
        return getDefault().with(fragment);
    }

    public static ImageDisplayLoader with(Activity activity) {
        return getDefault().with(activity);
    }

    public static ImageDisplayLoader with(Context context) {
        return getDefault().with(context);
    }
}

