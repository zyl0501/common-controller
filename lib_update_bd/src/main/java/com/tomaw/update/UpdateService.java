package com.tomaw.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;

/**
 * Created by zyl on 2017/6/26.
 */

public class UpdateService extends Service {

    private DialogAppUpdate mDialogAppUpdate;
    private final int NOTIFY_ID = 0;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private String packageName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        packageName = getPackageName();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean ignoreUpdate = PreferenceManager.getDefaultSharedPreferences(UpdateService.this)
            .getBoolean("AppUpdateIgnore_" + Utils.getAppVersion(UpdateService.this), false);
        if(ignoreUpdate){
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        BDAutoUpdateSDK.cpUpdateCheck(this, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
                onUpdate(appUpdateInfo, appUpdateInfoForInstall);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void onUpdate(final AppUpdateInfo appUpdateInfo, final AppUpdateInfoForInstall appUpdateInfoForInstall) {
        if (null == appUpdateInfo && null == appUpdateInfoForInstall) return;
        mDialogAppUpdate = new DialogAppUpdate(this, appUpdateInfo, appUpdateInfoForInstall, new DialogAppUpdate.OnClickListener() {
            @Override
            public void onPositive() {
                if (null != appUpdateInfoForInstall) {
                    //开发者可直接通过 AppUpdateInfoForInstall 里的内容进行新版本提示及安装
                    BDAutoUpdateSDK.cpUpdateInstall(UpdateService.this, appUpdateInfoForInstall.getInstallPath());
                } else if (null != appUpdateInfo) {
                    if (Utils.isWifi(UpdateService.this)) {
                        downloadApk(appUpdateInfo);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateService.this);
                        builder.setMessage("当前非Wifi环境，是否继续下载？");
                        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadApk(appUpdateInfo);
                            }
                        });
                        builder.setNegativeButton("等待wifi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BDAutoUpdateSDK.silenceUpdateAction(UpdateService.this);
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onNegative() {
                if (null != mDialogAppUpdate) mDialogAppUpdate.dismiss();
                PreferenceManager.getDefaultSharedPreferences(UpdateService.this)
                    .edit()
                    .putBoolean("AppUpdateIgnore_" + Utils.getAppVersion(UpdateService.this), true)
                    .apply();
                UpdateService.this.stopSelf();
            }

            @Override
            public void onClose() {
                UpdateService.this.stopSelf();
            }
        });
        mDialogAppUpdate.show();
    }

    public void downloadApk(AppUpdateInfo appUpdateInfo) {
        //开发者可直接通过 AppUpdateInfo 里的内容进行新版本提示，并将 AppUpdateInfo 传入新版本下载接口进行新版本下载
        BDAutoUpdateSDK.cpUpdateDownload(UpdateService.this, appUpdateInfo, new CPUpdateDownloadCallback() {
            @Override
            public void onStart() {
                setUpNotification();
            }

            @Override
            public void onPercent(int progress, long current, long total) {
                RemoteViews contentview = mNotification.contentView;
                contentview.setTextViewText(R.id.tv_progress, progress + "%");
                contentview.setTextViewText(R.id.tv_title, Utils.getAppName(UpdateService.this) +" 正在更新...");
                contentview.setProgressBar(R.id.progressbar, 100, progress, false);
                mNotificationManager.notify(NOTIFY_ID, mNotification);
            }

            @Override
            public void onDownloadComplete(String installPath) {
                mNotificationManager.cancel(NOTIFY_ID);
                BDAutoUpdateSDK.cpUpdateInstall(UpdateService.this, installPath);
            }

            @Override
            public void onFail(Throwable throwable, String s) {
                RemoteViews contentview = mNotification.contentView;
                contentview.setTextViewText(R.id.tv_progress, "下载失败");
                contentview.setProgressBar(R.id.progressbar, 100, 0, false);
                mNotificationManager.notify(NOTIFY_ID, mNotification);
            }

            @Override
            public void onStop() {

            }
        });
    }

    /**
     * 创建通知
     */
    @SuppressWarnings("deprecation")
    private void setUpNotification() {
        RemoteViews contentView = new RemoteViews(packageName, R.layout.notification_update);
        contentView.setImageViewResource(R.id.notification_ic,Utils.getAppIconRes(this));
        mNotification = new NotificationCompat.Builder(this)
            .setSmallIcon(Utils.getAppIconRes(this))
            .setTicker(Utils.getAppName(this) + " 正在更新...")
            .setCustomContentView(contentView)
            .setWhen(System.currentTimeMillis())
            .setOngoing(false)
            .build();
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }
}
