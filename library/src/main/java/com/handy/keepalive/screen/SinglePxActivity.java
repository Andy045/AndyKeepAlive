package com.handy.keepalive.screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.handy.keepalive.config.Config;

/**
 * 1个像素点的Activity
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 10:52 AM
 * @modified By liujie
 */
public class SinglePxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Config.isShowLog) {
            Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onCreate()");
        }
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = 1;
        layoutParams.height = 1;
        window.setAttributes(layoutParams);
        ScreenManager.getInstance().setWeakReference(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            if (Config.isShowLog) {
                Log.d(Config.LOG_TAG, this.getClass().getSimpleName() + " => onFinish()");
            }
        }
    }
}
