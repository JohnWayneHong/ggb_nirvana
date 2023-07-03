package com.jgw.common_library.http;

import java.util.Objects;

/**
 * Created by XiongShaoWu
 * on 2020/7/7
 */
public class Resource<T> {
    public static final int LOADING = 1;
    public static final int NEXT = 2;
    public static final int SUCCESS = 3;
    public static final int ERROR = -1;
    public static final int NETWORK_ERROR = -2;

    private int loadingStatus;
    private String errorMsg;
    private T data;

    public Resource(int success, T data, String s) {
        loadingStatus = success;
        this.data = data;
        this.errorMsg = s;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource<?> resource = (Resource<?>) o;
        return loadingStatus == resource.loadingStatus &&
                Objects.equals(errorMsg, resource.errorMsg) &&
                Objects.equals(data, resource.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadingStatus, errorMsg, data);
    }
}
