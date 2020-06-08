package cn.looksafe.client.ui.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.look.core.manager.GlideManager;
import com.ruffian.library.widget.RImageView;

import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VideosBean;

/**
 * Created by huyg on 2020-05-28.
 */
public class VideoAdapter extends BaseQuickAdapter<VideosBean.MainVideosListBean, BaseViewHolder> {

    public VideoAdapter(int layoutResId, @Nullable List<VideosBean.MainVideosListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideosBean.MainVideosListBean item) {
        helper.setText(R.id.item_video_name, item.getVname())
                .setText(R.id.item_video_time, String.valueOf(item.getPlaytimes()))
                .setText(R.id.item_video_type, item.getVtypename());
        RImageView imageView = helper.getView(R.id.item_video_img);
        GlideManager.getInstance().displayNetImage(mContext, item.getVimg(), imageView);
    }
}
