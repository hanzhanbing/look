package cn.looksafe.client.activitys;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityQsBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class QsActivity extends BaseActivity {
    ActivityQsBinding binding;
    private boolean getAll;
    private int selectType = -1;
    private ArrayList<VideosBean.MainVideo> listDm = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listYs = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listJlp = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listZy = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listZs = new ArrayList<>();
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qs);
//        binding.aqBack.setOnClickListener(this);
        binding.aqDm.setOnClickListener(this);
        binding.aqYs.setOnClickListener(this);
        binding.aqJlp.setOnClickListener(this);
        binding.aqZy.setOnClickListener(this);
        binding.aqZs.setOnClickListener(this);
        binding.aqToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getQsList(mContext, QsActivity.this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.aq_back:
//                finish();
//                break;
            case R.id.aq_dm:
                selectType(1);
                break;
            case R.id.aq_ys:
                selectType(2);
                break;
            case R.id.aq_jlp:
                selectType(3);
                break;
            case R.id.aq_zy:
                selectType(4);
                break;
            case R.id.aq_zs:
                selectType(5);
                break;
        }
    }

    private void selectType(int type) {
        if (selectType == type) return;
        selectType = type;
        switch (type) {
            case 1:
                videoListAdapter = new VideoListAdapter(listDm, mContext);
                break;
            case 2:
                videoListAdapter = new VideoListAdapter(listYs, mContext);
                break;
            case 3:
                videoListAdapter = new VideoListAdapter(listJlp, mContext);
                break;
            case 4:
                videoListAdapter = new VideoListAdapter(listZy, mContext);
                break;
            case 5:
                videoListAdapter = new VideoListAdapter(listZs, mContext);
                break;
        }
        binding.aqGrid.setAdapter(videoListAdapter);
        binding.aqGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoListAdapter.openPlayActivity(position);
            }
        });
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_QS_LIST_SUC:
                getAll = true;
                final VideosBean videosBean = (VideosBean) object;
                if (videosBean.mainVideosList != null) {
                    for (int i = 0; i < videosBean.mainVideosList.size(); i++) {
                        VideosBean.MainVideo mainVideo = videosBean.mainVideosList.get(i);
                        switch (mainVideo.vtypeid) {
                            case 20001://动漫
                                listDm.add(mainVideo);
                                break;
                            case 20002://影视
                                listYs.add(mainVideo);
                                break;
                            case 20003://纪录片
                                listJlp.add(mainVideo);
                                break;
                            case 20004://综艺
                                listZy.add(mainVideo);
                                break;
                            case 20005://知识
                                listZs.add(mainVideo);
                                break;

                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            selectType(1);
                        }
                    });
                    break;
                }
        }
    }
}
