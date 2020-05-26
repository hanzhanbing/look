package cn.looksafe.client.activitys;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityKuaiBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class KuaiActivity extends BaseActivity {
    ActivityKuaiBinding binding;
    private boolean getAll;
    private int selectType = -1;
    private ArrayList<VideosBean.MainVideo> listEng = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listYuwen = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listLishi = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listKexue = new ArrayList<>();
    private ArrayList<VideosBean.MainVideo> listShuxue = new ArrayList<>();
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kuai);
//        binding.akBack.setOnClickListener(this);
        binding.akEng.setOnClickListener(this);
        binding.akYuwen.setOnClickListener(this);
        binding.akLishi.setOnClickListener(this);
        binding.akKexue.setOnClickListener(this);
        binding.akShuxue.setOnClickListener(this);
        binding.akToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getKuaiList(mContext, KuaiActivity.this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ak_back:
//                finish();
//                break;
            case R.id.ak_eng:
                selectType(1);
                break;
            case R.id.ak_yuwen:
                selectType(2);
                break;
            case R.id.ak_lishi:
                selectType(3);
                break;
            case R.id.ak_kexue:
                selectType(4);
                break;
            case R.id.ak_shuxue:
                selectType(5);
                break;
        }
    }

    private void selectType(int type) {
        if (selectType == type) return;
        selectType = type;
        binding.akEng.setSelected(false);
        binding.akYuwen.setSelected(false);
        binding.akLishi.setSelected(false);
        binding.akKexue.setSelected(false);
        binding.akShuxue.setSelected(false);
        switch (type) {
            case 1:
                binding.akEng.setSelected(true);
                videoListAdapter = new VideoListAdapter(listEng, mContext);
                break;
            case 2:
                binding.akYuwen.setSelected(true);
                videoListAdapter = new VideoListAdapter(listYuwen, mContext);
                break;
            case 3:
                binding.akLishi.setSelected(true);
                videoListAdapter = new VideoListAdapter(listLishi, mContext);
                break;
            case 4:
                binding.akKexue.setSelected(true);
                videoListAdapter = new VideoListAdapter(listKexue, mContext);
                break;
            case 5:
                binding.akShuxue.setSelected(true);
                videoListAdapter = new VideoListAdapter(listShuxue, mContext);
                break;
        }
        binding.akGrid.setAdapter(videoListAdapter);
        binding.akGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoListAdapter.openPlayActivity(position);
            }
        });
    }


    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_KUAI_LIST_SUC:
                getAll = true;
                final VideosBean videosBean = (VideosBean) object;
                if (videosBean.mainVideosList != null) {
                    for (int i = 0; i < videosBean.mainVideosList.size(); i++) {
                        VideosBean.MainVideo mainVideo = videosBean.mainVideosList.get(i);
                        switch (mainVideo.vtypeid) {
                            case 10001://英语
                                listEng.add(mainVideo);
                                break;
                            case 10002://语文
                                listYuwen.add(mainVideo);
                                break;
                            case 10003://历史
                                listLishi.add(mainVideo);
                                break;
                            case 10004://科学
                                listKexue.add(mainVideo);
                                break;
                            case 10005://数学
                                listShuxue.add(mainVideo);
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
        super.requestSuccess(code, object, object2);
    }
}
