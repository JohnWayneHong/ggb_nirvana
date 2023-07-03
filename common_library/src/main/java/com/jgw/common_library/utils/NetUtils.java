package com.jgw.common_library.utils;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresPermission;

import com.jgw.common_library.base.CustomApplication;

/**
 * Created by XiongShaoWu
 * on 2020/4/29
 */
public class NetUtils {
    /**
     * 网络是否已连接
     *
     * @return true:已连接 false:未连接
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean iConnected() {
        ConnectivityManager manager = (ConnectivityManager) CustomApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }
}
