package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.http.BaseResponse;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-06-05.
 */
public class ActiveViewModel extends BaseViewModel {

    private UserRepository mUserRepository;


    public ActiveViewModel() {
        mUserRepository = new UserRepository();
    }

    public LiveData<Resource<BaseResponse>> activeApp(String loginname, String qrCode) {
        return mUserRepository.activeApp(loginname, qrCode);
    }
}
