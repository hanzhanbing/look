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
import cn.looksafe.client.databinding.ActivityRegisterBinding;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding binding;
    private NormalModel normalModel = new NormalModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setNormalModel(normalModel);
        normalModel.setTip1("获取验证码");
        binding.arGetcode.setOnClickListener(this);
//        binding.arBack.setOnClickListener(this);
        binding.arLinkFile.setOnClickListener(this);
        binding.arRegister.setOnClickListener(this);
        binding.arPro.setOnClickListener(null);
        binding.arToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
//            case R.id.ar_back:
//                finish();
//                break;
            case R.id.ar_getcode:
                if (normalModel.getTip1().length() < 5) return;
                getSmsCode();
                break;
            case R.id.ar_register:
                registerLogin();
                break;
            case R.id.ar_link_file:
                startActivity(new Intent(mContext, ProtocalActivity.class));
                break;
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_SMS_SUC:
                toast("验证码已发送", AppConfig.TOAST_SHORT);
                new ThreadCount().start();
                break;
            case HttpStatus.HTTP_REGISTER_LOGIN_SUC:
                toast("注册成功", AppConfig.TOAST_LONG);
                finish();
//                HttpBean httpBean = (HttpBean) object;
//                finish();
//                if (httpBean.isactive) {//被激活过
//                    startActivity(new Intent(mContext, MainActivity.class));
//                } else {
//                    startActivity(new Intent(mContext, ActiveActivity.class));
//                }
                break;
        }
        normalModel.setShowProgress(false);
    }

    @Override
    public void requestFailure(int code, Object object) {
        super.requestFailure(code, object);
        String tip = object.toString();
        toast(tip, AppConfig.TOAST_SHORT);
        normalModel.setShowProgress(false);
    }


    private void registerLogin() {
        //检测内容是否填写完毕
        String phone = binding.arName.getText().toString();
        String code = binding.arCode.getText().toString();
        String pwd = binding.arPwd.getText().toString();
        CheckBox cb = binding.arCk;
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
        if (!cb.isChecked()) {
            Toast.makeText(mContext, "请仔细阅读注册协议，同意请打勾", Toast.LENGTH_SHORT).show();
            return;
        }
        normalModel.setShowProgress(true);
        HttpTools.registerLogin(mContext, phone, pwd, code, RegisterActivity.this);

    }


    private void getSmsCode() {
        //先检验手机号有没有填写
        String phone = binding.arName.getText().toString();
        if (phone == null || phone.length() < 11) {
            Toast.makeText(mContext, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        normalModel.setShowProgress(true);
        HttpTools.getSmsCode(mContext, phone, RegisterActivity.this);
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
