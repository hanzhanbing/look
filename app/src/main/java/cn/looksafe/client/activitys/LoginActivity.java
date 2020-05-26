package cn.looksafe.client.activitys;

import android.app.Dialog;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.databinding.ActivityLoginBinding;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.SPutil;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    private NormalModel normalModel = new NormalModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setNormalModel(normalModel);
        binding.alLogin.setOnClickListener(this);
        binding.alRegister.setOnClickListener(this);
        binding.alForget.setOnClickListener(this);
        binding.alXy.setOnClickListener(this);
        binding.alPro.setOnClickListener(null);
        String name = SPutil.getInstance(mContext).getLoginname();
        String pwd = SPutil.getInstance(mContext).getLoginpwd();
        binding.alName.setText(name);
        binding.alPwd.setText(pwd);
        //先检测是否首次安装
        if (SPutil.getInstance(mContext).getVersionCode() < Tools.getAppVersionCode(mContext)) {
            showDialXY();
        }
        //如果账号密码不为空，自动登录
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
            normalModel.setShowProgress(true);
            HttpTools.login(mContext, name, pwd, LoginActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.al_forget:
                startActivity(new Intent(mContext, ForgetActivity.class));
                break;
            case R.id.al_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.al_login:
                normalModel.setShowProgress(true);
                String phone = binding.alName.getText().toString();
                String pwd = binding.alPwd.getText().toString();
                if (checkLoginInfo(phone, pwd)) {
                    SPutil.getInstance(mContext).setLoginname(phone);
                    HttpTools.login(mContext, phone, pwd, LoginActivity.this);
                } else normalModel.setShowProgress(false);
                break;
            case R.id.al_xy:
                startActivity(new Intent(mContext, ProtocalActivity.class));
                break;
        }
    }


    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_LOGIN_SUC:
                SPutil.getInstance(mContext).setLoginpwd(binding.alPwd.getText().toString());
                HttpBean httpBean = (HttpBean) object;
                finish();
                if (httpBean.isactive) {//被激活过
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    startActivity(new Intent(mContext, ActiveActivity.class));
                }
                break;
        }
        normalModel.setShowProgress(false);
    }

    @Override
    public void requestFailure(int code, Object object) {
        super.requestFailure(code, object);
        String tip = object.toString();
        switch (code) {
            case HttpStatus.HTTP_ERROR:
                SPutil.getInstance(mContext).setLoginpwd("");
                toast(tip, AppConfig.TOAST_SHORT);
                break;
        }
        normalModel.setShowProgress(false);
    }

    private boolean checkLoginInfo(String phone, String pwd) {
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            Toast.makeText(mContext, "账号填写错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            Toast.makeText(mContext, "密码填写错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void showDialXY() {
        final Dialog dialog = new Dialog(mContext, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dial_xy);
        WebView webView = dialog.findViewById(R.id.dx_xy);
        Button okbtn = dialog.findViewById(R.id.dx_ok);
        webView.loadUrl("file:///android_asset/web/register.html");
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPutil.getInstance(mContext).setVersionCode(Tools.getAppVersionCode(mContext));
                dialog.dismiss();
                checkPermissions();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}
