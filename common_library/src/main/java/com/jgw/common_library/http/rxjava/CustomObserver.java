package com.jgw.common_library.http.rxjava;

import android.util.Log;

import com.jgw.common_library.base.CustomApplication;
import com.jgw.common_library.http.CustomHttpApiException;
import com.jgw.common_library.router.CustomRouter;
import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.LoginPlugin;
import com.jgw.common_library.router.plugin.WebPlugin;
import com.jgw.common_library.utils.LogUtils;
import com.jgw.common_library.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/5/8.
 */

public abstract class CustomObserver<T> implements Observer<T> {


    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof CustomHttpApiException) {
            switchCustomException(((CustomHttpApiException) e).getApiExceptionCode(), ((CustomHttpApiException) e).getApiExceptionMessage());
        } else {
            //网络及其他异常处理
            if (e instanceof UnknownHostException){
                ToastUtils.showToast("网络繁忙,请稍后再试");
            }else if (e instanceof HttpException) {
                ToastUtils.showToast("网络错误:" + e);
            } else if (e instanceof SocketTimeoutException) {
                ToastUtils.showToast("网络超时");
            } else {
                ToastUtils.showToast("网络繁忙,请稍后再试");
            }
            LogUtils.xswShowLog("Error:++++++++++" + e);
        }
    }

    /**
     * 自定义状态码逻辑处理
     *
     * @param code 自定义状态码
     * @param msg  提示文本
     */
    private void switchCustomException(int code, String msg) {
        switch (code) {
            case 200:
                //特定接口在返回200成功时数据可能为null 主动触发onNext
                onNext(null);
                break;
            case 401://登录失效
                RouterCommand command = new RouterCommand();
                command.domain= LoginPlugin.DOMAIN;
                command.action= LoginPlugin.ACTION_TOKEN_USELESS;
                CustomRouter.route(CustomApplication.getCustomApplicationContext(), command);
                break;
            case 403://接口无权限
                ToastUtils.showToast(msg);
                break;
            default:
                ToastUtils.showToast(msg);
                LogUtils.xswShowLog("CustomException:state=" + code + ",msg=" + msg);
        }

    }

    @Override
    public void onComplete() {
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
