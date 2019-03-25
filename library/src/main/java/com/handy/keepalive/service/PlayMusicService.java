package com.handy.keepalive.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.handy.keepalive.BaseService;
import com.handy.keepalive.R;
import com.handy.keepalive.config.Config;

/**
 * 后台播放无声音乐
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 10:53 AM
 * @modified By liujie
 */
public class PlayMusicService extends BaseService {

    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStart(Intent intent, int flags, int startId) {
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
        }.execute();
        return super.onStart(intent, flags, startId);
    }

    @Override
    public void onFinish(String logTag) {
        super.onFinish(logTag);
        if (mMediaPlayer != null) {
            if (Config.isShowLog) {
                Log.d(Config.LOG_TAG, "关闭后台播放音乐");
            }
            mMediaPlayer.stop();
        }
    }
}
