package cn.looksafe.client.ui.activitys;

import com.look.core.BuildConfig;
import com.look.core.ui.SimpleActivity;
import com.look.core.util.StatusBarUtils;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityProtocalBinding;
import qiu.niorgai.StatusBarCompat;

public class ProtocalActivity extends SimpleActivity<ActivityProtocalBinding> {


    @Override
    public void setActionBar() {
        mTitle.setText("用户协议");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocal;
    }

    @Override
    protected void init() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(com.look.core.R.color.colorPrimary));
        StatusBarUtils.setLightStatusBar(this, true, false);
        if (BuildConfig.branch == 0) {
            mBinding.rWeb.loadUrl("file:///android_asset/web/register.html");
        }else {
            mBinding.rWeb.loadUrl("file:///android_asset/web/register_xincao.html");
        }

    }
}
