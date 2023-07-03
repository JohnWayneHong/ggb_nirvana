package com.jgw.common_library.utils;

import android.graphics.drawable.Drawable;

import com.jgw.common_library.base.CustomApplication;

/**
 * Created by XiongShaoWu
 * on 2019/9/29
 */
public class ResourcesUtils {

    public static int getColor(int res) {
        return CustomApplication.context.getResources().getColor(res);
    }

    public static String getString(int resId) {
        return CustomApplication.context.getString(resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        return CustomApplication.context.getString(resId, formatArgs);
    }

    public static Drawable getDrawable(int drawableId){
        return CustomApplication.context.getResources().getDrawable(drawableId);
    }
}
