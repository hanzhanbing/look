package cn.looksafe.client.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import java.io.File;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.databinding.ActivitySettingBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.ui.activitys.ProtocalActivity;
import cn.looksafe.client.utils.HttpCallBack;
import cn.looksafe.client.viewmodel.SettingViewModel;

@Route(path = "/setting/setting")
public class SysActivity extends BaseActivity<ActivitySettingBinding> implements HttpCallBack {
    private boolean getVersion;//是否已获取到服务器最新版本信息
    private int versionCode;
    private String versionName;
    private String upUrl;//升级url
    private int vcodeServer;//最新版本号
    private SettingViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        versionCode = Integer.parseInt(Tools.getAppVersionInfo(mContext).split(",")[1]);
        versionName = Tools.getAppVersionInfo(mContext).split(",")[0];
        mBinding.version.setText("当前版本: " + versionName);
        mBinding.cache.setText(Tools.getCacheSize());//缓存大小
        mBinding.setPresenter(new Presenter());
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
    }

    private void getVersion() {
        mViewModel.checkVersion(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VersionHttp>() {
                    @Override
                    public void onSuccess(VersionHttp data) {
                        vcodeServer = data.vcode;
                        upUrl = data.vurl;
                        if (versionCode >= vcodeServer) return;
                        commonDialog("升级提示", "升级到最新版?", 1);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {

    }

    @Override
    public void requestFailure(int code, Object object) {

    }

    public class Presenter {
        public void exit() {
            SpManager.getInstance(mContext).removeSP("token");
        }

        public void resetPwd() {

        }

        public void cleanCache() {
            commonDialog("清除提示", "清除缓存?", 2);
        }

        public void reportSug() {
            startActivity(new Intent(mContext, MySuggestActivity.class));
        }

        public void checkVersion() {
            getVersion();

        }

        public void goProtocal() {
            startActivity(new Intent(mContext, ProtocalActivity.class));
        }
    }

    private void commonDialog(String title, String con, final int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SysActivity.this);
        builder.setTitle(title);
        builder.setIcon(R.mipmap.app_logo);
        builder.setMessage(con);
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                switch (type) {
                    case 1://进行升级
                        if (TextUtils.isEmpty(upUrl)) {
                            Toast.makeText(mContext, "下载地址有误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        HttpTools.downApk(upUrl, Tools.getNameFromURL(upUrl), mContext, null, SysActivity.this);
                        break;
                    case 2://清除缓存
                        Tools.cleanAllCache(mContext);
                        Toast.makeText(mContext, "清除成功", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.create().show();

    }

    private void installApk(String downloadApkPath) {
        File apkFile = new File(downloadApkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mContext.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void setActionBar() {
        mTitle.setText("系统设置");
    }
}
