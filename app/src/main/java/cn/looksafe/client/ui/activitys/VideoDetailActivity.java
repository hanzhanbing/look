package cn.looksafe.client.ui.activitys;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.look.core.http.BaseResponse;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.Resource;
import com.look.core.vo.ResourceListener;
import com.unity3d.player.UnityPlayerActivity;

import java.util.Collections;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.SectionBean;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.databinding.ActivityVideoDetailBinding;
import cn.looksafe.client.db.Action;
import cn.looksafe.client.db.ActionDao;
import cn.looksafe.client.db.AppDatabase;
import cn.looksafe.client.manager.DialogManager;
import cn.looksafe.client.ui.adapter.PartAdapter;
import cn.looksafe.client.viewmodel.VideoViewModel;

@Route(path = "/video/detail")
public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding> implements BaseQuickAdapter.OnItemClickListener {


    private VideoViewModel mViewModel;
    private int id;
    private PartAdapter mAdapter;
    private List<SectionBean> mParts;
    private VideosBean.MainVideosListBean mVideoDetail;
    private long startTime;
    private long endTime;
    private SectionBean mSectionBean;
    private ActionDao mActionDao;

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
        mActionDao = AppDatabase.getAppDatabase(this).actionDao();
    }

    private void initView() {
        mBinding.setPresenter(new Presenter());
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
                            SectionBean sectionBean = new SectionBean(1, videosListBean.getIsreal(), videosListBean.getVurl(), videosListBean.getVtime());
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
        mSectionBean = mAdapter.getItem(position);
        palyVideo(mSectionBean.getIsreal(), mSectionBean.getSurl());

    }

    private void palyVideo(int isReal, String url) {
        if (mVideoDetail.getIsvip() == 1) {
            if (SpManager.getInstance(mContext).getBooleanSp("vip")) {
                playVideo(isReal, url);
            } else {
                DialogManager.getInstance().showTipDialog(VideoDetailActivity.this, "温馨提示", "该视频为vip视频，请前往购买", "前往", "取消", new DialogManager.Listener() {
                    @Override
                    public void onClickListener() {
                        ARouter.getInstance().build("/user/vip").navigation();
                    }
                });
            }
        } else {
            playVideo(isReal, url);
        }
    }

    private void playVideo(int isReal, String url) {
        mViewModel.addPlayTime(SpManager.getInstance(mContext).getSP("phone"),mVideoDetail.getVid()).observe(this, new Observer<Resource<BaseResponse>>() {
            @Override
            public void onChanged(Resource<BaseResponse> baseResponseResource) {

            }
        });
        Intent intent;
        if (isReal == 0) {
            intent = new Intent(VideoDetailActivity.this, UnityPlayerActivity.class);
        } else {
            intent = new Intent(VideoDetailActivity.this, GSYVideoActivity.class);
        }
        startTime = System.currentTimeMillis();
        intent.putExtra("url", url);
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            Log.d(getClass().getSimpleName(),"requestCode-->startTime-->"+startTime+"endTime-->"+endTime+"time"+time+"11"+((time / 1000) > mSectionBean.getVtime()));
            if ((time / 1000) > mSectionBean.getVtime()) {
                mActionDao.insert(new Action(mVideoDetail.getVid(), mVideoDetail.getVname(), mVideoDetail.getVtime(), System.currentTimeMillis()));
            }
        }
    }

    public class Presenter {

        public void play() {
            if (mVideoDetail != null) {
                mSectionBean = mAdapter.getItem(0);
                palyVideo(mVideoDetail.getIsreal(), mVideoDetail.getVurl());
            }
        }
    }
}
