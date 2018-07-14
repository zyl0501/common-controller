package com.tomtaw.imagepreview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.tomtaw.image_loader.DisplayOptions;
import com.tomtaw.image_loader.ImageLoadListener;
import com.tomtaw.image_loader.ImageLoaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2015/8/25.
 */
public final class ImagePreview {
    private static NetworkUrlCallBack networkUrlCallBack;
    /**
     * 待预览的图片地址，支持本地和网络图片
     */
    List<String> uris;

    String imageType;

    List<String> thumbnails;

    /**
     * 默认显示第几张图片
     */
    int firstIndex;

    /**
     * 是否显示保存到本地菜单
     */
    boolean showSaveToAlbum = false;

    Map<String, MenuAction> menuItems = new LinkedHashMap<>(4);

    public ImagePreview(String[] uris) {
        if (uris != null) {
            this.uris = new ArrayList<>();
            Collections.addAll(this.uris, uris);
        }
    }

    public static ImagePreview build(String... uris) {
        return new ImagePreview(uris);
    }

    public ImagePreview imageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public ImagePreview thumbnails(String... thumbnails) {
        if (thumbnails != null) {
            this.thumbnails = new ArrayList<>();
            Collections.addAll(this.thumbnails, thumbnails);
        }
        return this;
    }

    public ImagePreview firstUriIndex(int firstIndex) {
        this.firstIndex = firstIndex;
        return this;
    }

    /**
     * 添加自定义菜单项
     *
     * @param name   菜单项名称
     * @param action 点击动作
     * @return
     */
    public ImagePreview addMenuItem(String name, MenuAction action) {
        menuItems.put(name, action);
        return this;
    }

    public void show(Activity activity) {
        if (uris == null || uris.size() == 0) {
            return;
        }
        if (firstIndex >= uris.size() || firstIndex < 0) {
            firstIndex = 0;
        }
        if (showSaveToAlbum) {
            //TODO 保存到本地
//            menuItems.put("保存到相册", new SaveToAlbumMenuAction());
        }
        ImagePreviewActivity.builder = this;
        activity.startActivity(new Intent(activity, ImagePreviewActivity.class));
    }

    static String getUrl(String uri) {
        boolean networkImg = uri.startsWith("http://");
        if (!networkImg && !uri.startsWith("/")) {
            if (networkUrlCallBack != null) {
                uri = networkUrlCallBack.getImgUrl(uri);
            }
        }
        return uri;
    }

    static void display(int index, final ImageView imageView, final ImageLoadListener progress) {
        if (ImagePreviewActivity.builder == null) {return;}
        ImagePreview builder = ImagePreviewActivity.builder;
        //1.先显示缩略图
        if (builder.thumbnails != null && builder.thumbnails.size() == builder.uris.size()) {
            String thumbnail = builder.thumbnails.get(index);
            if (TextUtils.isEmpty(thumbnail)) {
                display(builder.uris.get(index), imageView, progress, 3);
            } else {
                final String uri = builder.uris.get(index);
                display(thumbnail, imageView, new ImageLoadListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        display(uri, imageView, progress, 2);
                    }
                }, 1);
            }
        } else {
            display(builder.uris.get(index), imageView, progress, 3);
        }
    }

    static void display(String uri, ImageView imageView, ImageLoadListener l, int showType) {
        ImagePreview builder = ImagePreviewActivity.builder;
        //2.下载并显示大图
        boolean networkImg = uri.startsWith("http://");
        if (!networkImg && !uri.startsWith("/")) {
            if (networkUrlCallBack != null) {
                uri = networkUrlCallBack.getImgUrl(uri);
            }
            networkImg = true;
        }
        DisplayOptions ops = DisplayOptions.getDefaultOps();
        if (showType == 1) {
            ops.setCacheInMemory(true);
            ops.setCacheOnDisk(true);
            if (networkImg) ops.setLoadingResId(R.mipmap.ip_ic_default_img);
            ops.setDefaultResId(R.mipmap.ip_ic_default_img);
            ops.setErrorResId(R.mipmap.ip_ic_default_img);
        } else if (showType == 2) {
            ops.setCacheInMemory(false);
            ops.setCacheOnDisk(networkImg);
        } else {
            ops.setCacheInMemory(false);
            ops.setCacheOnDisk(networkImg);
            if (networkImg) ops.setLoadingResId(R.mipmap.ip_ic_default_img);
            ops.setDefaultResId(R.mipmap.ip_ic_default_img);
            ops.setErrorResId(R.mipmap.ip_ic_default_img);
        }
        ImageLoaders.with(imageView.getContext()).display(ops, imageView, networkImg ? uri : "file://" + uri, l);
    }

    public static void setNetworkUrlCallBack(NetworkUrlCallBack callBack) {
        networkUrlCallBack = callBack;
    }
}
