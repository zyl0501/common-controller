package com.ray.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Chris on 2015/8/18.
 */
public final class CollectionVerify {

    /**
     * @param list
     * @return
     */
    public static boolean isEffective(Collection<?> list) {
        return list != null && list.size() > 0;
    }

    /**
     * @param list
     * @return
     */
    public static<T> boolean isEffective(T[] list) {
        return list != null && list.length > 0;
    }

    /**
     * @return
     */
    public static boolean isEffectiveMap(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

}
