package com.jgw.common_library.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.Nullable;

import com.jgw.common_library.base.CustomApplication;


/**
 * Created by xsw on 2016/10/25.
 */
public class BuildConfigUtils {
    @Nullable
    public static String getVersionName() {
        String versionName = null;
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
        }
        return versionName;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                versionCode = packageInfo.versionCode;
            } else {
                versionCode = (int) packageInfo.getLongVersionCode();
            }
        }
        return versionCode;
    }

    @Nullable
    public static PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        PackageManager packageManager = CustomApplication.context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(CustomApplication.context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
