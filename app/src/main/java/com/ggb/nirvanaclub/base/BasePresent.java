package com.ggb.nirvanaclub.base;

import android.content.Context;
import android.os.Bundle;

import com.ggb.nirvanaclub.listener.LifeCycleListener;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by likai on 2018/3/23.
 * 功能描述：
 * 版本：
 */

public class BasePresent<T> implements LifeCycleListener {
    protected Reference<T> mViews;
    protected T mView;
    protected Context mContext;

    public BasePresent(T mView){
        attachView(mView);
        setListener(mView);
    }

    public BasePresent(){

    }

    /**
     * 设置生命周期监听
     *
     * @author ZhongDaFeng
     */
    private void setListener(T mView) {
        if (getView() != null) {
            if (mView instanceof MyBaseActivity) {
                ((MyBaseActivity) getView()).setOnLifeCycleListener(this);
            } else if (mView instanceof BaseFragment) {
                ((BaseFragment) getView()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 关联
     *
     * @param view
     */
    private void attachView(T view) {
        mViews = new WeakReference<T>(view);
        mView = mViews.get();
    }

    /**
     * 销毁
     */
    private void detachView() {
        if (isViewAttached()) {
            mViews.clear();
            mViews = null;
        }
    }


    /**
     * 获取
     *
     * @return
     */
    public T getView() {
        if (mViews == null) {
            return null;
        }
        return mViews.get();
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return mViews != null && mViews.get() != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
