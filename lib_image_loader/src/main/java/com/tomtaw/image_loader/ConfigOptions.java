package com.tomtaw.image_loader;

import android.content.Context;

/**
 * Created by yxx on 2015/8/17.
 *
 * @author ohun@live.cn
 */
public final class ConfigOptions {
    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * 1024 * 1224;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * 1024 * 1224;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * 1024 * 1224;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * 1024 * 1224;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * 1024 * 1224;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1224;//默认图磁盘缓存的最大值
    public static final int MAX_MEMORY_CACHE_SIZE = (int) Runtime.getRuntime().maxMemory() / 12;//使用的缓存数量

    int maxMemoryCacheSize = MAX_MEMORY_CACHE_SIZE;
    int maxDiskCacheSize = MAX_DISK_CACHE_SIZE;
    int maxDiskFileCount = 3000;
    Context ctx;

    public ConfigOptions() {
        //this.maxMemoryCacheSize = OS.getHeapSize(ctx) * 1024*1224 / 12;
    }

    public void init(Context context){
        this.ctx = context;
    }
}
