package com.look.core.http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.look.core.http.HttpResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by huyg on 2020-02-12.
 */
public class GsonFactory<T> extends Converter.Factory {

    private final Gson gson;

    public static GsonFactory create() {
        return create(new Gson());
    }

    public static GsonFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        } else {
            return new GsonFactory(gson);
        }
    }

    private GsonFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        ParameterizedTypeImpl parameterizedType =
                new ParameterizedTypeImpl(null, HttpResponse.class, TypeToken.get(type).getType());
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(parameterizedType));
        return new GsonResponseBodyConverter(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }


}
