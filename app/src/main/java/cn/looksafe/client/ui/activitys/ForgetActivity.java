package cn.looksafe.client.ui.activitys;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.look.core.http.BaseResponse;
import com.look.core.ui.BaseActivity;
import com.look.core.util.StatusBarUtils;
import com.look.core.vo.Resource;
import com.look.core.vo.ResourceListener;

import java.util.concurrent.TimeUnit;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityForgetBinding;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.viewmodel.UserViewModel;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import qiu.niorgai.StatusBarCompat;

public class ForgetActivity extends BaseActivity<ActivityForgetBinding> {

    private UserViewModel mViewModel;
    private Disposable mDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mBinding.setPresenter(new Presenter());
    }


    public class Presenter{
        public void sendCode(){
            getSmsCode();
        }

        public void resetPwd(){
            resetLogin();
        }
    }

    private void resetLogin() {
        //检测内容是否填写完毕
        String phone = mBinding.phone.getText().toString();
        String code = mBinding.code.getText().toString();
        String pwd = mBinding.password.getText().toString();
        String pwdAgain = mBinding.pwdAgain.getText().toString();
        if (phone.length() < 11) {
            toast("手机号格式不正确");
            return;
        }
        if (code.length() < 6) {
            toast("请填写验证码");
            return;
        }
        if (pwd.length() < 6) {
            toast("密码长度不够");
            return;
        }
        if (pwdAgain.equals(pwd)){
            toast("密码不一致");
            Toast.makeText(mContext, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.resetPwd(phone, pwd, code)
                .observe(this, resource -> resource.work(new ResourceListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        toast("重置成功");
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        toast(msg);
                    }
                }));
    }

    private void getSmsCode() {
        //先检验手机号有没有填写
        String phone = mBinding.phone.getText().toString();
        if (phone == null || phone.length() < 11) {
            Toast.makeText(mContext, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.getCode(phone).observe(this, new Observer<Resource<BaseResponse>>() {
            @Override
            public void onChanged(Resource<BaseResponse> baseResponseResource) {
                mDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                if (!isFinishing()) {
                                    mBinding.sendCode.setText(String.format("%d秒", 60 - aLong));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (!isFinishing()) {
                                    mBinding.sendCode.setClickable(true);
                                    mBinding.sendCode.setText("重新获取");
                                }
                            }
                        }).subscribe();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
