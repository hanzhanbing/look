package cn.looksafe.client.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.HotListHttp;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.FgTypeBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.BaseFragment;
import cn.looksafe.client.utils.HoldView;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.VideoListAdapter;

public class FragmentType extends BaseFragment {
    FgTypeBinding binding;
    private boolean getAll;
    private int selectid = -1;
    private ArrayList<VideosBean.MainVideo> mBaseList = new ArrayList<>();
    private VideoListAdapter videoListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FgTypeBinding.inflate(inflater);
//        if (view == null) {
        view = binding.getRoot();
        binding.ftAll.setOnClickListener(this);
        binding.ftEng.setOnClickListener(this);
        binding.ftYuwen.setOnClickListener(this);
        binding.ftLishi.setOnClickListener(this);
        binding.ftKexue.setOnClickListener(this);
        binding.ftShuxue.setOnClickListener(this);
        binding.ftDm.setOnClickListener(this);
        binding.ftYs.setOnClickListener(this);
        binding.ftJlp.setOnClickListener(this);
        binding.ftZy.setOnClickListener(this);
        binding.ftZs.setOnClickListener(this);
        binding.ftMfsx.setOnClickListener(this);
        binding.ftJxsp.setOnClickListener(this);
        binding.ftHyzs.setOnClickListener(this);
        selectType(selectid > -1 ? selectid : 0);
//        }
        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ft_all:
                selectType(0);
                break;
            case R.id.ft_eng:
                selectType(1);
                break;
            case R.id.ft_yuwen:
                selectType(2);
                break;
            case R.id.ft_lishi:
                selectType(3);
                break;
            case R.id.ft_kexue:
                selectType(4);
                break;
            case R.id.ft_shuxue:
                selectType(5);
                break;
            case R.id.ft_dm:
                selectType(6);
                break;
            case R.id.ft_ys:
                selectType(7);
                break;
            case R.id.ft_jlp:
                selectType(8);
                break;
            case R.id.ft_zy:
                selectType(9);
                break;
            case R.id.ft_zs:
                selectType(10);
                break;
            case R.id.ft_mfsx:
                selectType(11);
                break;
            case R.id.ft_jxsp:
                selectType(12);
                break;
            case R.id.ft_hyzs:
                selectType(13);
                break;
        }

    }

    private void selectType(int type) {
        binding.ftAll.setSelected(false);
        binding.ftEng.setSelected(false);
        binding.ftYuwen.setSelected(false);
        binding.ftLishi.setSelected(false);
        binding.ftKexue.setSelected(false);
        binding.ftShuxue.setSelected(false);
        binding.ftDm.setSelected(false);
        binding.ftYs.setSelected(false);
        binding.ftJlp.setSelected(false);
        binding.ftZy.setSelected(false);
        binding.ftZs.setSelected(false);
        binding.ftMfsx.setSelected(false);
        binding.ftJxsp.setSelected(false);
        binding.ftHyzs.setSelected(false);
        selectid = type;
        int typeid = -1;
        switch (type) {
            case 0:
                binding.ftAll.setSelected(true);
                break;
            case 1:
                binding.ftEng.setSelected(true);
                typeid = 10001;
                break;
            case 2:
                binding.ftYuwen.setSelected(true);
                typeid = 10002;
                break;
            case 3:
                binding.ftLishi.setSelected(true);
                typeid = 10003;
                break;
            case 4:
                binding.ftKexue.setSelected(true);
                typeid = 10004;
                break;
            case 5:
                binding.ftShuxue.setSelected(true);
                typeid = 10005;
                break;
            case 6:
                binding.ftDm.setSelected(true);
                typeid = 20001;
                break;
            case 7:
                binding.ftYs.setSelected(true);
                typeid = 20002;
                break;
            case 8:
                binding.ftJlp.setSelected(true);
                typeid = 20003;
                break;
            case 9:
                binding.ftZy.setSelected(true);
                typeid = 20004;
                break;
            case 10:
                binding.ftZs.setSelected(true);
                typeid = 20005;
                break;
            case 11:
                binding.ftMfsx.setSelected(true);
                typeid = 30001;
                break;
            case 12:
                binding.ftJxsp.setSelected(true);
                typeid = 40001;
                break;
            case 13:
                binding.ftHyzs.setSelected(true);
                typeid = 40002;
                break;
        }
        //重新加载列表
        ArrayList<VideosBean.MainVideo> showlist = Tools.getTypeVideoList(mBaseList, typeid);
        videoListAdapter = new VideoListAdapter(showlist, getActivity());
        binding.ftGrid.setAdapter(videoListAdapter);
        binding.ftGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoListAdapter.openPlayActivity(position);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!getAll) HttpTools.getAllList(getActivity(), FragmentType.this);
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_ALL_LIST_SUC:
                getAll = true;
                final VideosBean videosBean = (VideosBean) object;
                mBaseList = videosBean.mainVideosList;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectType(0);
                    }
                });
                break;
        }
    }

//    private class ListAdapter extends BaseAdapter {
//        private ArrayList<VideosBean.MainVideo> videos;
//
//        public ListAdapter(ArrayList<VideosBean.MainVideo> videos) {
//            this.videos = videos;
//        }
//
//        @Override
//        public int getCount() {
//            return videos.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            HoldView holdView = null;
//            if (convertView == null) {
//                holdView = new HoldView();
//                convertView = View.inflate(getActivity(), R.layout.fg_type_item, null);
//                holdView.imageView1 = convertView.findViewById(R.id.fti_pic);
//                holdView.textView1 = convertView.findViewById(R.id.fti_name);
//                convertView.setTag(holdView);
//            } else holdView = (HoldView) convertView.getTag();
//            VideosBean.MainVideo video = videos.get(position);
//            mBitmapUtils.display(holdView.imageView1, video.vimg);
//            holdView.textView1.setText(video.vname);
//
//
//            return convertView;
//        }
//    }


}
