package cn.looksafe.client.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.look.core.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.FragmentHomeBinding;

/**
 * Created by huyg on 2020-05-28.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {


    private String[] tabs = {"推荐","快乐学习", "轻松一刻", "公益视训","教学广场"};

    private List<Fragment> mFragments;
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


}
