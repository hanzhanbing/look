package cn.looksafe.client.my;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.beans.OrderHttp;
import cn.looksafe.client.databinding.MyOrderBinding;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.utils.HoldView;
import cn.looksafe.client.utils.HttpStatus;

public class MyOrderActivity extends BaseActivity {
    MyOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_order);
//        binding.moBack.setOnClickListener(this);
        binding.moToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getMyOrders(mContext, MyOrderActivity.this);
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_MY_ORDERS_SUC:
                final OrderHttp orderHttp = (OrderHttp) object;
                if (orderHttp.list != null && orderHttp.list.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.aoList.setAdapter(new OrderAdapter(orderHttp.list));
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.mo_back:
//                finish();
//                break;
        }
    }


    private class OrderAdapter extends BaseAdapter {
        private ArrayList<OrderHttp.Order> orders;

        public OrderAdapter(ArrayList<OrderHttp.Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return orders.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView = null;
            if (convertView == null) {
                holdView = new HoldView();
                convertView = View.inflate(mContext, R.layout.my_order_item, null);
                holdView.textView1 = convertView.findViewById(R.id.moi_time);
                holdView.textView2 = convertView.findViewById(R.id.moi_sn);
                holdView.textView3 = convertView.findViewById(R.id.moi_price);
            } else holdView = (HoldView) convertView.getTag();
            OrderHttp.Order order = orders.get(position);
            //20200517150202
            String paytime = order.paytime + "";
            if (paytime.length() > 13)
                holdView.textView1.setText(paytime.substring(0, 4) + "-" + paytime.substring(4, 6) + "-" + paytime.substring(6, 8) + "\n" + paytime.substring(8, 10) + ":" + paytime.substring(10, 12) + ":" + paytime.substring(12, 14));
            holdView.textView2.setText(order.outtradeno);
            holdView.textView3.setText(order.amount / 100.00 + "元");
            return convertView;
        }
    }

}
