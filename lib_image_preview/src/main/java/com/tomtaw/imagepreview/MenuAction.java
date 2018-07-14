package com.tomtaw.imagepreview;

import android.content.Context;

/**
 * Created by yxx on 2015/12/3.
 *
 * @author ohun@live.cn
 */
public interface MenuAction {

    void onAction(Context context, IPreviewView previewView, int index);
}
