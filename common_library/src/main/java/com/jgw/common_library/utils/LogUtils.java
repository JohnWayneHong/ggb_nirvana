package com.jgw.common_library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.jgw.common_library.BuildConfig;


/**
 * Created by xsw on 2016/10/27.
 */
public class LogUtils {
    private static boolean debugShowLog = false;

    public static void setDebugShowLog(boolean debugShowLog) {
        LogUtils.debugShowLog = debugShowLog;
    }

    public static void xswShowLog(String msg) {
        if (getShowLogEnable() && !TextUtils.isEmpty(msg)) {
            Log.e("xsw", msg);
        }
    }

    public static void showLog(String msg) {
        if (getShowLogEnable() && !TextUtils.isEmpty(msg)) {
            Log.e("jgw", msg);
        }
    }

    public static void showLog(String tag, String msg) {
        if (getShowLogEnable() && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static boolean getShowLogEnable() {
        return BuildConfig.DEBUG || debugShowLog;
    }
}
