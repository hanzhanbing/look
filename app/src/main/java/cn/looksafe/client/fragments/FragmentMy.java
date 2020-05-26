package cn.looksafe.client.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.FgMyBinding;
import cn.looksafe.client.my.EyeLogActivity;
import cn.looksafe.client.my.MyAddrActivity;
import cn.looksafe.client.my.MyOrderActivity;
import cn.looksafe.client.my.SysActivity;
import cn.looksafe.client.my.VipActivity;
import cn.looksafe.client.tools.Contents;
import cn.looksafe.client.utils.BaseFragment;

public class FragmentMy extends BaseFragment {
    FgMyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FgMyBinding.inflate(inflater);
//        if (view == null) {
        view = binding.getRoot();
        binding.fmSljl.setOnClickListener(this);
        binding.fmXtsz.setOnClickListener(this);
        binding.fmWddz.setOnClickListener(this);
        binding.fmMddd.setOnClickListener(this);
        binding.fmVip.setOnClickListener(this);
//        }
        binding.fmAccount.setText(Contents.getInstance().getLoginname());
        return view;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fm_vip:
//                Toast.makeText(getActivity(), "敬请期待!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.fm_mddd://我的订单
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.fm_sljl://视力记录
                startActivity(new Intent(getActivity(), EyeLogActivity.class));
                break;
            case R.id.fm_wddz://我的地址
                startActivity(new Intent(getActivity(), MyAddrActivity.class));
                break;
            case R.id.fm_xtsz://系统设置
                startActivity(new Intent(getActivity(), SysActivity.class));
                break;
        }
    }
}
