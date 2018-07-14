package com.tomtaw.lib_badge;

import java.util.Collection;

/**
 * Created by zyl on 2017/6/1.
 */

public interface IBadgeGraph {
    int root = 0;
    int invalid = -1;

    Collection<Badge> badges();

    interface Badge{
        int self();
        int parent();
    }
}
