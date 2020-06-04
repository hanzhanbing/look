package cn.looksafe.client.ui.fragments;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseFragment;
import com.look.core.vo.ResourceListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.databinding.FragmentUserCenterBinding;
import cn.looksafe.client.repository.UserRepository;
import cn.looksafe.client.viewmodel.UserCenterViewModel;

/**
 * Created by huyg on 2020-05-29.
 */
public class UserCenterFragment extends BaseFragment<FragmentUserCenterBinding> {

    private UserCenterViewModel mViewModel;

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
        mViewModel = ViewModelProviders.of(this).get(UserCenterViewModel.class);
        mBinding.setPresenter(new Presenter());
        initView();
    }

    private void initView() {
        initRefresh();

    }

    private void initRefresh() {
        mBinding.refresh.setEnableLoadMore(false);
        mBinding.refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mBinding.phone.setText(SpManager.getInstance(mContext).getSP("phone"));
                getUserInfo();
            }
        });
    }


    private void getUserInfo() {
        mViewModel.getUserInfo(SpManager.getInstance(mContext).getSP("phone")).observe(this, resource -> resource.work(new ResourceListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo data) {
                mBinding.refresh.finishRefresh();

                UserInfo.UserinfoBean userinfoBean = data.getUserinfo();
                if (!TextUtils.isEmpty(userinfoBean.getNickname())) {
                    mBinding.realName.setText(userinfoBean.getNickname());
                }
                GlideManager.getInstance().displayNetImageWithCircle(mContext, userinfoBean.getHeadimg(), mBinding.avatar, getResources().getDrawable(R.mipmap.ic_user_avatar));
                if (userinfoBean!=null) {
                    if (userinfoBean.getIsvip() == 1) {//会员
                        SpManager.getInstance(mContext).putSP("vip", true);
                        mBinding.vipBg.setImageResource(R.mipmap.ic_vip);
                        mBinding.vipDate.setText(userinfoBean.getVipexpdate() + "到期");
                    } else {//非会员
                        SpManager.getInstance(mContext).putSP("vip", false);
                        mBinding.vipBg.setImageResource(R.mipmap.ic_no_vip);
                        mBinding.vipDate.setText("开通会员立享精彩");
                    }
                }
            }

            @Override
            public void onError(String msg) {
                mBinding.refresh.finishRefresh();
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.refresh.autoRefresh();
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

        public void goUserInfo(){
            ARouter.getInstance().build("/user/info").navigation();
        }

    }
}
