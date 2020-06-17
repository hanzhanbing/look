package cn.looksafe.client.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.ActionBean;
import retrofit2.http.Field;

/**
 * Created by huyg on 2020-06-16.
 */
public class DataRepository extends ApiRepository{



    public LiveData<Resource<BaseResponse>> upLoadPlayLog(ActionBean actionBean){
        return new NetworkOnlyResource<BaseResponse>(){

            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.upLoadPlayLog(actionBean);
            }
        }.asLiveData();
    }
}
