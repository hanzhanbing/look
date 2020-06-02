package cn.looksafe.client.ui.activitys;

import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.look.core.manager.SpManager;
import com.look.core.ui.SimpleActivity;

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

        Observable.timer(4000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (TextUtils.isEmpty(SpManager.getInstance(mContext).getSP("token"))) {
                            ARouter.getInstance().build("/user/login").navigation();
                        } else {
                            ARouter.getInstance().build("/ui/main").navigation();
                        }
                        finish();
                    }
                });
    }


    @Override
    public void setActionBar() {
        mToolbar.setVisibility(View.GONE);
    }
}
