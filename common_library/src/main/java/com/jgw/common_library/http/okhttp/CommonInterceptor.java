package com.jgw.common_library.http.okhttp;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jgw.common_library.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class CommonInterceptor implements Interceptor {

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    @NotNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        //获取原先的请求对象
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        urlBuilder.addEncodedQueryParameter("localRequestTime", System.currentTimeMillis() + "");
        HttpUrl httpUrl = urlBuilder.build();
        builder.url(httpUrl);
        String url = urlBuilder.toString();

        String bigFile = request.header("BigFile");
        String requestInfo = request.toString();
        LogUtils.xswShowLog("request=" + requestInfo);
        printParams(request.body());
        Response response = chain.proceed(request);
        ResponseBody body = response.body();
        String json = "";
        if (body != null && !TextUtils.equals("ture", bigFile)) {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (charset != null) {
                json = buffer.clone().readString(charset);
            }
            LogUtils.xswShowLog("url=" + url + ":json=" + json);
        } else {
            LogUtils.xswShowLog("url=" + url+ ":bigFile=ture");
        }
        return response;
    }

    private void printParams(@Nullable RequestBody body) {
        if (body == null) {
            return;
        }
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = null;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            String params = buffer.readString(charset);
            LogUtils.xswShowLog("请求参数:" + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

