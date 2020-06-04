package cn.looksafe.client.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import java.io.File;

import cn.looksafe.client.beans.UploadFile;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.repository.UserRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by huyg on 2020-06-02.
 */
public class UserCenterViewModel extends BaseViewModel {

    private UserRepository mUserRepository;

    public UserCenterViewModel(){
        mUserRepository = new UserRepository();
    }



    public LiveData<Resource<UserInfo>> getUserInfo(String loginname) {
        return mUserRepository.getUserInfo(loginname);
    }


    public LiveData<Resource<BaseResponse>> modifyUserInfo(String loginname, String nickName,String headImg) {
        return mUserRepository.modifyUserInfo(loginname,nickName,headImg);
    }


    public LiveData<Resource<UploadFile>> upload(File file){
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return mUserRepository.upload(part);
    }
}
