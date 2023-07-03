package com.jgw.common_library.provider;

import android.content.Context;

import androidx.core.content.FileProvider;

/**
 * Created by XiongShaoWu
 * on 2020/3/19
 */
public class CameraFileProvider extends FileProvider {
    public static String getProviderName(Context context) {
        return context.getPackageName() + ".app.file.provider";
    }
}
