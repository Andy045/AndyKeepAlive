package com.handy.keepalive.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.handy.keepalive.BaseService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 计时器服务
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/14 9:40 AM
 * @modified By liujie
 */
public class TimeLockService extends BaseService {

    {
        isShowNotification = true;
        isPlayMusic = true;
        isUseSinglePxActivity = true;
    }

    private MyBinder myBinder;
    private CountDownTimer countDownTimer;

    @Override
    public IBinder createIBinder(Intent intent) {
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        countDownTimer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onTick(long millisUntilFinished) {
                if (myBinder.getResultListener() != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    myBinder.getResultListener().onResult(simpleDateFormat.format(new Date()));
                }
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
        return myBinder;
    }

    @Override
    public boolean cancelIBinder(Intent intent) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        return super.cancelIBinder(intent);
    }

    public interface ResultListener {
        void onResult(String string);
    }

    public class MyBinder extends Binder {
        private ResultListener resultListener;

        public ResultListener getResultListener() {
            return resultListener;
        }

        public void setResultListener(ResultListener resultListener) {
            this.resultListener = resultListener;
        }
    }
}
