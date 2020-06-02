package cn.looksafe.client.ui.activitys;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityForgetBinding;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class ForgetActivity extends BaseActivity {
    ActivityForgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        binding.setPresenter(new Presenter());
    }


    public class Presenter{
        public void sendCode(){
            getSmsCode();
        }

        public void resetPwd(){
            resetLogin();
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_SMS_USER_SUC:
                toast("验证码已发送", AppConfig.TOAST_SHORT);
                new ThreadCount().start();
                break;
            case HttpStatus.HTTP_LOGIN_USER_SUC:
                toast("密码修改成功", AppConfig.TOAST_LONG);
                finish();
                break;

        }
    }

    @Override
    public void requestFailure(int code, Object object) {
        super.requestFailure(code, object);
        String tip = object.toString();
        toast(tip, AppConfig.TOAST_SHORT);
    }

    private void resetLogin() {
        //检测内容是否填写完毕
        String phone = binding.phone.getText().toString();
        String code = binding.code.getText().toString();
        String pwd = binding.password.getText().toString();
        String pwdAgain = binding.pwdAgain.getText().toString();
        if (phone.length() < 11) {
            Toast.makeText(mContext, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code.length() < 6) {
            Toast.makeText(mContext, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6) {
            Toast.makeText(mContext, "密码长度不够", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwdAgain.equals(pwd)){
            Toast.makeText(mContext, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpTools.loginNewPwd(mContext, phone, pwd, code, ForgetActivity.this);

    }

    private void getSmsCode() {
        //先检验手机号有没有填写
        String phone = binding.phone.getText().toString();
        if (phone == null || phone.length() < 11) {
            Toast.makeText(mContext, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpTools.getSmsCodeUser(mContext, phone, ForgetActivity.this);
    }

    private class ThreadCount extends Thread {
        @Override
        public void run() {
            super.run();
            int count = 60;
            while (isRun && count > 0) {
                binding.sendCode.setText(count + "S");
                SystemClock.sleep(1000);
                count--;
            }
            binding.sendCode.setText("获取验证码");
        }
    }
}
