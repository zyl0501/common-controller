package com.tomtaw.update;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.ray.common.dialogs.Builders;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zyl on 2017/7/13.
 */
public class PgyUpdateService extends Service {
    private static WeakReference<Activity> activity;

    public static void register(Activity activity) {
        Intent intent = new Intent(activity, PgyUpdateService.class);
        activity.startService(intent);
        PgyUpdateService.activity = new WeakReference<>(activity);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Activity target = activity != null ? activity.get() : null;
        if (target != null && !target.isFinishing()) {
            PgyUpdateManager.unregister();
            PgyUpdateManager.register(target, new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        final AppBean appBean = getAppBeanFromString(result);
                        Builders.A(PgyUpdateService.this)
                            .setTitle("更新")
                            .setMessage(appBean.getVersionName() + "\n" + appBean.getReleaseNote())
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    final Activity target = activity != null ? activity.get() : null;
                                    if (target != null && !target.isFinishing()) {
                                        AndPermission.with(target)
                                            .requestCode(200)
                                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            .callback(new PermissionListener() {
                                                @Override
                                                public void onSucceed(int requestCode, List<String> grantPermissions) {
                                                    startDownloadTask(target, appBean.getDownloadURL());
                                                }

                                                @Override
                                                public void onFailed(int requestCode, List<String> deniedPermissions) {
                                                }
                                            })
                                            .rationale(new RationaleListener() {
                                                @Override
                                                public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                                    AndPermission.rationaleDialog(target, rationale)
                                                        .show();
                                                }
                                            })
                                            .start();
                                    }
                                }
                            })
                            .show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Log.d("update", "no update available");
                    }
                });
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
