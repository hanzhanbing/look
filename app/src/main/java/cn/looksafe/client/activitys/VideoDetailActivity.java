package cn.looksafe.client.activitys;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unity3d.player.UnityPlayerActivity;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.tools.Contents;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.databinding.ActivityDetailBinding;
import cn.looksafe.client.utils.HttpStatus;

public class VideoDetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    VideosBean.MainVideo mainVideo;
    private int selectIndex = -1;
    private JiAdapter mJiAdapter;
    private boolean plus;//是否已上报

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mainVideo = (VideosBean.MainVideo) getIntent().getSerializableExtra("video");
//        binding.adBack.setOnClickListener(this);
        binding.adPlay.setOnClickListener(this);
        initView();
        HttpTools.getVideosByMainId(mContext, VideoDetailActivity.this, mainVideo.vid);
    }


    private void initView() {
        mBitmapUtils.display(binding.adPic, mainVideo.vimg);
        if (mainVideo.isvip == 1) binding.adVipTag.setVisibility(View.VISIBLE);
        binding.adPlaytimes.setText(mainVideo.playtimes + "次");
        binding.adTypeName.setText(mainVideo.vtypename);
        binding.adName.setText(mainVideo.vname);
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) binding.adGrid.getLayoutParams();
        if (mainVideo.totalnum > 5)
            linearParams.width = 200 * mainVideo.totalnum;
        else
            linearParams.width = 1000;
        linearParams.height = 200;
        binding.adGrid.setLayoutParams(linearParams);
        binding.adGrid.setNumColumns(mainVideo.totalnum > 5 ? mainVideo.totalnum : 5);
        mJiAdapter = new JiAdapter(mainVideo.totalnum);
        binding.adGrid.setAdapter(mJiAdapter);
        binding.adGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectIndex == position) return;
                openVideo(position);

            }
        });
        binding.adToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
            case HttpStatus.HTTP_GET_VIDEOS_MAINID_SUC:
                final VideosBean videosBean = (VideosBean) object;
                if (videosBean.mainVideosList != null && videosBean.mainVideosList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainVideo = videosBean.mainVideosList.get(0);
                            initView();
                        }
                    });
                }
                break;
        }
    }

    private void openVideo(int position) {
        selectIndex = position;
        mJiAdapter.notifyDataSetChanged();
        String url = mainVideo.vurl;
        int real = mainVideo.isreal;
        int isvip = mainVideo.isvip;
        if (selectIndex > 0) {
            for (int i = 0; i < mainVideo.sectionlist.size(); i++) {
                if (mainVideo.sectionlist.get(i).ordernum == position + 1) {
                    url = mainVideo.sectionlist.get(i).surl;
                    real = mainVideo.sectionlist.get(i).isreal;
                }
            }
        }
        //先判断是否符合VIP规则
        if (isvip == 1 && !Contents.getInstance().getUserinfo().isvip) {
            Toast.makeText(mContext, "非会员禁止播放", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent;
        //判断是否为原生视频
        if (real == 0) {
            intent = new Intent(VideoDetailActivity.this, UnityPlayerActivity.class);
        } else {
//            intent = new Intent(VideoDetailActivity.this, PlayActivity.class);
            intent = new Intent(VideoDetailActivity.this, GSYVideoActivity.class);
        }
        intent.putExtra("url", url);
        startActivity(intent);
        if (!plus) {
            plus = true;
            HttpTools.plusPlayTimes(mContext, VideoDetailActivity.this, mainVideo.vid);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ad_back:
//                finish();
//                break;
            case R.id.ad_play:
                openVideo(0);
                break;
        }
    }

    private class JiAdapter extends BaseAdapter {
        private int total;

        public JiAdapter(int total) {
            this.total = total;
        }

        @Override
        public int getCount() {
            return total;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(mContext, R.layout.jishu_item, null);
            TextView tv = convertView.findViewById(R.id.ji_num);
            tv.setText((position + 1) + "");
            if (position == selectIndex) tv.setSelected(true);
            else tv.setSelected(false);
            return convertView;
        }
    }


}
