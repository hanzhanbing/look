package com.look.core.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.look.core.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Create by Weixf
 * Date on 2017/7/14
 * Description loading加载框
 */
public class CircleProgressDialog extends AlertDialog {
    private AVLoadingIndicatorView mIndicator;

    private Context mContext;



    public CircleProgressDialog(Context context) {
        super(context, R.style.dialog_loading);
        mContext = context;
    }

    public CircleProgressDialog(Context context, String text) {
        super(context, R.style.dialog_loading);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mIndicator = findViewById(R.id.view_indicator);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount=0f;
        window.setAttributes(lp);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (mIndicator != null && mIndicator.isShown()) {
            mIndicator.hide();
        }
    }

}
