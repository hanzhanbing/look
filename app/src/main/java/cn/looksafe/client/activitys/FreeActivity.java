package cn.looksafe.client.activitys;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityFreeBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class FreeActivity extends BaseActivity {
    ActivityFreeBinding binding;
    private boolean getAll;
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free);
//        binding.afBack.setOnClickListener(this);
        binding.afToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getFreeList(mContext, FreeActivity.this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.af_back:
//                finish();
//                break;
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_FREE_LIST_SUC:
                getAll = true;
                final VideosBean videosBean = (VideosBean) object;
                if (videosBean.mainVideosList != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoListAdapter = new VideoListAdapter(videosBean.mainVideosList, mContext);
                            binding.afGrid.setAdapter(videoListAdapter);
                            binding.afGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    videoListAdapter.openPlayActivity(position);
                                }
                            });
                        }
                    });
                }
                break;
        }
    }

}
