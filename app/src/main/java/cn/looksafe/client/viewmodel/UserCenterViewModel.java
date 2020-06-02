package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-06-02.
 */
public class UserCenterViewModel extends BaseViewModel {

    private UserRepository mUserRepository;

    public UserCenterViewModel(){
        mUserRepository = new UserRepository();
    }



    public LiveData<Resource<UserInfo>> getUserInfo() {
        return mUserRepository.getUserInfo();
    }
}
