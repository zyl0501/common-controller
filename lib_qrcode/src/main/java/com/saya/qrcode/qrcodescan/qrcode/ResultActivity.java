/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.saya.qrcode.qrcodescan.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.saya.qrcode.R;
import com.saya.qrcode.qrcodescan.base.BaseSwipeBackActivity;
import com.saya.qrcode.qrcodescan.qrcode.decode.DecodeThread;
import com.saya.qrcode.qrcodescan.utils.CommonUtils;

/**
 * Author:  syy
 * Description:
 */
public class ResultActivity extends BaseSwipeBackActivity {

    public static final String BUNDLE_KEY_SCAN_RESULT = "BUNDLE_KEY_SCAN_RESULT";

    ImageView resultImage;

    TextView resultType;

    TextView resultContent;

    Button browseWebsiteBtn;

    private Bitmap mBitmap;
    private int mDecodeMode;
    private String mResultStr;
    private String mDecodeTime;

    @Override
    protected void getBundleExtras(Bundle extras) {
        if (extras != null) {
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                mBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            mResultStr = extras.getString(BUNDLE_KEY_SCAN_RESULT);
            mDecodeMode = extras.getInt(DecodeThread.DECODE_MODE);
            mDecodeTime = extras.getString(DecodeThread.DECODE_TIME);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_result;
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        setTitle("扫描结果");

        resultImage = (ImageView) findViewById(R.id.result_image);
         resultType = (TextView) findViewById(R.id.result_type);
        resultContent = (TextView) findViewById(R.id.result_content);
        browseWebsiteBtn = (Button)findViewById(R.id.btn_browsewebsite);

        StringBuilder sb = new StringBuilder();
//        sb.append("扫描方式:\t\t");
//        if (mDecodeMode == DecodeUtils.DECODE_MODE_ZBAR) {
//            sb.append("ZBar扫描");
//        } else if (mDecodeMode == DecodeUtils.DECODE_MODE_ZXING) {
//            sb.append("ZXing扫描");
//        }
//
//        if (!CommonUtils.isEmpty(mDecodeTime)) {
//            sb.append("\n\n扫描时间:\t\t");
//            sb.append(mDecodeTime);
//        }
        sb.append("扫描结果:");

        resultType.setText(sb.toString());
        resultContent.setText(mResultStr);

        if (null != mBitmap) {
            resultImage.setImageBitmap(mBitmap);
        }
    }

    /**
     * 浏览网页
     * @param view
     */
    public void onClickBrowseWebsite(View view){
       String result =  resultContent.getText().toString();
        if(!CommonUtils.isEmpty(result)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(result));
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }



    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }


    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }


    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }
}
