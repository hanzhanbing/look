package cn.looksafe.client.ui.activitys;

import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.look.core.manager.SpManager;
import com.look.core.ui.SimpleActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivitySplashBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huyg on 2020-05-29.
 */
public class SplashActivity extends SimpleActivity<ActivitySplashBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        initPermission();
    }


    //    private void initProtocal() {
//        StringBuilder content = new StringBuilder();
//        content.append("感谢您使用匠神来运！为了帮助您安全使用产品和服务，在您同意并授权的基础上，我们可能会收集您的身份信息、联系信息、交易信息、位置信息等。请您务必仔细阅读并透彻理解");
//        content.append("<font color=\"#FFE304\"><a href=\"https://www.jiangshen56.com/ysxy.html\">《隐私政策》</a></font>");
//        content.append("和<font color=\"#FFE304\"><a href=\"https://www.jiangshen56.com/zcxy.html\">《用户协议》</a></font>");
//        if (SpManager.getInstance(mContext).getIntSP("protocal") == 0) {
//            AppDialogFragment appDialogFragment = AppDialogFragment.getInstance();
//            appDialogFragment.setTitle("用户协议与隐私保护声明");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                appDialogFragment.setMessage(Html.fromHtml(content.toString(), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                appDialogFragment.setMessage(Html.fromHtml(content.toString()));
//            }
//
//            appDialogFragment.setPositiveButton("同意并继续", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SpManager.getInstance(mContext).putIntSP("protocal", 1);
//                    initPermission();
//                }
//            });
//            appDialogFragment.setNegativeButton("不同意", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//            appDialogFragment.setCancel(false);
//            appDialogFragment.show(getSupportFragmentManager(), "appDialog");
//        } else {
//            initPermission();
//        }
//    }
    private void initPermission() {
        XXPermissions.with(this)
                .permission(
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE
                )
                .constantRequest()
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Observable.timer(3500, TimeUnit.MILLISECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long aLong) throws Exception {
                                            if (TextUtils.isEmpty(SpManager.getInstance(mContext).getSP("token"))) {
                                                ARouter.getInstance().build("/user/login").navigation();
                                            } else {
                                                if (SpManager.getInstance(mContext).getBooleanSp("active")) {
                                                    ARouter.getInstance().build("/ui/main").navigation();
                                                } else {
                                                    ARouter.getInstance().build("/user/login").navigation();
                                                }
                                            }
                                            finish();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        ToastUtils.showShort("请允许权限再使用");
                    }
                });
    }

    @Override
    public void setActionBar() {
        mToolbar.setVisibility(View.GONE);
    }

}
