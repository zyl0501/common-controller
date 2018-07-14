package com.example.rightindexbar.IndexBar.bean;


import com.example.rightindexbar.suspension.ITitleCategoryInterface;

/**
 * 介绍：索引类的标志位的实体基类
 */

public abstract class BaseIndexTagBean implements ITitleCategoryInterface {
    private String baseIndexTag;//所属的分类（城市的汉语拼音首字母）

    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    public void setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
    }

    @Override
    public String getTitleCategory() {
        return baseIndexTag;
    }

}
