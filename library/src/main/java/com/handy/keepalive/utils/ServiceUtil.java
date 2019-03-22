package com.handy.keepalive.utils;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.handy.keepalive.BaseServiceConnection;

import java.util.Objects;


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
            context.bindService(new Intent(context, serviceClass), connection, Context.BIND_AUTO_CREATE);
        }
    }

    public static void unbindService(@NonNull final Context context, @NonNull final Class<? extends Service> serviceClass, @NonNull BaseServiceConnection connection) {
        if (connection.isConnected) {
            context.unbindService(connection);
            connection.onServiceDisconnected(new ComponentName(Objects.requireNonNull(serviceClass.getPackage()).getName(), serviceClass.getName()));
        }
    }

    public static void startService(Context context, Class<? extends Service> serviceClass) {
        try {
            context.startService(new Intent(context, serviceClass));
        } catch (Exception ignored) {

        }
    }

    public static void stopService(Context context, Class<? extends Service> serviceClass) {
        try {
            context.stopService(new Intent(context, serviceClass));
        } catch (Exception ignored) {

        }
    }

    public static void startAndBindService(@NonNull final Context context, @NonNull final Class<? extends Service> serviceClass, @NonNull BaseServiceConnection connection) {
        if (!connection.isConnected) {
            startService(context, serviceClass);
            bindService(context, serviceClass, connection);
        }
    }

    public static void stopAndUnbindService(@NonNull final Context context, @NonNull final Class<? extends Service> serviceClass, @NonNull BaseServiceConnection connection) {
        if (connection.isConnected) {
            unbindService(context, serviceClass, connection);
            stopService(context, serviceClass);
        }
    }
}
