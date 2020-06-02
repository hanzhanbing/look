package cn.looksafe.client.ui.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityModifyPwdBinding;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class ModifyPwdActivity extends BaseActivity {
    ActivityModifyPwdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_pwd);
//        binding.ampBack.setOnClickListener(this);
        binding.ampSub.setOnClickListener(this);
        binding.ampToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_RESET_PWD_SUC:
                toast("密码修改成功!", AppConfig.TOAST_SHORT);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.amp_back:
//                finish();
//                break;
            case R.id.amp_sub:
                checkPwd();
                break;
        }

    }

    private void checkPwd() {
        String old = binding.ampOld.getText().toString().trim();
        String n = binding.ampNew.getText().toString().trim();
        if (TextUtils.isEmpty(old) || TextUtils.isEmpty(n) || old.length() < 6 || n.length() < 6) {
            Toast.makeText(mContext, "密码输入有误", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpTools.resetPwd(mContext, ModifyPwdActivity.this, old, n);

    }

}
