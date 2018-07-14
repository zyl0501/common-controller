package com.saya.qrcode.qrcodescan.qrcode;

public interface ScanCallBack {
        /**
         * 二维码扫描结果回调
         *
         * @param content 扫描的内容
         * @return 是否消耗掉此次扫描，true表示消耗
         */
        boolean onResult(String content);
    }