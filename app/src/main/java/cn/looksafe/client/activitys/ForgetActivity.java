package cn.looksafe.client.activitys;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.databinding.ActivityForgetBinding;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class ForgetActivity extends BaseActivity {
    ActivityForgetBinding binding;
    private NormalModel normalModel = new NormalModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        binding.setNormalModel(normalModel);
//        binding.afBack.setOnClickListener(this);
        binding.afPro.setOnClickListener(null);
        binding.afGetcode.setOnClickListener(this);
        binding.afReset.setOnClickListener(this);
        normalModel.setTip1("获取验证码");
        binding.afToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.af_back:
//                finish();
//                break;
            case R.id.af_getcode:
                if (normalModel.getTip1().length() < 5) return;
                getSmsCode();
                break;
            case R.id.af_reset:
                resetLogin();
                break;

        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        normalModel.setShowProgress(false);
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
        normalModel.setShowProgress(false);
    }

    private void resetLogin() {
        //检测内容是否填写完毕
        String phone = binding.afName.getText().toString();
        String code = binding.afCode.getText().toString();
        String pwd = binding.afPwd.getText().toString();
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
        normalModel.setShowProgress(true);
        HttpTools.loginNewPwd(mContext, phone, pwd, code, ForgetActivity.this);

    }

    private void getSmsCode() {
        //先检验手机号有没有填写
        String phone = binding.afName.getText().toString();
        if (phone == null || phone.length() < 11) {
            Toast.makeText(mContext, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        normalModel.setShowProgress(true);
        HttpTools.getSmsCodeUser(mContext, phone, ForgetActivity.this);
    }

    private class ThreadCount extends Thread {
        @Override
        public void run() {
            super.run();
            int count = 60;
            while (isRun && count > 0) {
                normalModel.setTip1(count + "S");
                SystemClock.sleep(1000);
                count--;
            }
            normalModel.setTip1("获取验证码");
        }
    }
}
