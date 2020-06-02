package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.http.BaseResponse;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-05-28.
 */
public class UserViewModel extends BaseViewModel {

    private UserRepository mUserRepository;


    public UserViewModel() {
        mUserRepository = new UserRepository();
    }


    public LiveData<Resource<HttpBean>> login(String phone, String password) {
        return mUserRepository.login(phone, password);
    }


    public LiveData<Resource<BaseResponse>> resetPwd(String loginname, String prePwd, String newPwd) {
        return mUserRepository.resetPwd(loginname, prePwd, newPwd);
    }

    public LiveData<Resource<BaseResponse>> getSmsCode(String phone){
        return mUserRepository.getSmsCode(phone);
    }

    public LiveData<Resource<BaseResponse>> register(String phone, String pwd, String code){
        return mUserRepository.register(phone,pwd,code);
    }

    public LiveData<Resource<BaseResponse>> getCode(String phone) {
        return mUserRepository.getCode(phone);
    }

}
