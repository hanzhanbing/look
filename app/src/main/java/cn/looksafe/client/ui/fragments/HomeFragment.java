package cn.looksafe.client.ui.fragments;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseFragment;
import com.look.core.vo.ResourceListener;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.databinding.FragmentHomeBinding;
import cn.looksafe.client.viewmodel.UserViewModel;

/**
 * Created by huyg on 2020-05-28.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {


    private String[] tabs = {"推荐", "公益视训", "快乐学习", "轻松一刻", "教学广场"};

    private List<Fragment> mFragments;
    private UserViewModel mViewModel;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mBinding.setPresenter(new Presenter());
        initFragment();
        initTabLayout();
        initViewPager();
    }


    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(VideoFragment.newInstance(0));
        mFragments.add(VideoFragment.newInstance(1));
        mFragments.add(VideoFragment.newInstance(2));
        mFragments.add(VideoFragment.newInstance(3));
        mFragments.add(VideoFragment.newInstance(4));
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }


    private void getUserInfo() {
        mViewModel.getUserInfo(SpManager.getInstance(mContext).getSP("phone")).observe(this, resource -> resource.work(new ResourceListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo data) {
                UserInfo.UserinfoBean userinfoBean = data.getUserinfo();
                GlideManager.getInstance().displayNetImageWithCircle(mContext, userinfoBean.getHeadimg(), mBinding.avatar, getResources().getDrawable(R.mipmap.ic_user_avatar));
            }

            @Override
            public void onError(String msg) {
            }
        }));
    }

    private void initTabLayout() {
        for (int i = 0; i < tabs.length; i++) {
            mBinding.tablayout.addTab(mBinding.tablayout.newTab().setText(tabs[i]));
        }
    }

    private void initViewPager() {
        mBinding.viewpager.setOffscreenPageLimit(3);
        mBinding.viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return tabs.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });

        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }

    public class Presenter {
        public void clickSearch() {
            ToastUtils.showShort("功能未完善，正在努力开发中...");
        }
    }


}
