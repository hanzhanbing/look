package cn.looksafe.client.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.beans.VideosBean;
import retrofit2.http.Field;

/**
 * Created by huyg on 2020-05-29.
 */
public class VideoRepository extends ApiRepository {


    public LiveData<Resource<VideosBean>> getVideoById(String loginName, int vid) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getVideoById(loginName, vid);
            }
        }.asLiveData();
    }

    public LiveData<Resource<VideosBean>> getHappyLearnApp(String phone) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getHappyLearnApp(phone);
            }
        }.asLiveData();
    }

    public LiveData<Resource<VideosBean>> getRelaxApp(String loginName) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getRelaxApp(loginName);
            }
        }.asLiveData();
    }


    public LiveData<Resource<VideosBean>> getLovelyApp(String loginName) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getLovelyApp(loginName);
            }
        }.asLiveData();
    }


    public LiveData<Resource<VideosBean>> getFreeApp(String loginName) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getFreeApp(loginName);
            }
        }.asLiveData();
    }


    public LiveData<Resource<VideosBean>> getHotVideo(String phone) {
        return new NetworkOnlyResource<VideosBean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosBean>> createCall() {
                return apiInterface.getHotVideo(phone);
            }
        }.asLiveData();
    }

    public LiveData<Resource<LoopImgHttp>> getLoopImgs(String loginname) {
        return new NetworkOnlyResource<LoopImgHttp>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<LoopImgHttp>> createCall() {
                return apiInterface.getLoopImgs(loginname);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseResponse>> addPlayTime(String loginname, int vid) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.addPlayTime(loginname, vid);
            }
        }.asLiveData();
    }
}
