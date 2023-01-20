package com.ggb.nirvanaclub.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ggb.nirvanaclub.listener.LifeCycleListener;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by hongweijie on 2022/10/22.
 * 功能描述：
 * 版本：
 */

public abstract class BaseFragment extends RxFragment {
    public View mView ;
    public boolean isLoadComplete = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(getLayoutResource(),container,false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        beForInitView();
        if (mListener != null) {
            mListener.setContext(getContext());
        }
        initView();
    }

    protected void beForInitView(){}
    public void refreshData(){}
    protected abstract int getLayoutResource();
    protected abstract void initView();

    public void initData(){}

    /**
     * 回调函数
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
//        ActivityStackManager.getManager().remove(this);
    }
}
