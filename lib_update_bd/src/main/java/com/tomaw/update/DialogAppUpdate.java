package com.tomaw.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;

/**
 * Created by xiaodigua on 2016/9/7.
 */
public class DialogAppUpdate extends Dialog {

  TextView tv_info;
  TextView tv_update;
  TextView tv_cancel;
  ImageView iv_close;


  private Context context;

  public DialogAppUpdate(@NonNull Context context) {
    super(context);
  }

  public DialogAppUpdate(Context context, int themeResId) {
    super(context, themeResId);
  }

  public DialogAppUpdate(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }
  /**
   * dialog 服务选择
   *
   * @param context
   */
  public DialogAppUpdate(Context context, AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall, final OnClickListener listener) {
    super(context);
    this.context = context;
    Window window = getWindow();
    if(!(context instanceof Activity)) {
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      if(window != null) {
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);//需要添加的语句

      }else{
        Log.w("dialog","dialog window is null!");
      }
    }
    if(window != null){
      window.requestFeature(Window.FEATURE_NO_TITLE);
      window.setGravity(Gravity.CENTER_HORIZONTAL);
      window.setBackgroundDrawable(new BitmapDrawable());
    }
    View rootView = getLayoutInflater().inflate(
        R.layout.dialog_app_update, null);
    int screenWidth = Utils.getScreenHeightAndWidth(context).widthPixels;
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    super.setContentView(rootView, params);
    this.setCancelable(true);

    tv_info = (TextView) findViewById(R.id.tv_info);
    tv_update = (TextView) findViewById(R.id.tv_update);
    tv_cancel = (TextView) findViewById(R.id.tv_cancel);
    iv_close = (ImageView) findViewById(R.id.iv_close);

    if (null != appUpdateInfoForInstall) {
      //开发者可直接通过 AppUpdateInfoForInstall 里的内容进行新版本提示及安装
      tv_info.setText(Html.fromHtml(appUpdateInfoForInstall.getAppChangeLog()));
    } else if (null != appUpdateInfo) {
      //开发者可直接通过 AppUpdateInfo 里的内容进行新版本提示，并将 AppUpdateInfo 传入新版本下载接口进行新版本下载
      tv_info.setText(Html.fromHtml(appUpdateInfo.getAppChangeLog()));
    }

    tv_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(null != listener)listener.onPositive();
      }
    });

    tv_cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(null != listener)listener.onNegative();
      }
    });

    iv_close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
        if(null != listener)listener.onClose();
      }
    });
  }


  public interface OnClickListener {
    void onPositive();

    void onNegative();

    void onClose();
  }
}
