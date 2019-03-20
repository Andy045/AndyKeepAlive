package com.handy.keepalive;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;

/**
 * 业务服务基础类接口
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/13 2:45 PM
 * @modified By liujie
 */
public interface BaseServiceApi {
    Notification getNotification();

    IBinder createIBinder();

    void cancelIBinder();

    void onStart(Intent intent, int flags, int startId);

    void onFinish();
}
