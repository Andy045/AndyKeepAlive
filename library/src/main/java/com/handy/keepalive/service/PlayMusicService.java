package com.handy.keepalive.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.handy.keepalive.R;
import com.handy.keepalive.config.Config;
import com.handy.keepalive.reciver.StopBroadcastReceiver;

/**
 * 后台播放无声音乐
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 10:53 AM
 * @modified By liujie
 */
public class PlayMusicService extends Service {

    /**
     * 销毁标识位。当启用销毁时重建功能后。通过结束广播控制此标识位，用于销毁时不再重建自身。
     */
    public boolean isFinishFromReceiver = false;

    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);


        // 注册结束广播
        StopBroadcastReceiver stopBroadcastReceiver = new StopBroadcastReceiver(new StopBroadcastReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isFinishFromReceiver = true;
                stopSelf();
            }
        });
        registerReceiver(stopBroadcastReceiver, stopBroadcastReceiver.getIntentFilter(Config.STOP_BROADCASTRECEIVER_IDENTIFIER));
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (mMediaPlayer != null) {
                    if (Config.isShowLog) {
                        Log.d(Config.LOG_TAG, "启动后台播放音乐");
                    }
                    mMediaPlayer.start();
                }
                return null;
            }
        };
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (Config.isShowLog) {
                Log.d(Config.LOG_TAG, "关闭后台播放音乐");
                mMediaPlayer.stop();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
