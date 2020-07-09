package com.look.core.http.api;


import com.look.core.BuildConfig;

/**
 * Created by huyg on 2020-01-14.
 */
public interface ApiService {


    static String API_URL() {
        if (BuildConfig.branch == 0) {
            return "http://www.looksafe.cn/luke/";
        } else {
            return "http://www.xincaokeji.com/luke/";
        }
    }

}
