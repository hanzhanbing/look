package cn.looksafe.client.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-06-01.
 */
public class SettingViewModel extends BaseViewModel {

    private UserRepository mUserRepository;

    public SettingViewModel() {
        mUserRepository = new UserRepository();
    }


    public LiveData<Resource<VersionHttp>> checkVersion(String loginName) {
        return mUserRepository.checkVersion(loginName);
    }

}
