package cn.looksafe.client.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.Contents;

public class BaseActivity extends Activity implements View.OnClickListener, HttpCallBack {
    protected Context mContext;
    protected boolean isRun;
    protected BitmapUtils mBitmapUtils;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBitmapUtils = new BitmapUtils(mContext);
        isRun = true;
        Contents.getInstance().addActivity(this);
        String activityname = mContext.getClass().getName();
        if (!activityname.contains("LoginActivity"))
            checkPermissions();

    }

    protected void checkPermissions() {
        // Android 6.0动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 111);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {

    }

    @Override
    public void requestFailure(int code, Object object) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
        Contents.getInstance().removeActivity(this);
    }

    protected void toast(final String tip, final int tl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (AppConfig.TOAST_LONG == tl) {
                    Toast.makeText(mContext, tip, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, tip, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
