package cn.looksafe.client.activitys;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityGyBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class GyActivity extends BaseActivity {
    ActivityGyBinding binding;
    private boolean getAll;
    private ArrayList<VideosBean.MainVideo> listJxsp = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listHyzs = new ArrayList<>();
    private int selectType = -1;
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gy);
//        binding.agBack.setOnClickListener(this);
        binding.agJxsp.setOnClickListener(this);
        binding.agHyzs.setOnClickListener(this);
        binding.agToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getGyList(mContext, GyActivity.this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ag_back:
//                finish();
//                break;
            case R.id.ag_jxsp:
                selectType(1);
                break;
            case R.id.ag_hyzs:
                selectType(2);
                break;
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_GY_LIST_SUC:
                if (getAll) return;
                getAll = true;
                final VideosBean videosBean = (VideosBean) object;
                if (videosBean.mainVideosList != null) {
                    for (int i = 0; i < videosBean.mainVideosList.size(); i++) {
                        VideosBean.MainVideo mainVideo = videosBean.mainVideosList.get(i);
                        switch (mainVideo.vtypeid) {
                            case 40001://教学视频
                                listJxsp.add(mainVideo);
                                break;
                            case 40002://护眼知识
                                listHyzs.add(mainVideo);
                                break;
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            selectType(1);
                        }
                    });

                }
                break;
        }
    }

    private void selectType(int type) {
        if (selectType == type) return;
        selectType = type;
        switch (type) {
            case 1:
                videoListAdapter = new VideoListAdapter(listJxsp, mContext);
                break;
            case 2:
                videoListAdapter = new VideoListAdapter(listHyzs, mContext);
                break;
        }
        binding.agGrid.setAdapter(videoListAdapter);
        binding.agGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoListAdapter.openPlayActivity(position);
            }
        });
    }


}
