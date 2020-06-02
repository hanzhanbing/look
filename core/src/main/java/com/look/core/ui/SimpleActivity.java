package com.look.core.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


/**
 * Created by huyg on 2020-02-13.
 */
public abstract class SimpleActivity<DB extends ViewDataBinding> extends ToolbarActivity {


    protected DB mBinding;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected abstract int getLayoutId();

    protected abstract void init();


}
