package cn.looksafe.client.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseFragment;
import com.look.core.vo.ResourceListener;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.BuildConfig;
import cn.looksafe.client.MyApplication;
import cn.looksafe.client.R;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.beans.VideoType;
import cn.looksafe.client.databinding.FragmentHomeBinding;
import cn.looksafe.client.viewmodel.UserViewModel;

/**
 * Created by huyg on 2020-05-28.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {


    private List<String> tabs;

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
        initView();

    }


    private void initView() {
        initFragment();
        initViewPager();
        initTabLayout();
    }


    private void initTabLayout() {
        mBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (BuildConfig.branch == 1 && view instanceof TextView) {
                    ((TextView) view).setTextSize(19);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (BuildConfig.branch == 1 && view instanceof TextView) {
                    ((TextView) view).setTextSize(14);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initFragment() {
        List<VideoType.MainlistBean> mainlistBeans = MyApplication.getInstance().getTypeBeans();
        mFragments = new ArrayList<>();
        tabs = new ArrayList<>();
        mFragments.add(VideoFragment.newInstance(-1));
        mBinding.tablayout.addTab(mBinding.tablayout.newTab().setText("推荐"));
        tabs.add("推荐");
        if (mainlistBeans != null && mainlistBeans.size() > 0) {
            for (VideoType.MainlistBean mainlistBean : mainlistBeans) {
                tabs.add(mainlistBean.getTmname());
                mBinding.tablayout.addTab(mBinding.tablayout.newTab().setText(mainlistBean.getTmname()));
                mFragments.add(VideoFragment.newInstance(mainlistBean.getTmid()));
            }
        }
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
                return tabs.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position);
            }
        });

        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
        if (BuildConfig.branch == 1) {
            for (int i = 0; i < mBinding.tablayout.getTabCount(); i++) {
                TabLayout.Tab tab = mBinding.tablayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(getTabView(i));
                }
            }
        }

    }

    public class Presenter {
        public void clickSearch() {
            ToastUtils.showShort("功能未完善，正在努力开发中...");
        }
    }


    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(tabs.get(currentPosition));
        return view;
    }

}
