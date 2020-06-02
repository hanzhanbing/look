package cn.looksafe.client.ui.fragments;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseFragment;
import com.look.core.vo.ResourceListener;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.databinding.FragmentFindBinding;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.ui.adapter.FindAdapter;
import cn.looksafe.client.utils.MapUtil;
import cn.looksafe.client.viewmodel.FindViewModel;

/**
 * Created by huyg on 2020-05-29.
 */
public class FindFragment extends BaseFragment<FragmentFindBinding> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private String[] tabs = {"全部", "服务店", "学校", "医院"};
    private FindViewModel mViewModel;
    private FindAdapter mAdapter;
    private BaiduMap mBaiduMap;
    private List<PointHttp.Point> mDatas;
    private List<PointHttp.Point> mDatas1;
    private List<PointHttp.Point> mDatas2;
    private List<PointHttp.Point> mDatas3;
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.mipmap.n_se);

    public static FindFragment newInstance() {
        FindFragment findFragment = new FindFragment();
        return findFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(FindViewModel.class);
        initView();
        initData();
    }

    private void initData() {
        mViewModel.getPoints(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<PointHttp>() {
                    @Override
                    public void onSuccess(PointHttp data) {
                        mDatas = data.list;
                        mAdapter.setNewData(data.list);
                        initMarker(mDatas);
                        if (mDatas != null && mDatas.size() > 0) {
                            mDatas1 = new ArrayList<>();
                            mDatas2 = new ArrayList<>();
                            mDatas3 = new ArrayList<>();
                            for (PointHttp.Point point : mDatas) {
                                switch (point.type) {
                                    case 0:
                                        mDatas1.add(point);
                                        break;
                                    case 1:
                                        mDatas2.add(point);
                                        break;
                                    case 2:
                                        mDatas3.add(point);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                }));
    }

    private void initView() {
        initTabLayout();
        initAdapter();
        initMap();
    }

    private void initMap() {
        mBaiduMap = mBinding.mapview.getMap();
        LatLng cenpt = new LatLng(30.28, 120.15);
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(cenpt)
                .zoom(18)
                .build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态

    }

    private void initAdapter() {
        mAdapter = new FindAdapter(R.layout.item_find, mDatas);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void initTabLayout() {
        for (int i = 0; i < tabs.length; i++) {
            mBinding.tablayout.addTab(mBinding.tablayout.newTab().setText(tabs[i]));
        }
        mBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mDatas != null && mDatas.size() > 0) {
                    switch (tab.getText().toString()) {
                        case "全部":
                            mAdapter.setNewData(mDatas);
                            break;
                        case "服务店":
                            mAdapter.setNewData(mDatas1);
                            break;
                        case "学校":
                            mAdapter.setNewData(mDatas2);
                            break;
                        case "医院":
                            mAdapter.setNewData(mDatas3);
                            break;
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initMarker(List<PointHttp.Point> points) {
        if (points != null && points.size() > 0) {
            for (PointHttp.Point point : points) {
                LatLng latLngA = new LatLng(Double.parseDouble(point.wdu), Double.parseDouble(point.jdu));
                MarkerOptions markerOptionsA = new MarkerOptions()
                        .position(latLngA)
                        .icon(bitmapA)// 设置 Marker 覆盖物的图标
                        .zIndex(9)// 设置 marker 覆盖物的 zIndex
                        .draggable(false); // 设置 marker 是否允许拖拽，默认不可拖拽
                Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(markerOptionsA));
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PointHttp.Point point = mAdapter.getItem(position);
        if (point != null) {
            mapCenter(point);
        }
    }

    private void mapCenter(PointHttp.Point point) {
        LatLng latLngA = new LatLng(Double.parseDouble(point.wdu), Double.parseDouble(point.jdu));
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(latLngA)
                .zoom(13)
                .build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PointHttp.Point point = mAdapter.getItem(position);
        if (point != null) {

            showList(Double.parseDouble(point.wdu), Double.parseDouble(point.jdu));
        }
    }
}
