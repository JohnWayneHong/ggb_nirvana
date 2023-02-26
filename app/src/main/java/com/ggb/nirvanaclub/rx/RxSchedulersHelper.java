package com.ggb.nirvanaclub.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hongweijie on 2022/10/22.
 * 功能描述：
 * 版本：
 */

public class RxSchedulersHelper {
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream ->
                //子线程进行Http访问
                upstream.subscribeOn(Schedulers.io())
                        //UI线程处理返回接口
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> io_io() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
    }
}
