package com.look.core.http;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.look.core.http.adapter.LiveDataCallAdapterFactory;
import com.look.core.http.api.ApiService;
import com.look.core.http.converter.GsonFactory;
import com.look.core.http.converter.NullOnEmptyConverterFactory;
import com.look.core.http.interceptor.ParamsInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by huyg on 2020-01-14.
 */
public class RetrofitManager {


    public Context context;
    private Retrofit mRetrofit;


    private static final class RetrofitClientHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return RetrofitClientHolder.INSTANCE;
    }


    public RetrofitManager() {
        createRetrofit();
    }

    private void createRetrofit() {
        OkHttpClient.Builder clientBuild = (new OkHttpClient.Builder())
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new ParamsInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonFactory.create())
                .client(clientBuild.build())
                .build();
    }

    public <T> T create(Class<?> clazz) {
        return (T) mRetrofit.create(clazz);
    }


}
