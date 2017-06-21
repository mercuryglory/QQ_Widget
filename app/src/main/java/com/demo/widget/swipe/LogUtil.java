package com.demo.widget.swipe;

import android.util.Log;


/**
 * 创建者:    wang.zhonghao
 * 创建时间:  2016/10/11
 * 描述:      log工具类 上线的时候将isDebug置为false，不会输出可能涉及保密的信息
 */
public class LogUtil {

    private static boolean isDebug = true;

    public static void logD(String message) {
        if (isDebug) {
            Log.d("Mercury", message);
        }
    }

    public static void logD(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    public static void logE(String message) {
        if (isDebug) {
            Log.e("Mercury", message);
        }
    }

    public static void logE(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }
    public static void logI(String message) {
        if (isDebug) {
            Log.i("Mercury", message);
        }
    }

    public static void logI(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }
    public static void logW(String message) {
        if (isDebug) {
            Log.w("Mercury", message);
        }
    }

    public static void logW(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

}
