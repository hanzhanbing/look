package cn.looksafe.client.viewmodel;

import androidx.lifecycle.LiveData;

import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-06-02.
 */
public class SuggestViewModel extends BaseViewModel {

    private UserRepository mUserRepository;

    public SuggestViewModel() {
        mUserRepository = new UserRepository();
    }



    public LiveData<Resource<Void>> suggest(String loginName, String content) {
        return mUserRepository.suggest(loginName,content);
    }
}
