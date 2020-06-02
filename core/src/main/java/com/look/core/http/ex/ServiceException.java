package com.look.core.http.ex;

import androidx.annotation.Nullable;

/**
 * Created by huyg on 2020-01-14.
 */
public class ServiceException extends RuntimeException{

    private int code;
    private String message;

    public ServiceException(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }
}
