package com.look.core.http.interceptor;

import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huyg on 2019/4/29.
 */
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();//获取请求
        Request.Builder builder = originalRequest.newBuilder();
        Response response = chain.proceed(builder.build());
        if (response.code() == 401) {
            ARouter.getInstance()
                    .build("/user/login")
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .navigation();
            return response.newBuilder()
                    .body(response.body())
                    .build();
        }
        //body只能读取一次
        return response.newBuilder()
                .body(response.body())
                .build();
    }
}
