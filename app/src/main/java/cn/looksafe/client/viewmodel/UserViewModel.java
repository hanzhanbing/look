package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-05-28.
 */
public class UserViewModel extends BaseViewModel {

    private UserRepository mIndexRepository;


    public UserViewModel(){
        mIndexRepository = new UserRepository();
    }


    public LiveData<Resource<HttpBean>> login(String phone, String password){
        return mIndexRepository.login(phone,password);
    }

}
