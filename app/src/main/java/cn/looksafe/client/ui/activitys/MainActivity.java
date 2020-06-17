package cn.looksafe.client.ui.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.look.core.manager.AppManager;
import com.look.core.manager.SpManager;
import com.look.core.util.StatusBarUtils;
import com.look.core.vo.ResourceListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.databinding.ActivityMainBinding;
import cn.looksafe.client.manager.DialogManager;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.ui.fragments.FindFragment;
import cn.looksafe.client.ui.fragments.HomeFragment;
import cn.looksafe.client.ui.fragments.PlanFragment;
import cn.looksafe.client.ui.fragments.UserCenterFragment;
import cn.looksafe.client.viewmodel.SettingViewModel;
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
    private static final int REQ_UPDATE = 999;
    private SettingViewModel mViewModel;
    private boolean getVersion;//是否已获取到服务器最新版本信息
    private int versionCode;
    private String versionName;
    private String upUrl;//升级url
    private int vcodeServer;//最新版本号
    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APK_PATH = "/apk_cache";

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
        getVersion();
        initFragment();
        initViewPager();
        initNV();
    }


    private void getVersion() {
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        versionCode = Integer.parseInt(Tools.getAppVersionInfo(MainActivity.this).split(",")[1]);
        mViewModel.checkVersion(SpManager.getInstance(MainActivity.this).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VersionHttp>() {
                    @Override
                    public void onSuccess(VersionHttp data) {
                        vcodeServer = data.vcode;
                        upUrl = data.vurl;
                        if (versionCode >= vcodeServer) return;
                        DialogManager.getInstance().showTipDialog(MainActivity.this, "升级提示", "有新的版本哦,请更新~~", "确定", "取消", new DialogManager.Listener() {
                            @Override
                            public void onClickListener() {
                                if (TextUtils.isEmpty(upUrl)) {
                                    Toast.makeText(MainActivity.this, "下载地址有误", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                updateApp(upUrl);
                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {
                    }
                }));
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


    private void updateApp(String url) {
        File f = new File(ACCOUNT_DIR + APK_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        File downloadFile = new File(f.getAbsoluteFile() + "/" + System.currentTimeMillis() + "temp1.apk");
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("下载更新");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FileDownloader.getImpl().create(url)
                .setPath(downloadFile.getAbsolutePath(), false)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressDialog.setMessage("连接中...");
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressDialog.setProgress((int) (((float) soFarBytes / totalBytes) * 100));

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        progressDialog.setProgress(100);
                        progressDialog.dismiss();
                        if (downloadFile.exists()) {
                            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri uri = FileProvider.getUriForFile(MainActivity.this, "cn.looksafe.client.fileprovider", downloadFile);
                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                            } else {
                                intent.setDataAndType(Uri.parse("file://" + downloadFile.getAbsolutePath()), "application/vnd.android.package-archive");
                            }
                            MainActivity.this.startActivityForResult(intent, REQ_UPDATE);
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        progressDialog.dismiss();
                        ToastUtils.showShort("更新出错");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
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
