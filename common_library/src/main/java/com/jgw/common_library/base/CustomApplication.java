package com.jgw.common_library.base;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.hjq.toast.Toaster;
import com.jgw.common_library.utils.MMKVUtils;

/**
 * Created by XiongShaoWu
 * on 2019/9/10
 */
public class CustomApplication extends Application {
    public static CustomApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MMKVUtils.init(this);
        Toaster.init(this, com.jgw.common_library.utils.ToastUtils.getNormalStyle());
    }

    public void setToastStyle(int gravity, int textSize) {
        Toaster.setStyle(com.jgw.common_library.utils.ToastUtils.getCustomStyle(this, gravity, textSize));
    }

    /**
     * dp转px
     */
    public int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    public static Context getCustomApplicationContext() {
        return context.getApplicationContext();
    }
}
