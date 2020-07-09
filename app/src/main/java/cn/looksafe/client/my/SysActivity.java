package cn.looksafe.client.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import java.io.File;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.databinding.ActivitySettingBinding;
import cn.looksafe.client.manager.DialogManager;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.ui.activitys.ProtocalActivity;
import cn.looksafe.client.viewmodel.SettingViewModel;

@Route(path = "/setting/setting")
public class SysActivity extends BaseActivity<ActivitySettingBinding> {
    private boolean getVersion;//是否已获取到服务器最新版本信息
    private int versionCode;
    private String versionName;
    private String upUrl;//升级url
    private int vcodeServer;//最新版本号
    private SettingViewModel mViewModel;
    private static final int REQ_UPDATE = 999;

    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APK_PATH = "/apk_cache";

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
        getVersion();
    }

    private void getVersion() {
        mViewModel.checkVersion(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VersionHttp>() {
                    @Override
                    public void onSuccess(VersionHttp data) {
                        vcodeServer = data.vcode;
                        upUrl = data.vurl;
                        if (versionCode >= vcodeServer){
                            mBinding.point.setVisibility(View.GONE);
                        }else {
                            mBinding.point.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        toast(msg);
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public class Presenter {
        public void exit() {
            SpManager.getInstance(mContext).removeSP("token");
            ARouter.getInstance().build("/user/login").navigation();
        }

        public void resetPwd() {
            ARouter.getInstance().build("/user/modifyPwd").navigation();
        }

        public void cleanCache() {
            DialogManager.getInstance().showTipDialog(SysActivity.this, "温馨提示", "清除缓存?", "清除", "取消", new DialogManager.Listener() {
                @Override
                public void onClickListener() {
                    Tools.cleanAllCache(mContext);
                    mBinding.cache.setText(Tools.getCacheSize());//缓存大小
                    toast("清除成功");
                }
            });
        }

        public void reportSug() {
            startActivity(new Intent(mContext, MySuggestActivity.class));
        }

        public void checkVersion() {
            if (versionCode >= vcodeServer){
                toast("暂无新版本");
                return;
            }
            DialogManager.getInstance().showTipDialog(SysActivity.this, "升级提示", "升级到最新版?", "确定", "取消", new DialogManager.Listener() {
                @Override
                public void onClickListener() {
                    if (TextUtils.isEmpty(upUrl)) {
                        Toast.makeText(mContext, "下载地址有误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateApp(upUrl);
                }
            });
        }

        public void goProtocal() {
            startActivity(new Intent(mContext, ProtocalActivity.class));
        }
    }


    @Override
    public void setActionBar() {
        mTitle.setText("系统设置");
    }

    private void updateApp(String url) {
        File f = new File(ACCOUNT_DIR + APK_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        File downloadFile = new File(f.getAbsoluteFile() + "/" + System.currentTimeMillis() + "temp1.apk");
        final ProgressDialog progressDialog = new ProgressDialog(SysActivity.this);
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
                                Uri uri = FileProvider.getUriForFile(SysActivity.this, getPackageName()+".fileprovider", downloadFile);
                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                            } else {
                                intent.setDataAndType(Uri.parse("file://" + downloadFile.getAbsolutePath()), "application/vnd.android.package-archive");
                            }
                            SysActivity.this.startActivityForResult(intent, REQ_UPDATE);
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
}
