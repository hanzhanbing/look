package cn.looksafe.client.my;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.MyAddrBinding;
import cn.looksafe.client.databinding.MyOrderBinding;
import cn.looksafe.client.utils.BaseActivity;

public class MyAddrActivity extends BaseActivity {
    MyAddrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_addr);
//        binding.maBack.setOnClickListener(this);
        binding.maToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ma_back:
//                finish();
//                break;
        }
    }
}
