package com.ray.common.cache.memory.impl;

import com.ray.common.cache.memory.MemoryCache;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A cache that holds strong references to a limited number of Ts. Each time a T is accessed, it is moved to
 * the head of a queue. When a T is added to a full cache, the T at the end of that queue is evicted and may
 * become eligible for garbage collection.<br>
 * <br>
 * <b>NOTE:</b> This cache uses only strong references for stored Ts.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.1
 */
public abstract class LruMemoryCache<T> implements MemoryCache<T> {

    private final LinkedHashMap<String, T> map;

    private final int maxSize;
    /**
     * Size of this cache in bytes
     */
    private int size;

    /**
     * @param maxSize Maximum sum of the sizes of the Ts in this cache
     */
    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    /**
     * Returns the T for {@code key} if it exists in the cache. If a T was returned, it is moved to the head
     * of the queue. This returns null if a T is not cached.
     */
    @Override
    public T get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            return map.get(key);
        }
    }

    /**
     * Caches {@code T} for {@code key}. The T is moved to the head of the queue.
     */
    @Override
    public final boolean put(String key, T value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            size += sizeOf(key, value);
            T previous = map.put(key, value);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }

        trimToSize(maxSize);
        return true;
    }

    /**
     * Remove the eldest entries until the total of remaining entries is at or below the requested size.
     *
     * @param maxSize the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
     */
    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            T value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, T> toEvict = map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= sizeOf(key, value);
            }
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     */
    @Override
    public  T remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            T previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
            return previous;
        }
    }

    @Override
    public Collection<String> keys() {
        synchronized (this) {
            return new HashSet<>(map.keySet());
        }
    }

    protected Collection<T> values() {
        synchronized (this) {
            return map.values();
        }
    }

    @Override
    public void clear() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * Returns the size {@code T} in bytes.
     * <p>
     * An entry's size must not change while it is in the cache.
     */
    protected abstract long sizeOf(String key, T value);

    @Override
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }
}