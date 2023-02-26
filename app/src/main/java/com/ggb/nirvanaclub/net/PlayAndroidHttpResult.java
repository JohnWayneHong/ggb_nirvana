package com.ggb.nirvanaclub.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayAndroidHttpResult<T> implements Serializable {
    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;
    @SerializedName("data")
    public T data;

    public static int SUCCESS = 0;
    public static int SIGN_OUT = 401 ;//token验证失败


    public boolean isTokenInvalid() {
        return errorCode == SIGN_OUT;
    }

    public int getCode() {
        return errorCode;
    }

    public String getMessage() {
        return errorMsg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PlayAndroidHttpResult{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}