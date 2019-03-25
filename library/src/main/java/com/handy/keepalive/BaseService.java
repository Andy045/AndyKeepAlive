package com.handy.keepalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.handy.keepalive.config.Config;
import com.handy.keepalive.reciver.StopBroadcastReceiver;
import com.handy.keepalive.screen.ScreenManager;
import com.handy.keepalive.screen.ScreenReceiverUtil;
import com.handy.keepalive.service.HideNotificationService;
import com.handy.keepalive.service.PlayMusicService;
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
    private ScreenManager screenManager;
    private ScreenReceiverUtil screenReceiverUtil;

    public boolean isPlayMusic = false;
    public boolean isShowNotification = false;
    public boolean isHideNotification = false;
    public boolean isUseSinglePxActivity = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onCreate()");
        }

        context = this;

        // TODO: 2019/3/14 创建前台通知，并判断是否需要隐藏通知栏中的UI
        if (isShowNotification) {
            startForeground(Config.NOTIFICATION_INDEX, getNotification());
            if (isHideNotification) {
                //启动前台服务而不显示通知的漏洞已在 API Level 25 修复，大快人心！
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                    //利用漏洞在 API Level 17 及以下的 Android 系统中，启动前台服务而不显示通知
                    startForeground(Config.NOTIFICATION_INDEX, new Notification());
                    //利用漏洞在 API Level 18 及以上的 Android 系统中，启动前台服务而不显示通知
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ServiceUtil.startService(context, HideNotificationService.class);
                    }
                }
            }
        }

        // TODO: 2019/3/25 后台播放无声音乐
        if (isPlayMusic) {
            ServiceUtil.startService(context, PlayMusicService.class);
        }

        // TODO: 2019/3/25 监听屏幕，准备一个像素点的Activity
        if (isUseSinglePxActivity) {
            screenManager = ScreenManager.getInstance();
            screenReceiverUtil = new ScreenReceiverUtil(context);
            screenReceiverUtil.registerScreenReceiverListener(new ScreenReceiverUtil.SreenStateListener() {
                @Override
                public void onSreenOn() {

                }

                @Override
                public void onSreenOff() {
                    screenManager.startActivity(context);
                }

                @Override
                public void onUserPresent() {
                    screenManager.finishActivity();
                }
            });
        }

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

        if (!isFinishFromReceiver) {
            // TODO: 2019/3/14 正常结束服务
            onFinish("onDestroy");
        }
    }

    @Override
    public int onStart(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder createIBinder(Intent intent) {
        return null;
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

        // TODO: 2019/3/14 销毁前台通知
        if (isShowNotification) {
            NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(Config.NOTIFICATION_INDEX);
            if (isHideNotification) {
                //利用漏洞在 API Level 18 及以上的 Android 系统中，启动前台服务而不显示通知
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ServiceUtil.stopService(context, HideNotificationService.class);
                }
            }
        }

        // TODO: 2019/3/25 结束后台无声音乐
        if (isPlayMusic) {
            ServiceUtil.stopService(context, PlayMusicService.class);
        }

        // TODO: 2019/3/25 结束屏幕监听
        if (isUseSinglePxActivity) {
            screenReceiverUtil.unregisterScreenReceiverListener();
            screenManager = null;
            screenReceiverUtil = null;
        }

        // TODO: 2019/3/25 解除销毁广播
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
