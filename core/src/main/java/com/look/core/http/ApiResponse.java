package com.look.core.http;

import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.look.core.http.ex.HandleException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by huyg on 2020-01-14.
 */
public class ApiResponse<T> {

    public static <T> ApiResponse<T> create(Throwable throwable) {
        return new ApiErrorResponse<T>(throwable.getMessage());
    }

    public static <T> ApiResponse<T> create(Response<HttpResponse<T>> response) {
        if (response.isSuccessful()) {
            HttpResponse<T> body = response.body();
            if (body != null) {
                if (body.isSuccess()) {
                    return new ApiSuccessResponse(body.getData());
                } else if (body.isAuthFail()){
                    ARouter.getInstance()
                            .build("/user/login")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .navigation();
                    return new ApiErrorResponse(body.getCode(),body.getData(), body.getMsg());
                } else {
                    return new ApiErrorResponse(body.getCode(),body.getData(), body.getMsg());
                }
            } else {
                return new ApiErrorResponse(body.getMsg());
            }
        } else {
            return new ApiErrorResponse(HandleException.handle(new HttpException(response)).getMessage());
        }
    }

    public static class ApiErrorResponse<T> extends ApiResponse<T> {
        private String message;
        private int code = -1;
        private T data;

        public ApiErrorResponse(String message) {
            this.message = message;
        }


        public ApiErrorResponse(int code, T data,String message ) {
            this.message = message;
            this.code = code;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        public T getData(){
            return data;
        }
    }


    public static class ApiSuccessResponse<T> extends ApiResponse<T> {
        private T data;

        public ApiSuccessResponse(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }


}
