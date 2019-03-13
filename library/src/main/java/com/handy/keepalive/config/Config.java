package com.handy.keepalive.config;

import com.handy.keepalive.BuildConfig;

/**
 * 配置类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/3/8 11:06 AM
 * @modified By liujie
 */
public class Config {
    /**
     * 日志标签
     */
    public static final String LOG_TAG = "HandyKeepAlive";
    /**
     * 业务服务创建的通知标识符
     */
    public static final int NOTIFICATION_LEVEL = 100;
    /**
     * 统一停止广播标识符
     */
    public static final String STOP_BROADCASTRECEIVER_IDENTIFIER = "STOP_BROADCASTRECEIVER_IDENTIFIER";
    /**
     * 是否显示Log日志，默认只在Debug模式时显示
     */
    public static boolean isShowLog = BuildConfig.DEBUG;
}
