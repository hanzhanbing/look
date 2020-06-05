package cn.looksafe.client.ui.activitys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.look.core.manager.AppManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.util.StatusBarUtils;
import com.look.core.vo.ResourceListener;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.databinding.ActivityLoginBinding;
import cn.looksafe.client.viewmodel.UserViewModel;
import qiu.niorgai.StatusBarCompat;

@Route(path = "/user/login")
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private UserViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mBinding.password.setText("");
    }

    @Override
    protected void init() {
        mBinding.setPresenter(new Presenter());
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        initBar();
        initSp();
    }


    private void initBar() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color._FDB232));
        StatusBarUtils.setLightStatusBar(this, true, false);
    }

    private void initSp() {
        String phone = SpManager.getInstance(mContext).getSP("phone");
        if (!TextUtils.isEmpty(phone)) {
            mBinding.name.setText(phone);
            mBinding.name.setSelection(phone.length());
        }
    }

    private boolean checkLoginInfo(String phone, String pwd) {
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            ToastUtils.showShort("账号填写错误");
            return false;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            ToastUtils.showShort("密码填写错误");
            return false;
        }
        return true;
    }


    public class Presenter {

        public void forgetPwd() {
            startActivity(new Intent(mContext, ForgetActivity.class));
        }

        public void registerUser() {
            startActivity(new Intent(mContext, RegisterActivity.class));
        }

        public void login() {
            String phone = mBinding.name.getText().toString().trim();
            String pwd = mBinding.password.getText().toString().trim();
            if (checkLoginInfo(phone, pwd)) {
                doLogin(phone, pwd);
            }
        }
    }

    private void doLogin(String phone, String pwd) {
        mViewModel.login(phone, pwd).observe(this, resource -> resource.work(new ResourceListener<HttpBean>() {
            @Override
            public void onSuccess(HttpBean data) {
                SpManager.getInstance(mContext).putSP("token", data.getToken());
                SpManager.getInstance(mContext).putSP("phone", mBinding.name.getText().toString());
                SpManager.getInstance(mContext).putSP("vip", data.isIsvip());
                SpManager.getInstance(mContext).putSP("active", data.isIsactive());
                if (data.isIsactive()) { //被激活过
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    startActivity(new Intent(mContext, ActiveActivity.class));
                }
            }

            @Override
            public void onError(String msg) {
                toast(msg);
            }
        }));
    }

    @Override
    public void setActionBar() {
        mToolbar.setVisibility(View.GONE);
    }

    private long exitTime = 0;

    /**
     * 点击两次退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
