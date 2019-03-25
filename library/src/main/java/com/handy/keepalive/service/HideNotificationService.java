package com.handy.keepalive.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.handy.keepalive.R;
import com.handy.keepalive.config.Config;

/**
 * 前台通知栏隐藏服务
 *
 * @author LiuJie https://github.com/Handy045
 * @description 用于隐藏业务服务创建的通知栏
 * @date Created in 2019/3/8 11:17 AM
 * @modified By liujie
 */
public class HideNotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onCreate()");
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onStartCommand()");
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // 创建新的通知，通过标识符新增或替换已有的通知。
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(Config.NOTIFICATION_INDEX, builder.build());
            // 在异步任务中移除业务服务的前台通知。
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    // 1秒延迟
                    SystemClock.sleep(1000);
                    // 取消当前服务的前台
                    stopForeground(true);
                    // 移除业务服务的通知
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(Config.NOTIFICATION_INDEX);
                    // 结束当前服务
                    stopSelf();
                    return null;
                }
            }.execute();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onDestroy()");
        }
    }
}
