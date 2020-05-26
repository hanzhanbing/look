package cn.looksafe.client.activitys;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.RegisterBinding;
import cn.looksafe.client.utils.BaseActivity;

public class ProtocalActivity extends BaseActivity {
    RegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.register);
        binding.rToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.rWeb.loadUrl("file:///android_asset/web/register.html");
    }
}
