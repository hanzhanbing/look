package cn.looksafe.client.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.db.Action;

/**
 * Created by huyg on 2020-05-20.
 */
public class PlanAdapter extends BaseQuickAdapter<Action, BaseViewHolder> {

    public PlanAdapter(int layoutResId, @Nullable List<Action> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Action item) {
        helper.setText(R.id.item_video_name, item.videoName);
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.item_top_view, false);
        } else {
            helper.setVisible(R.id.item_top_view, true);
        }
        if (helper.getAdapterPosition() == getItemCount() - 1) {
            helper.setVisible(R.id.item_bottom_view, false);
        } else {
            helper.setVisible(R.id.item_bottom_view, true);
        }
        long minute = item.playTime / 60;
        long second = item.playTime % 60;
        StringBuilder builder = new StringBuilder();
        if (minute != 0) {
            builder.append(minute);
            builder.append("分钟");
        }
        if (second != 0) {
            builder.append(second);
            builder.append("秒");
        }
        helper.setText(R.id.item_video_time, builder.toString());
    }
}
