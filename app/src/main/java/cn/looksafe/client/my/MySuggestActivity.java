package cn.looksafe.client.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.MySugBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;

public class MySuggestActivity extends BaseActivity {
    MySugBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_sug);
//        binding.msBack.setOnClickListener(this);
        binding.msSub.setOnClickListener(this);
        binding.msToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
//            case R.id.ms_back:
//                finish();
//                break;
            case R.id.ms_sub:
                String con = binding.msEdit.getText().toString().trim();
                if (con.length() < 1) return;
                HttpTools.suggest(mContext, MySuggestActivity.this, con);
                break;
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_SUGGEST_SUC:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "提交成功", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                break;
        }
    }
}
