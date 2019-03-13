package com.handy.keepalive.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 屏幕监听工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 10:52 AM
 * @modified By liujie
 */
public class ScreenReceiverUtil {

    private Context context;
    private SreenStateListener sreenStateListener;
    private SreenBroadcastReceiver sreenBroadcastReceiver;

    public ScreenReceiverUtil(Context context) {
        this.context = context;
    }

    public void registerScreenReceiverListener(SreenStateListener mStateReceiverListener) {
        // 动态启动广播接收器
        this.sreenStateListener = mStateReceiverListener;
        this.sreenBroadcastReceiver = new SreenBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);

        context.registerReceiver(sreenBroadcastReceiver, intentFilter);
    }

    public void unregisterScreenReceiverListener() {
        context.unregisterReceiver(sreenBroadcastReceiver);
    }

    /**
     * 监听sreen状态对外回调接口
     */
    public interface SreenStateListener {
        /**
         * 开屏
         */
        void onSreenOn();

        /**
         * 锁屏
         */
        void onSreenOff();

        /**
         * 解锁
         */
        void onUserPresent();
    }

    public class SreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (sreenStateListener != null) {
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    // 开屏
                    sreenStateListener.onSreenOn();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    // 锁屏
                    sreenStateListener.onSreenOff();
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    // 解锁
                    sreenStateListener.onUserPresent();
                }
            }
        }
    }
}
