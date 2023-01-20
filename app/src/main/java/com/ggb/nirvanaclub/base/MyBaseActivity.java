package com.ggb.nirvanaclub.base;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.ggb.nirvanaclub.listener.LifeCycleListener;
import com.ggb.nirvanaclub.manager.ActivityStackManager;
import com.ggb.nirvanaclub.view.dialog.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * Created by hongweijie on 2022/10/27.
 * 功能描述：
 * 版本：
 */

public abstract class MyBaseActivity extends RxAppCompatActivity {

    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beForSetContentView();
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
            mListener.setContext(this);
        }
        ActivityStackManager.getManager().push(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SetTransparencyActionBar();
        setContentView(getLayoutResource());

        Log.e("TAG", "sHA1的值---------------->: "+sHA1(this));
        loadingDialog = new LoadingDialog(this);
    }

    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SetTransparencyActionBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 拦截ACTION_DOWN事件，判断是否需要隐藏输入法
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 交由DecorView去做Touch事件的分发
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }

        // Activity内没有View对这个Touch事件做处理，那么有Activity来处理
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View view, MotionEvent ev) {
        // 1、判断是否是EditText，如果不是，直接返回false
        if (view != null && (view instanceof EditText)) {
            int[] location = {0, 0};
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];

            // 2、判断Touch的点是否在EditText外
            if (ev.getX() < left || (ev.getX() > left + view.getWidth())
                    || ev.getY() < top || (ev.getY() > top + view.getHeight())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    protected abstract int getLayoutResource();
    protected abstract void initView();
    protected void beForSetContentView(){}
    protected abstract void initEvent();


    /**
     * 回调函数
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        ActivityStackManager.getManager().remove(this);
    }


}

