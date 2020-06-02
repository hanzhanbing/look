package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.repository.UserRepository;
import cn.looksafe.client.ui.fragments.FindFragment;

/**
 * Created by huyg on 2020-06-02.
 */
public class FindViewModel extends BaseViewModel {

    private UserRepository mUserRepository;

    public FindViewModel() {
        mUserRepository = new UserRepository();
    }

    public LiveData<Resource<PointHttp>> getPoints(String loginName) {
        return mUserRepository.getPoints(loginName);
    }
}
