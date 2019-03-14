package com.handy.keepalive;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.handy.keepalive.utils.ServiceUtil;

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
        isConnected = true;
        onConnected(name, service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
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

    public void connected(Context context, Class<? extends BaseService> cls) {
        if (!isConnected) {
            ServiceUtil.bindService(context, cls, this);
        }
    }

    public void disconnected(Context context, Class<? extends BaseService> cls) {
        if (isConnected) {
            ServiceUtil.unbindService(context, cls, this);
        }
    }
}
