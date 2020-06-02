package com.look.core.http.adapter;

import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by huyg on 2020-01-14.
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {


    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (CallAdapter.Factory.getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type liveDataType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Type rawLiveDataType = getRawType(liveDataType);
        if (rawLiveDataType != ApiResponse.class) {
            throw new IllegalArgumentException("type must be a resource");
        }
        if (!(liveDataType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("resource must be parameterized");
        }

        Type bodyType = getParameterUpperBound(0, (ParameterizedType) liveDataType);

        return new LiveDataCallAdapter(bodyType);
    }
}
