package cn.looksafe.client.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.activitys.VideoDetailActivity;
import cn.looksafe.client.beans.VideosBean;

public class VideoListAdapter extends BaseAdapter {
    private ArrayList<VideosBean.MainVideo> videos;
    private Context context;
    private BitmapUtils mBitmapUtils;

    public VideoListAdapter(ArrayList<VideosBean.MainVideo> videos, Context context) {
        this.videos = videos;
        this.context = context;
        mBitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return videos.size();
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
        HoldView holdView = null;
        if (convertView == null) {
            holdView = new HoldView();
            convertView = View.inflate(context, R.layout.fg_type_item, null);
            holdView.imageView1 = convertView.findViewById(R.id.fti_pic);
            holdView.textView1 = convertView.findViewById(R.id.fti_name);
            convertView.setTag(holdView);
        } else holdView = (HoldView) convertView.getTag();
        VideosBean.MainVideo video = videos.get(position);
        mBitmapUtils.display(holdView.imageView1, video.vimg);
        holdView.textView1.setText(video.vname);


        return convertView;
    }

    public void openPlayActivity(int position) {
        VideosBean.MainVideo video = videos.get(position);
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("video", video);
        context.startActivity(intent);
    }


}
