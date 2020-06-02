package cn.looksafe.client.ui.activitys;

import com.look.core.ui.SimpleActivity;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityProtocalBinding;

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
        mBinding.rWeb.loadUrl("file:///android_asset/web/register.html");
    }
}
