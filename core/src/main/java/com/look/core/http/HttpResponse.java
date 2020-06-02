package com.look.core.http;

/**
 * Created by huyg on 2020-01-14.
 */
public class HttpResponse<T> {

    private T data;
    private int code;
    private String msg;
    public static int SUCCESS = 0;

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return SUCCESS == code;
    }

    public boolean isAuthFail() {
        return code == -1;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
