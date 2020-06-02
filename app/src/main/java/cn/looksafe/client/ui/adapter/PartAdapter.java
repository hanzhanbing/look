package cn.looksafe.client.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.SectionBean;

/**
 * Created by huyg on 2020-03-30.
 */
public class PartAdapter extends BaseQuickAdapter<SectionBean, BaseViewHolder> {

    public PartAdapter(int layoutResId, @Nullable List<SectionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionBean item) {
        helper.setText(R.id.item_part, String.valueOf(item.getOrdernum()));
    }
}
