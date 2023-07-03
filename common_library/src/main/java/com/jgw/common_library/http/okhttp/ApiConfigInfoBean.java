package com.jgw.common_library.http.okhttp;

import java.util.Objects;

import okhttp3.OkHttpClient;

public class ApiConfigInfoBean<T> {

    public Class<T> clazz;
    public String baseUrl;
    public OkHttpClient client;

    public ApiConfigInfoBean(Class<T> clazz, String baseUrl, OkHttpClient client) {
        this.clazz = clazz;
        this.baseUrl = baseUrl;
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiConfigInfoBean<?> that = (ApiConfigInfoBean<?>) o;
        return Objects.equals(clazz, that.clazz) && Objects.equals(baseUrl, that.baseUrl) && Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, baseUrl, client);
    }
}
