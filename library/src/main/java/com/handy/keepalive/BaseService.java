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

    /**
     * 销毁广播的广播标识符
     */
    public String stopBroadcastreceiverIdentifier;

    private Context context;
    private String className;
    private boolean isFinishFromReceiver;
    private StopBroadcastReceiver stopBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onCreate()");
        }

        context = this;
        className = this.getClass().getSimpleName();

        // TODO: 2019/3/14 创建前台通知
        startForeground(Config.NOTIFICATION_INDEX, getNotification());

        // TODO: 2019/3/14 注册销毁广播
        stopBroadcastreceiverIdentifier = Config.STOP_BROADCASTRECEIVER_IDENTIFIER;
        stopBroadcastReceiver = new StopBroadcastReceiver(new StopBroadcastReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Config.isShowLog) {
                    Log.d(Config.LOG_TAG, className + " => StopBroadcastReceiver.CallBack()");
                }

                isFinishFromReceiver = true;

                if (stopBroadcastReceiver != null) {
                    unregisterReceiver(stopBroadcastReceiver);
                    stopBroadcastReceiver = null;
                }

                stopSelf();
            }
        });
        registerReceiver(stopBroadcastReceiver, stopBroadcastReceiver.getIntentFilter(stopBroadcastreceiverIdentifier));


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onStartCommand()");
        }

        // TODO: 2019/3/14 启动守护服务

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onBind()");
        }
        return createIBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onUnbind()");
        }
        return super.onUnbind(intent);
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

        // TODO: 2019/3/14 重启自身和守护服务
        if (!isFinishFromReceiver) {
            ServiceUtil.startService(context, this.getClass());
        }
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
