package com.example.rightindexbar.IndexBar.bean;

/**
 * 介绍：某个需要被处理的字段的接口
 */

public interface IIndexTargetInterface {
    String getTarget();//需要被转化成拼音，并取出首字母 索引排序的 字段

    boolean isNeedToPinyin();//是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
}
