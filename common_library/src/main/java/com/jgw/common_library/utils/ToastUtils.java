package com.jgw.common_library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.hjq.toast.Toaster;
import com.hjq.toast.config.IToastStyle;
import com.hjq.toast.style.BlackToastStyle;
import com.jgw.common_library.base.CustomApplication;
import com.jgw.common_library.base.ui.BaseActivity;


/**
 * Created by xsw on 2016/10/25.
 */
public class ToastUtils {
    public static void showToast(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toaster.setGravity(Gravity.BOTTOM, 0, (int) (60 * BaseActivity.getXMultiple()));
            Toaster.show(msg);
        }
    }

    public static void showToastCenter(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toaster.setGravity(Gravity.CENTER, 0, 0);
            Toaster.show(msg);
        }
    }

    public static IToastStyle<?> getNormalStyle() {
        return new BlackToastStyle() {
            @Override
            public int getGravity() {
                return Gravity.BOTTOM;
            }
        };
    }

    public static IToastStyle<?> getCustomStyle(CustomApplication customApplication, int gravity, int textSize) {
        return new BlackToastStyle() {
            @Override
            public int getGravity() {
                return gravity;
            }

            @Override
            protected float getTextSize(Context context) {
                return customApplication.dp2px(textSize);
            }
        };
    }
}
