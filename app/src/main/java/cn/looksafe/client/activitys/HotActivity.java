package cn.looksafe.client.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.fragments.FragmentHome;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.databinding.ActivityHotBinding;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class HotActivity extends BaseActivity {
    ActivityHotBinding binding;
    private VideosBean hotVideos = null;//热门推荐
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hot);
        hotVideos = (VideosBean) getIntent().getSerializableExtra("hotVideos");
        if (hotVideos == null) {
            HttpTools.getHotList(mContext, HotActivity.this);
        }else showList();
        binding.ahToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_HOT_LIST_SUC:
                hotVideos = (VideosBean) object;
                showList();
                break;
        }

    }

    private void showList() {
        if (hotVideos.mainVideosList != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videoListAdapter = new VideoListAdapter(hotVideos.mainVideosList, mContext);
                    binding.ahGrid.setAdapter(videoListAdapter);
                    binding.ahGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            videoListAdapter.openPlayActivity(position);
                        }
                    });
                }
            });

        }
    }

    @Override
    public void requestFailure(int code, Object object) {
        super.requestFailure(code, object);
        toast("列表获取失败", AppConfig.TOAST_SHORT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }
}
