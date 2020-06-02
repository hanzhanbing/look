package cn.looksafe.client.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.PointHttp;

/**
 * Created by huyg on 2020-06-02.
 */
public class FindAdapter extends BaseQuickAdapter<PointHttp.Point, BaseViewHolder> {

    public FindAdapter(int layoutResId, @Nullable List<PointHttp.Point> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointHttp.Point item) {
        helper.setText(R.id.item_address, item.addr)
                .setText(R.id.item_distance, item.pname);
        helper.addOnClickListener(R.id.item_navigation);
    }
}
