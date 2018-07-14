package com.tomtaw.imagepreview;

import java.util.List;

/**
 * @author zyl
 * @date Created on 2018/1/3
 */
public interface IPreviewView {
    /**
     * 移除 uri
     * @param uri uri
     */
    void removeUri(String uri);

    /**
     * 移除 uri
     * @param index uri 的 index
     */
    void removeUri(int index);

    /**
     * 增加 uri
     * @param uri uri
     */
    void addUri(String uri);

    /**
     * 增加 uri
     * @param uri uri
     * @param index 增加的index
     */
    void addUri(String uri, int index);

    /**
     * 当前的所有 uri
     * @return 所有的uri
     */
    List<String> uris();

    /**
     * 刷新页面
     */
    void refresh();
}
