package cn.looksafe.client.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActiveActivityBinding;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.Constant;
import cn.looksafe.client.utils.HttpStatus;

public class ActiveActivity extends BaseActivity {
    ActiveActivityBinding binding;
    private NormalModel normalModel = new NormalModel();
    private long backtime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.active_activity);
        binding.setNormalModel(normalModel);
        binding.aaLogin.setOnClickListener(this);
        binding.aaScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.aa_scan:
                startScan();
                break;
            case R.id.aa_login:
                activeLogin();
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

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_ACTIVE_APP_SUC:
                finish();
                startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
        normalModel.setShowProgress(false);
    }

    private void activeLogin() {
        String str = binding.aaCode.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(mContext, "请填写或扫描激活码", Toast.LENGTH_SHORT).show();
            return;
        }
        normalModel.setShowProgress(true);
        HttpTools.activeApp(mContext, str, ActiveActivity.this);
    }


    private void startScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(mContext, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
            }
            // 申请权限
            ActivityCompat.requestPermissions(ActiveActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
//            tvResult.setText(scanResult);
            Log.d("xxx", scanResult + "    <-----扫描");
            if (!TextUtils.isEmpty(scanResult))
                binding.aaCode.setText(scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startScan();
                } else {
                    // 被禁止授权
                    Toast.makeText(mContext, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://返回
                if (System.currentTimeMillis() - backtime < 2000) {
                    finish();
                } else {
                    backtime = System.currentTimeMillis();
                }
                break;
        }
        return false;
    }

}
