package com.look.core.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ToastUtils;
import com.look.core.R;
import com.look.core.bean.ResourceStatus;
import com.look.core.util.StatusBarUtils;
import com.look.core.widget.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import qiu.niorgai.StatusBarCompat;


/**
 * Created by huyg on 2020-01-19.
 */
public abstract class BaseActivity<DB extends ViewDataBinding> extends ToolbarActivity implements IBaseView {

    protected DB mBinding;
    private CircleProgressDialog mDialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color._FDB232));
        StatusBarUtils.setLightStatusBar(this, true, false);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setActionBar() {

    }


    protected abstract int getLayoutId();

    protected abstract void init();



    @Override
    public void showProgress() {
        if (mDialog == null) {
            mDialog = new CircleProgressDialog(this);
        }
        mDialog.setCanceledOnTouchOutside(true);
        if (!this.isFinishing()) {
            mDialog.show();
        }
    }

    @Override
    public void closeProgress() {
        if (mDialog != null && mDialog.isShowing() && !this.isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void toast(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), "onStart");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(ResourceStatus event) {
        switch (event.status) {

            case LOADING:
                showProgress();
                Log.d(getClass().getSimpleName(), "LOADING");
                break;
            case ERROR:
                Log.d(getClass().getSimpleName(), "SUCCESS ERROR");
            case SUCCESS:
                closeProgress();
                break;
        }
    }

}
