package cn.looksafe.client.ui.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseFragment;
import com.look.core.vo.ResourceListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.FragmentVideoBinding;
import cn.looksafe.client.ui.adapter.VideoAdapter;
import cn.looksafe.client.ui.adapter.divider.DividerGridItemDecoration;
import cn.looksafe.client.utils.GlideImageLoader;
import cn.looksafe.client.viewmodel.VideoViewModel;

/**
 * Created by huyg on 2020-05-28.
 */
public class VideoFragment extends BaseFragment<FragmentVideoBinding> implements BaseQuickAdapter.OnItemClickListener {


    private VideoAdapter mAdapter;
    private int type;
    private VideoViewModel mViewModel;
    private List<VideosBean.MainVideosListBean> mDatas;
    private List<String> mLoopImgs;

    public static VideoFragment newInstance(int type) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        initIntent();
        initView();
        initData();
    }

    private void initData() {
        if (type == 0) {
            mViewModel.getLoopImgs(SpManager.getInstance(mContext).getSP("phone"))
                    .observe(this, resource -> resource.work(new ResourceListener<LoopImgHttp>() {
                        @Override
                        public void onSuccess(LoopImgHttp data) {
                            if (data.list != null && data.list.size() > 0) {
                                mBinding.banner.setVisibility(View.VISIBLE);
                                mLoopImgs = new ArrayList<>();
                                for (LoopImgHttp.LoopImg loopImg : data.list) {
                                    mLoopImgs.add(loopImg.imgurl);
                                }
                                mBinding.banner.setImages(mLoopImgs);
                                mBinding.banner.start();
                            } else {
                                mBinding.banner.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    }));
        }
    }

    private void initIntent() {
        type = getArguments().getInt("type");
    }

    private void initView() {
        initRefresh();
        initRecycler();
        initBanner();
    }


    private void initBanner() {
        if (type == 0) {
            mBinding.banner.setVisibility(View.VISIBLE);
            mBinding.banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                }
            });
            mBinding.banner.setImageLoader(new GlideImageLoader());
        } else {
            mBinding.banner.setVisibility(View.GONE);
        }

    }

    private void initRecycler() {
        mAdapter = new VideoAdapter(R.layout.item_video, mDatas);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.addItemDecoration(new DividerGridItemDecoration(ConvertUtils.dp2px(16), getResources().getColor(R.color._f2f2f2)));
        mAdapter.setOnItemClickListener(this);
    }


    private void initRefresh() {
        mBinding.refresh.autoRefresh();
        mBinding.refresh.setEnableLoadMore(false);
        mBinding.refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //0:推荐;1:快乐学习;2:轻松一刻;3:公益视训;4:教学广场
                switch (type) {
                    case 0:
                        getHotVideo();
                        break;
                    case 1:
                        getHappyLearnApp();
                        break;
                    case 2:
                        getRelaxApp();
                        break;
                    case 3:
                        getLovelyApp();
                        break;
                    case 4:
                        getFreeApp();
                        break;
                }
            }
        });
    }

    private void getHotVideo() {
        mViewModel.getHotVideo(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {
                        mAdapter.setNewData(data.getMainVideosList());
                        mBinding.refresh.finishRefresh();
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                        mBinding.refresh.finishRefresh();
                    }
                }));
    }

    private void getHappyLearnApp() {
        mViewModel.getHappyLearnApp(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {
                        mAdapter.setNewData(data.getMainVideosList());
                        mBinding.refresh.finishRefresh();
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                        mBinding.refresh.finishRefresh();
                    }
                }));
    }

    private void getRelaxApp() {
        mViewModel.getRelaxApp(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {
                        mAdapter.setNewData(data.getMainVideosList());
                        mBinding.refresh.finishRefresh();
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                        mBinding.refresh.finishRefresh();
                    }
                }));
    }


    private void getLovelyApp() {
        mViewModel.getLovelyApp(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {
                        mBinding.refresh.finishRefresh();
                        mAdapter.setNewData(data.getMainVideosList());
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                        mBinding.refresh.finishRefresh();
                    }
                }));
    }

    private void getFreeApp() {
        mViewModel.getFreeApp(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {
                        mBinding.refresh.finishRefresh();
                        mAdapter.setNewData(data.getMainVideosList());
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                        mBinding.refresh.finishRefresh();
                    }
                }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        VideosBean.MainVideosListBean videosListBean = mAdapter.getItem(position);
        ARouter.getInstance().build("/video/detail")
                .withInt("id", videosListBean.getVid())
                .navigation();
    }
}
