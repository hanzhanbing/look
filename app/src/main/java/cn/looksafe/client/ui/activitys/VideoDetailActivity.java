package cn.looksafe.client.ui.activitys;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.SectionBean;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityVideoDetailBinding;
import cn.looksafe.client.ui.adapter.PartAdapter;
import cn.looksafe.client.viewmodel.VideoViewModel;

@Route(path = "/video/detail")
public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding> implements BaseQuickAdapter.OnItemClickListener {


    private VideoViewModel mViewModel;
    private int id;
    private PartAdapter mAdapter;
    private List<SectionBean> mParts;
    private VideosBean.MainVideosListBean mVideoDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void init() {
        initIntent();
        initView();
        initData();

    }

    private void initData() {
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        getVideoDetail();
    }

    private void initView() {
        initRecycler();
    }

    private void initRecycler() {
        mAdapter = new PartAdapter(R.layout.item_part, mParts);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setNewData(mParts);
    }

    private void getVideoDetail() {
        mViewModel.getVideoById(SpManager.getInstance(mContext).getSP("phone"), id)
                .observe(this, resource -> resource.work(new ResourceListener<VideosBean>() {
                    @Override
                    public void onSuccess(VideosBean data) {

                        List<VideosBean.MainVideosListBean> mainVideosListBeans = data.getMainVideosList();
                        if (mainVideosListBeans != null && mainVideosListBeans.size() > 0) {
                            VideosBean.MainVideosListBean videosListBean = mainVideosListBeans.get(0);
                            mVideoDetail = videosListBean;
                            GlideManager.getInstance().displayNetImage(mContext, videosListBean.getVimg(), mBinding.videoImg);
                            if (videosListBean.getIsvip() == 1) {
                                mBinding.vip.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.vip.setVisibility(View.GONE);
                            }
                            mBinding.playTime.setText(videosListBean.getPlaytimes() + "次");
                            mBinding.videoType.setText(videosListBean.getVtypename());
                            mBinding.videoName.setText(videosListBean.getVname());
                            SectionBean sectionBean = new SectionBean(1, videosListBean.getIsreal(), videosListBean.getVurl());
                            List<SectionBean> sectionBeans = videosListBean.getSectionlist();
                            sectionBeans.add(0, sectionBean);
                            Collections.sort(sectionBeans);
                            mAdapter.setNewData(sectionBeans);
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        toast("获取详情失败");
                    }
                }));
    }

    private void initIntent() {
        id = getIntent().getIntExtra("id", 0);
    }


    @Override
    public void setActionBar() {
        mTitle.setText("视频详情");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SectionBean sectionBean = mAdapter.getItem(position);
        palyVideo(sectionBean.getIsreal(), sectionBean.getSurl());

    }

    private void palyVideo(int isReal, String url) {
        Intent intent;
        if (isReal == 0) {
            intent = new Intent(VideoDetailActivity.this, UnityPlayerActivity.class);
        } else {
            intent = new Intent(VideoDetailActivity.this, GSYVideoActivity.class);
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public class Presenter {

        public void play() {
            if (mVideoDetail != null) {
                palyVideo(mVideoDetail.getIsreal(), mVideoDetail.getVurl());
            }
        }
    }
}
