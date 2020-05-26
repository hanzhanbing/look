package cn.looksafe.client.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.activitys.FreeActivity;
import cn.looksafe.client.activitys.GyActivity;
import cn.looksafe.client.activitys.HotActivity;
import cn.looksafe.client.activitys.KuaiActivity;
import cn.looksafe.client.activitys.QsActivity;
import cn.looksafe.client.activitys.VideoDetailActivity;
import cn.looksafe.client.beans.HotListHttp;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.FgHomeBinding;
import cn.looksafe.client.my.VipActivity;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.DbTools;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseFragment;
import cn.looksafe.client.utils.HoldView;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.ImagePagerAdapter;
import cn.looksafe.client.utils.VideoListAdapter;


public class FragmentHome extends BaseFragment {
    FgHomeBinding binding;
    private boolean getLoopimg;
    private boolean getHot;
    private VideoListAdapter videoListAdapter;
    private VideosBean hotVideos = null;//热门推荐

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FgHomeBinding.inflate(inflater);
        if (view == null) {
            view = binding.getRoot();
            binding.fhMore.setOnClickListener(this);
            binding.fhGy.setOnClickListener(this);
            binding.fhQs.setOnClickListener(this);
            binding.fhSx.setOnClickListener(this);
            binding.fhXuexi.setOnClickListener(this);
            binding.fhVip.setOnClickListener(this);
            refreshLoopImgs();

        }
        return view;
    }

    private void refreshLoopImgs() {
        ArrayList<String> imgurls = DbTools.getInstance(getActivity()).queryAllLoopImgs();
        if (imgurls.size() > 0) {
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getActivity(), binding.fhCvp, imgurls);
            binding.fhCvp.setOffscreenPageLimit(1);
            binding.fhCvp.setAdapter(imagePagerAdapter);
            binding.fhCvp.setTimeOut(10);
            binding.fhCvp.setHasData(true);
            binding.fhCvp.startTimer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!getLoopimg) HttpTools.getLoopImgs(getActivity(), FragmentHome.this);
        if (!getHot) HttpTools.getHotList(getActivity(), FragmentHome.this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fh_xuexi:
                startActivity(new Intent(getActivity(), KuaiActivity.class));
                break;
            case R.id.fh_qs:
                startActivity(new Intent(getActivity(), QsActivity.class));
                break;
            case R.id.fh_sx:
                startActivity(new Intent(getActivity(), FreeActivity.class));
                break;
            case R.id.fh_gy:
                startActivity(new Intent(getActivity(), GyActivity.class));
                break;
            case R.id.fh_vip:
//                Toast.makeText(getActivity(), "敬请期待!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.fh_more://更多
                Intent intent = new Intent(getActivity(), HotActivity.class);
                intent.putExtra("hotVideos", hotVideos);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_LOOP_IMGS_SUC:
                getLoopimg = true;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLoopImgs();
                    }
                });
                break;
            case HttpStatus.HTTP_GET_HOT_LIST_SUC:
                getHot = true;
                hotVideos = (VideosBean) object;
                if (hotVideos.mainVideosList != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoListAdapter = new VideoListAdapter(hotVideos.mainVideosList, getActivity());
                            binding.fhGrid.setAdapter(videoListAdapter);
                            binding.fhGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @Override
    public void requestFailure(int code, Object object) {
        String tip = object.toString();
        toast(tip, AppConfig.TOAST_SHORT);
    }


}
