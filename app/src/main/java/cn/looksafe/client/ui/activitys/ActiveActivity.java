package cn.looksafe.client.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.activity.CaptureActivity;
import com.look.core.http.BaseResponse;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityActiveBinding;
import cn.looksafe.client.utils.Constant;
import cn.looksafe.client.viewmodel.ActiveViewModel;


public class ActiveActivity extends BaseActivity<ActivityActiveBinding> {
    private long backtime = 0;
    private ActiveViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(ActiveViewModel.class);
        mBinding.setPresenter(new Presenter());
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
            if (!TextUtils.isEmpty(scanResult)) {
                mBinding.code.setText(scanResult);
            }

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


    public class Presenter {
        public void scan() {
            startScan();
        }

        public void active() {
            String code = mBinding.code.getText().toString();
            if (TextUtils.isEmpty(code)) {
                toast("请填写或扫描激活码");
                return;
            }
            toActive(code);
        }
    }

    private void toActive(String code) {
        mViewModel.activeApp(SpManager.getInstance(mContext).getSP("phone"), code)
                .observe(this, resource -> resource.work(new ResourceListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        SpManager.getInstance(mContext).putSP("active", true);
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        toast(msg);
                    }
                }));
    }

}
