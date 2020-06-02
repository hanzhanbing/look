package com.look.core.http;

/**
 * Created by huyg on 2020-05-27.
 */
public class FormatResponse {

    private int code;
    private String msg;
    private String data="&&";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
