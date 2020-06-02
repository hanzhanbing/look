package com.look.core.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.look.core.R;
import com.look.core.manager.AppManager;
import com.look.core.util.StatusBarUtils;

import io.reactivex.disposables.CompositeDisposable;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by huyg on 2020-01-19.
 */
public abstract class ToolbarActivity extends AppCompatActivity {


    private FrameLayout containerLayout = null;
    protected Toolbar mToolbar;
    protected TextView mTitle;
    protected Activity mContext;
    protected ViewGroup mContentView;
    protected CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        StatusBarUtils.setLightStatusBar(this, true, false);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_base);
        //StatusBarUtils.transparencyBar(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (R.layout.activity_base == layoutResID) {
            super.setContentView(R.layout.activity_base);
            containerLayout = findViewById(R.id.layout_center);
            mContentView = (ViewGroup) findViewById(android.R.id.content);
            mToolbar = findViewById(R.id.base_toolbar);
            mToolbar.setTitle("");
            mToolbar.setSubtitle("");
            mTitle = findViewById(R.id.toolbar_title);
            containerLayout.removeAllViews();
        } else {
            View contentView = LayoutInflater.from(this).inflate(layoutResID, containerLayout,false);
            containerLayout.setId(android.R.id.content);
            mContentView.setId(View.NO_ID);
            containerLayout.removeAllViews();
            containerLayout.addView(contentView);
            mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backAction();
                }
            });
            setActionBar();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction();
    }

    public void backAction() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        AppManager.getAppManager().finishActivity(this);
    }

    public abstract void setActionBar();

}
