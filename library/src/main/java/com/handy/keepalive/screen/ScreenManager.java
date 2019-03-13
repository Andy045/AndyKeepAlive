package com.handy.keepalive.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.handy.keepalive.config.Config;

import java.lang.ref.WeakReference;

/**
 * 窗口界面管理器
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 10:50 AM
 * @modified By liujie
 */
public class ScreenManager {

    private static ScreenManager screenManager;

    private WeakReference<SinglePxActivity> weakReference;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (screenManager == null) {
            screenManager = new ScreenManager();
        }
        return screenManager;
    }

    void setWeakReference(SinglePxActivity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    /**
     * 启动一个像素点Activity
     */
    public void startActivity(Context context) {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, "SinglePxActivity is Started");
        }
        Intent intent = new Intent();
        intent.setClass(context, SinglePxActivity.class);
        context.startActivity(intent);
    }

    /**
     * 结束一个像素点Activity
     */
    public void finishActivity() {
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, "SinglePxActivity is Finished");
        }
        if (this.weakReference != null) {
            Activity activity = this.weakReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
