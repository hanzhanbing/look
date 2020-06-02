package com.look.core.http.interceptor;



import com.look.core.CoreSource;
import com.look.core.manager.SpManager;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huyg on 2018/4/24.
 */

public class ParamsInterceptor implements Interceptor {

    private Map<String, String> bodyParams;

    @Override
    public Response intercept(Chain chain) throws IOException {
        bodyParams = new TreeMap<>();
        Request request = chain.request();
        if (request.method().equals("POST")) {
            if (request.body() == null || request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                if (request.body() != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                }
                bodyBuilder
                        .addEncoded("token", SpManager.getInstance(CoreSource.getInstance().getContext()).getSP("token"));
                formBody = bodyBuilder.build();
                request = request.newBuilder().post(formBody).build();
            }
        }


        Response response = chain.proceed(request);
        return response;
    }

}
