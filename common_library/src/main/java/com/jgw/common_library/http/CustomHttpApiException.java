package com.jgw.common_library.http;

/**
 * Created by xsw on 2017/4/21.
 */

public class CustomHttpApiException extends RuntimeException {
    private String msg;
    private int code;
    private Object data;

    public CustomHttpApiException(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public CustomHttpApiException(int code, String msg, Object object) {
        this.msg = msg;
        this.code = code;
        data = object;
    }

    public String getApiExceptionMessage() {
        return msg;
    }

    public int getApiExceptionCode() {
        return code;
    }

    public <T> T getApiExceptionCode(Class<T> clazz) {
        return (T) data;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
