package com.jgw.common_library.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jgw.common_library.base.CustomApplication;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.UUID;

public class DevicesUtils {

    private static String uniqueId = "";
    private static String oldUniqueId = "";

    @NotNull
    public static String getDevicesSerialNumber() {
        if (!TextUtils.isEmpty(uniqueId)) {
            return uniqueId;
        }
        String serial = null;
        try {
            //noinspection rawtypes
            Class c = Class.forName("android.os.SystemProperties");
            //noinspection unchecked
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            if (TextUtils.isEmpty(serial)) {
                serial = (String) get.invoke(c, "persist.hw.serial_no");
            }
            if (TextUtils.isEmpty(serial)) {
                serial = Build.SERIAL;
            }
            if (TextUtils.isEmpty(serial) || TextUtils.equals("unknown", serial)) {
                serial = Settings.Secure.getString(CustomApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            LogUtils.showLog("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serial != null && !TextUtils.equals("unknown", serial)) {
            uniqueId = serial;
        }
        return uniqueId;
    }


    //获取本机的MD5
    @NotNull
    public static String getOldDevicesSerialNumber() {
        if (!TextUtils.isEmpty(oldUniqueId)) {
            return oldUniqueId;
        }
        final TelephonyManager tm = (TelephonyManager) CustomApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(CustomApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        oldUniqueId = deviceUuid.toString();
        return oldUniqueId;
    }


}
