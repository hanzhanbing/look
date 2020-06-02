package com.look.core.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by huyg on 2020-01-19.
 */
public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {


    protected Activity mActivity;
    protected Context mContext;
    protected DB mBinding;
    protected CompositeDisposable mDisposable = new CompositeDisposable();
    private static final String TAG = "BaseFragment";

    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getClass().getSimpleName() + "  onCreateView");
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mDisposable.clear();
        Log.d(TAG, getClass().getSimpleName() + "  onDestroyView");
    }

    protected abstract int getLayoutId();

    protected abstract void init();



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, getClass().getSimpleName() + "  onStart");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, getClass().getSimpleName() + "  onPause");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, getClass().getSimpleName() + "  onStop");
    }

    @Subscribe
    public void onEvent(Object object) {
    }
}
