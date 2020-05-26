package cn.looksafe.client.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.BaseActivity;
import cn.looksafe.client.databinding.MyEyeBinding;
import cn.looksafe.client.utils.HttpStatus;
import cn.looksafe.client.utils.LineChartManager2;

public class EyeLogActivity extends BaseActivity {
    MyEyeBinding binding;
    private LineChart lineChart1, lineChart2;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_eye);
//        binding.meBack.setOnClickListener(this);
        binding.meSub.setOnClickListener(this);
        binding.meToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HttpTools.getEyeList(mContext, EyeLogActivity.this);
    }

    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        super.requestSuccess(code, object, object2);
        switch (code) {
            case HttpStatus.HTTP_GET_EYE_LIST_SUC:
                final EyeLogHttp eyeLogHttp = (EyeLogHttp) object;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initChart2(eyeLogHttp);
                    }
                });
                break;
            case HttpStatus.HTTP_UP_EYE_LOG_SUC:
                HttpTools.getEyeList(mContext, EyeLogActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.meLeft.setText("");
                        binding.meRight.setText("");
                    }
                });
                toast("上传成功", AppConfig.TOAST_SHORT);
                break;
        }
    }

    private void initChart2(EyeLogHttp eyeLogHttp) {
        if (eyeLogHttp == null || eyeLogHttp.list == null || eyeLogHttp.list.size() == 0) return;
        ArrayList<String> dates = new ArrayList<>();
        float[] date1 = new float[eyeLogHttp.list.size()];
        float[] date2 = new float[eyeLogHttp.list.size()];
        for (int i = eyeLogHttp.list.size() - 1; i >= 0; i--) {
            EyeLogHttp.Eye eye = eyeLogHttp.list.get(i);
            String da = "";
            if (eye.createday.length() == 8) {
                da = eye.createday.substring(4, 6) + "/" + eye.createday.substring(6, 8);
            }
            dates.add(da);
            try {
                date1[eyeLogHttp.list.size() - 1 - i] = Float.parseFloat(eye.lefte);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                date1[eyeLogHttp.list.size() - 1 - i] = 0;
            }
            try {
                date2[eyeLogHttp.list.size() - 1 - i] = Float.parseFloat(eye.righte);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                date2[eyeLogHttp.list.size() - 1 - i] = 0;
            }
        }
        lineChart2 = (LineChart) findViewById(R.id.me_lines);
        //设置图表的描述
//        lineChart2.setDescription("双曲线");
        //设置y轴的数据
        //设置折线的名称
        LineChartManager2.setLineName("左眼视力");
        //设置第二条折线y轴的数据
        LineChartManager2.setLineName1("右眼视力");
        //创建两条折线的图表
        lineData = LineChartManager2.initDoubleLineChart(this, lineChart1, dates, date1, date2);
        LineChartManager2.initDataStyle(lineChart2, lineData, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.me_back:
//                finish();
//                break;
            case R.id.me_sub:
                String l = binding.meLeft.getText().toString();
                String r = binding.meRight.getText().toString();
                if (TextUtils.isEmpty(l) || TextUtils.isEmpty(r)) {
                    Toast.makeText(mContext, "请完整填写左右眼视力", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.parseDouble(l) > 5 || Double.parseDouble(r) > 5) {
                    Toast.makeText(mContext, "视力填写错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpTools.upEyeLog(mContext, EyeLogActivity.this, l, r);
                break;
        }
    }
}
