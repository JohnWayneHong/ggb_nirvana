package com.ggb.nirvanaclub.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThirdHttpResult<T> implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public T data;

    public static int SUCCESS = 0;
    public static int SIGN_OUT = 401 ;//token验证失败


    public boolean isTokenInvalid() {
        return code == SIGN_OUT;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ThirdHttpResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}