package cn.looksafe.client.repository;


import com.look.core.http.ApiFactory;

import cn.looksafe.client.api.LookApi;

/**
 * Created by huyg on 2020-02-11.
 */
public class ApiRepository {

    public LookApi apiInterface;

    public ApiRepository() {
        apiInterface = ApiFactory.getInstance().getApi(LookApi.class);
    }


}
