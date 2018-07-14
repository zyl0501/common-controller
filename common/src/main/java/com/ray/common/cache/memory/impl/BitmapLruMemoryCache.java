package com.ray.common.cache.memory.impl;

import android.graphics.Bitmap;

public final class BitmapLruMemoryCache extends LruMemoryCache<Bitmap> {
    /**
     * @param maxSize Maximum sum of the sizes of the Ts in this cache
     */
    public BitmapLruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected long sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }
}
