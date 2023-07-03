package com.jgw.common_library.http;


import com.jgw.common_library.http.okhttp.ApiConfigInfoBean;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 * Created by admin on 2019/2/15.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class HttpClient {

    private static final HashMap<ApiConfigInfoBean, Object> map = new HashMap<>();

    public static synchronized <T> T getApi(Class<T> clz, String url, OkHttpClient client) {
        ApiConfigInfoBean<T> infoBean = new ApiConfigInfoBean<>(clz, url,client);
        Object o = map.get(infoBean);
        if (o == null) {
            o = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(clz);
            map.put(infoBean, o);
        }
        return (T) o;
    }

    public static void release(){
        map.clear();
    }
}
