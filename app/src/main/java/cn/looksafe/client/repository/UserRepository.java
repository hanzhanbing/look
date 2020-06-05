package cn.looksafe.client.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;
import com.look.core.http.HttpResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.beans.UploadFile;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.beans.VersionHttp;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.Part;

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

    public LiveData<Resource<BaseResponse>> addEyesRecord(String loginName, String left, String right) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
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

    public LiveData<Resource<BaseResponse>> suggest(String loginName, String content) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.suggest(loginName, content);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseResponse>> resetPwd(String loginname, String prePwd, String newPwd) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.resetPwd(loginname, prePwd, newPwd);
            }
        }.asLiveData();
    }

    public LiveData<Resource<UserInfo>> getUserInfo(String loginname) {
        return new NetworkOnlyResource<UserInfo>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<UserInfo>> createCall() {
                return apiInterface.getUserInfo(loginname);
            }
        }.asLiveData();
    }


    public LiveData<Resource<BaseResponse>> getSmsCode(String phone) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.getSmsCode(phone);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseResponse>> register(String phone, String pwd, String code) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.register(phone, pwd, code);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseResponse>> getCode(String phone) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.getCode(phone);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseResponse>> modifyUserInfo(String loginname, String nickName, String headImg) {
        return new NetworkOnlyResource<BaseResponse>() {

            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.modifyUserInfo(loginname, nickName, headImg);
            }
        }.asLiveData();
    }

    public LiveData<Resource<UploadFile>> upload(MultipartBody.Part file) {
        return new NetworkOnlyResource<UploadFile>() {

            @NonNull
            @Override
            protected LiveData<ApiResponse<UploadFile>> createCall() {
                return apiInterface.upload(file);
            }
        }.asLiveData();
    }


    public LiveData<Resource<BaseResponse>> activeApp(String loginname, String qrCode) {
        return new NetworkOnlyResource<BaseResponse>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return apiInterface.activeApp(loginname, qrCode);
            }
        }.asLiveData();
    }

}
