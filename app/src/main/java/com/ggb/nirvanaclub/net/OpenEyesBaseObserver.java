package com.ggb.nirvanaclub.net;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.ggb.nirvanaclub.manager.TokenLoseEfficacy;
import com.ggb.nirvanaclub.utils.LogUtils;
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil;
import com.ggb.nirvanaclub.view.dialog.LoadingDialog;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class OpenEyesBaseObserver<T> implements Observer<T> {
    protected Context mContext;
    Boolean doShow = false;

    private LoadingDialog loadingDialog;

    public OpenEyesBaseObserver(Context cxt, Boolean doShow) {
        this.mContext = cxt;
        this.doShow = doShow;
        if(cxt!=null){
            loadingDialog = new LoadingDialog(cxt);
        }
    }

    public OpenEyesBaseObserver(Context cxt) {
        this.mContext = cxt;
    }
    public OpenEyesBaseObserver() {

    }
  
    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onNext(T tBaseEntity) {
        LogUtils.d(tBaseEntity.toString());
        onRequestEnd();
        try {
            onSuccess(tBaseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof SocketTimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            }else if(e instanceof MalformedJsonException ||e instanceof JsonSyntaxException){
                Throwable throwable = new Throwable("??????????????????");
                onFailure(throwable, true);
                LogUtils.e("??????????????????"+e.getMessage());
            }else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }
  
    /** 
     * ???????????? 
     * 
     * @param t 
     * @throws Exception
     */  
    protected abstract void onSuccess(T t) throws Exception;
  
    /** 
     * ???????????????,??????code?????? 
     * 
     * @param t 
     * @throws Exception
     */  
    protected void onCodeError(T t) throws Exception {}
  
    /** 
     * ???????????? 
     * 
     * @param e 
     * @param isNetWorkError ????????????????????? 
     * @throws Exception
     */  
    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

    }
  
    protected void onRequestStart() {
        if(doShow&&loadingDialog!=null){
            showProgressDialog();
        }
    }  
  
    protected void onRequestEnd() {
        if(doShow&&loadingDialog!=null){
            closeProgressDialog();
        }
    }
  
    public void showProgressDialog() {
        loadingDialog.showLoading("?????????...");
    }
  
    public void closeProgressDialog() {
        loadingDialog.dismiss();
    }
  
}  