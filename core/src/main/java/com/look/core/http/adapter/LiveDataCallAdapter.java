package com.look.core.http.adapter;

import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huyg on 2020-01-14.
 */
public class LiveDataCallAdapter<R> implements CallAdapter<HttpResponse<R>, LiveData<ApiResponse<R>>> {

    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }


    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(final Call<HttpResponse<R>> call) {

        return new LiveData<ApiResponse<R>>() {
            private AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false,true)){
                    call.enqueue(new Callback<HttpResponse<R>>() {
                        @Override
                        public void onResponse(Call<HttpResponse<R>> call, Response<HttpResponse<R>> response) {
                            postValue(ApiResponse.create(response));
                        }

                        @Override
                        public void onFailure(Call<HttpResponse<R>> call, Throwable t) {
                            postValue((ApiResponse<R>) ApiResponse.create(t));
                        }
                    });
                }

            }

            @Override
            protected void onInactive() {
                super.onInactive();
                call.cancel();
            }
        };
    }
}
