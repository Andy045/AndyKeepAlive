package com.handy.keepalive;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.handy.keepalive.config.Config;

import java.util.Objects;

/**
 * 服务绑定基础类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/11 3:25 PM
 * @modified By liujie
 */
public abstract class BaseServiceConnection implements ServiceConnection {

    public boolean isConnected = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, Objects.requireNonNull(this.getClass().getSuperclass()).getSimpleName() + ".onServiceConnected()");
        }
        isConnected = true;
        onConnected(name, service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, Objects.requireNonNull(this.getClass().getSuperclass()).getSimpleName() + ".onServiceDisconnected()");
        }
        isConnected = false;
        onDisconnected(name);
    }

    @Override
    public void onBindingDied(ComponentName name) {
        onServiceDisconnected(name);
    }

    @Override
    public void onNullBinding(ComponentName name) {
    }

    public void onConnected(ComponentName name, IBinder service) {

    }

    public void onDisconnected(ComponentName name) {

    }
}
