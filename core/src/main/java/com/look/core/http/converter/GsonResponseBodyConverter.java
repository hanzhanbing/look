package com.look.core.http.converter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.look.core.http.BaseResponse;
import com.look.core.http.FormatResponse;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;


/**
 * Created by huyg on 2020-02-12.
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private Gson mGson = new Gson();
    Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String responseStr = "";
            String valueStr = value.string();
            BaseResponse baseResponse = mGson.fromJson(valueStr, BaseResponse.class);
            FormatResponse formatResponse = new FormatResponse();
            formatResponse.setCode(baseResponse.getCode());
            formatResponse.setMsg(baseResponse.getMsg());
            formatResponse.setData("good");
            responseStr = mGson.toJson(formatResponse);
            String replaceStr=  responseStr.replaceAll("\"good\"",valueStr);
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
            InputStream inputStream = new ByteArrayInputStream(replaceStr.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);
            return adapter.read(gson.newJsonReader(reader));
        } finally {
            value.close();
        }
    }
}


