package com.jgw.common_library.http.rxjava;

import com.jgw.common_library.base.CustomApplication;
import com.jgw.common_library.http.CustomHttpApiException;
import com.jgw.common_library.router.CustomRouter;
import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.LoginPlugin;
import com.jgw.common_library.utils.LogUtils;
import com.jgw.common_library.utils.ToastUtils;

import java.net.SocketTimeoutException;

import io.reactivex.FlowableSubscriber;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/5/8.
 */

public abstract class CustomFlowableSubscriber<T> implements FlowableSubscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof CustomHttpApiException) {
            switchCustomException(((CustomHttpApiException) e).getApiExceptionCode(), ((CustomHttpApiException) e).getApiExceptionMessage());
        } else {
            //网络及其他异常处理
            if (e instanceof HttpException) {
                ToastUtils.showToast("网络错误:" + e.getMessage());
            } else if (e instanceof SocketTimeoutException) {
                ToastUtils.showToast("网络超时");
            } else {
                ToastUtils.showToast("网络繁忙,请稍后再试");
            }
            LogUtils.xswShowLog("Error:++++++++++" + e.getMessage() + e.toString());
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
}
