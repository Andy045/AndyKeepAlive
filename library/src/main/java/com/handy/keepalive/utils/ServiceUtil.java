package com.handy.keepalive.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.handy.keepalive.BaseServiceConnection;
import com.handy.keepalive.config.Config;


/**
 * 服务工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/11 4:51 PM
 * @modified By liujie
 */
public class ServiceUtil {

    public static void bindService(@NonNull final Context context, @NonNull final Class<? extends Service> serviceClass, @NonNull BaseServiceConnection connection) {
        if (!connection.isConnected) {
            startService(context, serviceClass);
            context.bindService(new Intent(context, serviceClass), connection, Context.BIND_AUTO_CREATE);
        }
    }

    public static void unbindService(@NonNull final Context context, @NonNull final Class<? extends Service> serviceClass, @NonNull BaseServiceConnection connection) {
        if (connection.isConnected) {
            stopService(context, serviceClass);
            context.unbindService(connection);
        }
    }


    public static void startService(Context context, Class<? extends Service> i) {
        try {
            context.startService(new Intent(context, i));
        } catch (Exception ignored) {

        }
    }

    public static void stopService(Context context, Class<? extends Service> i) {
        try {
            context.stopService(new Intent(context, i));
        } catch (Exception ignored) {

        }
    }

    /**
     * 当前哪个进程使用的时候 就用其上下文发送广播
     */
    public static void stopAllServices(Context context) {
        if (context != null) {
            context.sendBroadcast(new Intent(Config.STOP_BROADCASTRECEIVER_IDENTIFIER));
        }
    }
}