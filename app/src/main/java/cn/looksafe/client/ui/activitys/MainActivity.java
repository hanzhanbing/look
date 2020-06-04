package cn.looksafe.client.ui.activitys;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.look.core.manager.AppManager;
import com.look.core.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityMainBinding;
import cn.looksafe.client.ui.fragments.FindFragment;
import cn.looksafe.client.ui.fragments.HomeFragment;
import cn.looksafe.client.ui.fragments.PlanFragment;
import cn.looksafe.client.ui.fragments.UserCenterFragment;
import qiu.niorgai.StatusBarCompat;


@Route(path = "/ui/main")
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private long backtime = 0;
    private List<Fragment> mFragments;
    private ActivityMainBinding mBinding;
    private BottomNavigationView mNavigation;
    private ViewPager mViewpager;
    private HomeFragment mHomeFragment;
    private PlanFragment mPlanFragment;
    private FindFragment mFindFragment;
    private UserCenterFragment mUserCenterFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color._FDB232));
        StatusBarUtils.setLightStatusBar(this, true, false);
        init();
    }

    private void init() {
        mViewpager = mBinding.viewpager;
        mNavigation = mBinding.navigation;

        initFragment();
        initViewPager();
        initNV();
    }


    private void initNV() {
        mNavigation.setItemIconTintList(null);
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{getResources().getColor(R.color._787878),
                getResources().getColor(R.color._FFE304)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        mNavigation.setItemTextColor(csl);
        mNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mHomeFragment = HomeFragment.newInstance();
        mPlanFragment = PlanFragment.newInstance();
        mFindFragment = FindFragment.newInstance();
        mUserCenterFragment = UserCenterFragment.newInstance();
        mFragments.add(mHomeFragment);
        mFragments.add(mPlanFragment);
        mFragments.add(mFindFragment);
        mFragments.add(mUserCenterFragment);
    }

    private void initViewPager() {
        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetToDefaultIcon();
                switch (mNavigation.getMenu().getItem(position).getItemId()) {
                    case R.id.nv_home:
                        mNavigation.getMenu().getItem(position).setIcon(R.mipmap.ic_nv_home);
                        break;
                    case R.id.nv_plan:
                        mNavigation.getMenu().getItem(position).setIcon(R.mipmap.ic_nv_plan);
                        break;
                    case R.id.nv_find:
                        mNavigation.getMenu().getItem(position).setIcon(R.mipmap.ic_nv_find);
                        break;
                    case R.id.nv_mine:
                        mNavigation.getMenu().getItem(position).setIcon(R.mipmap.ic_nv_mine);
                        break;
                }
                mNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.isChecked()) {
            return true;
        }
        resetToDefaultIcon();
        switch (menuItem.getItemId()) {
            case R.id.nv_home:
                mViewpager.setCurrentItem(0);
                menuItem.setIcon(R.mipmap.ic_nv_home);
                break;
            case R.id.nv_plan:
                mViewpager.setCurrentItem(1);
                menuItem.setIcon(R.mipmap.ic_nv_plan);
                break;
            case R.id.nv_find:
                mViewpager.setCurrentItem(2);
                menuItem.setIcon(R.mipmap.ic_nv_find);
                break;
            case R.id.nv_mine:
                mViewpager.setCurrentItem(3);
                menuItem.setIcon(R.mipmap.ic_nv_mine);
                break;
        }
        return true;
    }

    private void resetToDefaultIcon() {
        Menu menu = mNavigation.getMenu();
        menu.findItem(R.id.nv_home).setIcon(R.mipmap.ic_nv_home_default);
        menu.findItem(R.id.nv_plan).setIcon(R.mipmap.ic_nv_plan_default);
        menu.findItem(R.id.nv_find).setIcon(R.mipmap.ic_nv_find_default);
        menu.findItem(R.id.nv_mine).setIcon(R.mipmap.ic_nv_mine_default);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://返回
                if (System.currentTimeMillis() - backtime < 2000) {
                    AppManager.getAppManager().finishAllActivity();
                    finish();
                    System.exit(0);
                } else {
                    backtime = System.currentTimeMillis();
                }
                break;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
