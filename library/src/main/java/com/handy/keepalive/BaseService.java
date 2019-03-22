package com.handy.keepalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.handy.keepalive.config.Config;
import com.handy.keepalive.reciver.StopBroadcastReceiver;
import com.handy.keepalive.utils.ServiceUtil;

/**
 * 业务服务基础类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/13 2:37 PM
 * @modified By liujie
 */
public abstract class BaseService extends Service implements BaseServiceApi {

    private Context context;
    private boolean isFinishFromReceiver;
    private StopBroadcastReceiver stopAllBroadcastReceiver;
    private StopBroadcastReceiver stopSelfBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onCreate()");
        }

        context = this;

        // TODO: 2019/3/14 创建前台通知
        startForeground(Config.NOTIFICATION_INDEX, getNotification());

        // TODO: 2019/3/14 注册销毁广播
        stopSelfBroadcastReceiver = new StopBroadcastReceiver(new StopBroadcastReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isFinishFromReceiver = true;
                onFinish("stopSelfBroadcastReceiver");
            }
        });
        stopAllBroadcastReceiver = new StopBroadcastReceiver(new StopBroadcastReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isFinishFromReceiver = true;
                onFinish("stopAllBroadcastReceiver");
            }
        });
        registerReceiver(stopSelfBroadcastReceiver, stopSelfBroadcastReceiver.getIntentFilter(this.getClass().getName()));
        registerReceiver(stopAllBroadcastReceiver, stopAllBroadcastReceiver.getIntentFilter(Config.STOP_BROADCASTRECEIVER_IDENTIFIER));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onStartCommand()");
        }

        // TODO: 2019/3/14 启动守护服务

        return onStart(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onBind()");
        }
        return createIBinder(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onUnbind()");
        }
        return cancelIBinder(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onTaskRemoved()");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onDestroy()");
        }

        // TODO: 2019/3/14 销毁前台通知
        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mManager.cancel(Config.NOTIFICATION_INDEX);

        if (!isFinishFromReceiver) {
            if (isKeepAlive()) {
                // TODO: 2019/3/14 重启自身和守护服务
                ServiceUtil.startService(context, this.getClass());
            } else {
                // TODO: 2019/3/14 正常结束服务
                onFinish("onDestroy");
            }
        }
    }

    @Override
    public int onStart(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean cancelIBinder(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onFinish(String logTag) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => " + logTag + ".onFinish()");
        }

        if (stopSelfBroadcastReceiver != null) {
            unregisterReceiver(stopSelfBroadcastReceiver);
            stopSelfBroadcastReceiver = null;
        }
        if (stopAllBroadcastReceiver != null) {
            unregisterReceiver(stopAllBroadcastReceiver);
            stopAllBroadcastReceiver = null;
        }

        stopSelf();
    }

    @Override
    public Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getResources().getString(R.string.notification_title));
        builder.setContentText(getResources().getString(R.string.notification_content));
        return builder.build();
    }
}
