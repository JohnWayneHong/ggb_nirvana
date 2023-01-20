package com.ggb.nirvanaclub.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HttpResult<T> implements Serializable {
    @SerializedName("success")
    public boolean success;
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public T data;

    public static int SUCCESS = 0;
    public static int SIGN_OUT = 401 ;//token验证失败

    public boolean isSuccess() {
        return success;
    }

    public boolean isTokenInvalid() {
        return code == SIGN_OUT;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code='" + code + '\'' +
                ", msg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}