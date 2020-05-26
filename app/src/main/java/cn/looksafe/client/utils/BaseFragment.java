package cn.looksafe.client.utils;

import android.os.Bundle;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import androidx.fragment.app.Fragment;
import cn.looksafe.client.tools.AppConfig;


public class BaseFragment extends Fragment implements View.OnClickListener, HttpCallBack {
    protected View view = null;
    protected BitmapUtils mBitmapUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBitmapUtils = new BitmapUtils(getActivity());
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void toast(final String tip, final int tl) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (AppConfig.TOAST_LONG == tl) {
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {

    }

    @Override
    public void requestFailure(int code, Object object) {
        switch (code) {
            case HttpStatus.HTTP_ERROR:
                String tip = object.toString();
                toast(tip, AppConfig.TOAST_SHORT);
                break;
        }
    }
}
