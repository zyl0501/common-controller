package com.ray.common.cache.memory.impl;

import java.io.File;

public class FileLruMemoryCache extends LruMemoryCache<File> {
    /**
     * @param maxSize Maximum sum of the sizes of the Ts in this cache
     */
    public FileLruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected long sizeOf(String key, File value) {
        return value.length();
    }
}
