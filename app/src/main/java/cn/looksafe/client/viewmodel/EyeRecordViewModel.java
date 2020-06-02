package cn.looksafe.client.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.repository.UserRepository;

/**
 * Created by huyg on 2020-06-01.
 */
public class EyeRecordViewModel extends BaseViewModel {


    private UserRepository mUserRepository;

    public EyeRecordViewModel() {
        mUserRepository = new UserRepository();
    }


    public LiveData<Resource<EyeLogHttp>> getEyesRecord(String loginName) {
        return mUserRepository.getEyesRecord(loginName);
    }

    public LiveData<Resource<Boolean>> addEyesRecord(String loginName, String left, String right) {
        return mUserRepository.addEyesRecord(loginName, left, right);
    }

}
