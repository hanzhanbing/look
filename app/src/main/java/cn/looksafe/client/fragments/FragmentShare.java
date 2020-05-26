package cn.looksafe.client.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import cn.looksafe.client.R;
import cn.looksafe.client.activitys.MainActivity;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseFragment;
import cn.looksafe.client.databinding.FgShareBinding;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.MapUtil;

public class FragmentShare extends BaseFragment {
    FgShareBinding binding;
    private boolean getAll;
    private ArrayList<PointHttp.Point> basePoints = new ArrayList<>();
    private ListAdapter listAdapter;
    private ArrayList<PointHttp.Point> showPoints = new ArrayList<>();

    //--------------------
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.mipmap.n_se);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FgShareBinding.inflate(inflater);
//        if (view == null) {
        view = binding.getRoot();
        binding.fsAll.setOnClickListener(this);
        binding.fsFw.setOnClickListener(this);
        binding.fsSchool.setOnClickListener(this);
        binding.fsHospital.setOnClickListener(this);
//        }
        mBaiduMap = binding.fsBmapView.getMap();
        LatLng cenpt = new LatLng(30.28, 120.15);
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(cenpt)
                .zoom(18)
                .build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
        binding.fsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapCenter(position);
            }
        });
        return view;
    }

    private void initMarker() {

        for (int i = 0; i < showPoints.size(); i++) {
            // add marker overlay
            LatLng latLngA = new LatLng(Double.parseDouble(showPoints.get(i).wdu), Double.parseDouble(showPoints.get(i).jdu));
            MarkerOptions markerOptionsA = new MarkerOptions()
                    .position(latLngA)
                    .icon(bitmapA)// 设置 Marker 覆盖物的图标
                    .zIndex(9)// 设置 marker 覆盖物的 zIndex
                    .draggable(false); // 设置 marker 是否允许拖拽，默认不可拖拽
            Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(markerOptionsA));
            if (i == 0) {
                mapCenter(0);
//                MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
//                        .target(latLngA)
//                        .zoom(13)
//                        .build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
            }
        }


    }

    private void mapCenter(int position) {
        if (position >= showPoints.size()) return;
        LatLng latLngA = new LatLng(Double.parseDouble(showPoints.get(position).wdu), Double.parseDouble(showPoints.get(position).jdu));
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(latLngA)
                .zoom(13)
                .build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
    }


    /**
     * 清除所有Overlay
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fs_all:
                selectType(-1);
                break;
            case R.id.fs_fw:
                selectType(0);
                break;
            case R.id.fs_school:
                selectType(1);
                break;
            case R.id.fs_hospital:
                selectType(2);
                break;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private ArrayList<PointHttp.Point> points;

        public ListAdapter(ArrayList<PointHttp.Point> points) {
            this.points = points;
        }

        @Override
        public int getCount() {
            return points.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(), R.layout.fg_share_item, null);
            TextView name = convertView.findViewById(R.id.fsi_name);
            TextView m = convertView.findViewById(R.id.fsi_m);
            TextView addr = convertView.findViewById(R.id.fsi_addr);
            m.setVisibility(View.GONE);
            RelativeLayout relativeLayout = convertView.findViewById(R.id.fsi_dh);
            final PointHttp.Point point = points.get(position);
            name.setText(point.pname);
            addr.setText(point.addr);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showList(Double.parseDouble(point.wdu), Double.parseDouble(point.jdu));
                }
            });
            return convertView;
        }
    }


    private void selectType(int type) {
        clearOverlay(null);
        binding.fsAll.setSelected(false);
        binding.fsFw.setSelected(false);
        binding.fsSchool.setSelected(false);
        binding.fsHospital.setSelected(false);
        showPoints.clear();
        switch (type) {
            case -1:
                binding.fsAll.setSelected(true);
                for (int i = 0; i < basePoints.size(); i++) {
                    showPoints.add(basePoints.get(i));
                }
                break;
            case 0:
                binding.fsFw.setSelected(true);
                for (int i = 0; i < basePoints.size(); i++) {
                    if (0 == basePoints.get(i).type) showPoints.add(basePoints.get(i));
                }
                break;
            case 1:
                binding.fsSchool.setSelected(true);
                for (int i = 0; i < basePoints.size(); i++) {
                    if (1 == basePoints.get(i).type) showPoints.add(basePoints.get(i));
                }
                break;
            case 2:
                binding.fsHospital.setSelected(true);
                for (int i = 0; i < basePoints.size(); i++) {
                    if (2 == basePoints.get(i).type) showPoints.add(basePoints.get(i));
                }
                break;
        }
        listAdapter = new ListAdapter(showPoints);
        binding.fsList.setAdapter(listAdapter);
        initMarker();
    }


    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_POINT_LIST_SUC:
                getAll = true;
                PointHttp pointHttp = (PointHttp) object;
                basePoints = pointHttp.list;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectType(-1);
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.fsBmapView.onResume();
        if (!getAll) HttpTools.getPointList(getActivity(), FragmentShare.this);
        selectType(-1);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.fsBmapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaiduMap.clear();
        binding.fsBmapView.onDestroy();
    }


    private void showList(final double wdu, final double jdu) {
        final String[] items = {"百度地图", "高德地图"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.app_logo)
                .setTitle("请选择导航")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                try {
                                    MapUtil.openBaidu(getActivity(), wdu, jdu);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "未安装百度地图", AppConfig.TOAST_SHORT).show();
                                }
                                break;
                            case 1:
                                double[] ps = MapUtil.bd09_To_Gcj02(wdu, jdu);
                                try {
                                    MapUtil.openGaoDe(getActivity(), ps[0], ps[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "未安装高德地图", AppConfig.TOAST_SHORT).show();
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }

}
