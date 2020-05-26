package cn.looksafe.client.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.activitys.ProtocalActivity;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.databinding.MeSysBinding;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.Contents;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.SPutil;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class SysActivity extends BaseActivity {
    MeSysBinding binding;
    NormalModel normalModel = new NormalModel();
    private boolean getVersion;//是否已获取到服务器最新版本信息
    private int versionCode;
    private String versionName;
    private String upUrl;//升级url
    private int vcodeServer;//最新版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.me_sys);
        binding.setNormalModel(normalModel);
//        binding.msBack.setOnClickListener(this);
        binding.msExit.setOnClickListener(this);
        binding.msResetPwd.setOnClickListener(this);
        binding.msCleanCache.setOnClickListener(this);
        binding.msReportSug.setOnClickListener(this);
        binding.msVersion.setOnClickListener(this);
        binding.msXieyi.setOnClickListener(this);
        binding.msPro.setOnClickListener(null);
        versionCode = Integer.parseInt(Tools.getAppVersionInfo(mContext).split(",")[1]);
        versionName = Tools.getAppVersionInfo(mContext).split(",")[0];
        normalModel.setTip1("当前版本: " + versionName);
        normalModel.setTip3(Tools.getCacheSize());//缓存大小
        binding.msToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getVersion) HttpTools.getVersion(mContext, SysActivity.this);
    }

    @Override
    public void requestFailure(int code, Object object) {
        super.requestFailure(code, object);
        String tip = (String) object;
        toast(tip, AppConfig.TOAST_SHORT);
        normalModel.setShowProgress(false);
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_VERSION_SUC:
                //进行版本对比
                VersionHttp versionHttp = (VersionHttp) object;
                vcodeServer = versionHttp.vcode;
                String n = versionHttp.vname;
                upUrl = versionHttp.vurl;
                if (vcodeServer > versionCode) {
                    normalModel.setShowRedPoint(true);
                }
                break;
            case HttpStatus.HTTP_DOWN_APK_SUC:
                String apkUri = (String) object;
                normalModel.setShowProgress(false);
                installApk(apkUri);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ms_back:
//                finish();
//                break;
            case R.id.ms_exit:
                //清空账号，退出
                SPutil.getInstance(mContext).setLoginpwd("");
                Contents.getInstance().exit();
                break;
            case R.id.ms_reset_pwd:
                startActivity(new Intent(mContext, ModifyPwdActivity.class));
                break;
            case R.id.ms_clean_cache://清除缓存
//                Toast.makeText(mContext, "清除成功", Toast.LENGTH_SHORT).show();
                commonDialog("清除提示", "清除缓存?", 2);
                break;
            case R.id.ms_report_sug:
                startActivity(new Intent(mContext, MySuggestActivity.class));
                break;
            case R.id.ms_version:
                //是否进行升级
                if (versionCode >= vcodeServer) return;
                commonDialog("升级提示", "升级到最新版?", 1);
                break;
            case R.id.ms_xieyi:
                startActivity(new Intent(mContext, ProtocalActivity.class));
                break;

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
                        normalModel.setShowProgress(true);
                        HttpTools.downApk(upUrl, Tools.getNameFromURL(upUrl), mContext, normalModel, SysActivity.this);
                        break;
                    case 2://清除缓存
                        Tools.cleanAllCache(mContext);
                        normalModel.setTip3("");
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
}
