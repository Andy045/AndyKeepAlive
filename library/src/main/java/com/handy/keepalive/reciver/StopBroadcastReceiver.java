package com.handy.keepalive.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * 停止广播
 *
 * @author LiuJie https://github.com/Handy045
 * @description 让每个服务注册此广播，用于同时停止所有服务
 * @date Created in 2019/3/11 3:42 PM
 * @modified By liujie
 */
public class StopBroadcastReceiver extends BroadcastReceiver {

    private CallBack callBack;

    public StopBroadcastReceiver(@NonNull CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callBack.onReceive(context, intent);
    }

    public IntentFilter getIntentFilter(String identifier) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(identifier);
        return intentFilter;
    }

    public interface CallBack {
        void onReceive(Context context, Intent intent);
    }
}
