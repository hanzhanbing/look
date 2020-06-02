package cn.looksafe.client.ui.fragments;

import com.alibaba.android.arouter.launcher.ARouter;
import com.look.core.ui.BaseFragment;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.FragmentUserCenterBinding;

/**
 * Created by huyg on 2020-05-29.
 */
public class UserCenterFragment extends BaseFragment<FragmentUserCenterBinding> {

    public static UserCenterFragment newInstance() {
        UserCenterFragment userCenterFragment = new UserCenterFragment();
        return userCenterFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void init() {
        mBinding.setPresenter(new Presenter());
    }


    public class Presenter {

        //设置
        public void goSetting() {
            ARouter.getInstance().build("/setting/setting").navigation();
        }

        //视力记录
        public void goRecord() {
            ARouter.getInstance().build("/eye/log").navigation();
        }

        //vip购买
        public void goVip() {
            ARouter.getInstance().build("/user/vip").navigation();
        }
    }
}
