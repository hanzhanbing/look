package cn.looksafe.client.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.beans.VersionHttp;
import retrofit2.http.Field;

/**
 * Created by huyg on 2020-02-11.
 */
public class UserRepository extends ApiRepository {


    public LiveData<Resource<HttpBean>> login(String phone, String password) {
        return new NetworkOnlyResource<HttpBean>() {

            @NonNull
            @Override
            protected LiveData<ApiResponse<HttpBean>> createCall() {
                return apiInterface.login(phone, password);
            }
        }.asLiveData();

    }

    public LiveData<Resource<EyeLogHttp>> getEyesRecord(String loginName) {
        return new NetworkOnlyResource<EyeLogHttp>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<EyeLogHttp>> createCall() {
                return apiInterface.getEyesRecord(loginName);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> addEyesRecord(String loginName, String left, String right) {
        return new NetworkOnlyResource<Boolean>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Boolean>> createCall() {
                return apiInterface.addEyesRecord(loginName, left, right);
            }
        }.asLiveData();
    }

    public LiveData<Resource<VersionHttp>> checkVersion(String loginName) {
        return new NetworkOnlyResource<VersionHttp>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<VersionHttp>> createCall() {
                return apiInterface.checkVersion(loginName);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PointHttp>> getPoints(String loginName) {
        return new NetworkOnlyResource<PointHttp>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PointHttp>> createCall() {
                return apiInterface.getPoints(loginName);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> suggest(String loginName, String content) {
        return new NetworkOnlyResource<Void>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiInterface.suggest(loginName, content);
            }
        }.asLiveData();
    }
}
