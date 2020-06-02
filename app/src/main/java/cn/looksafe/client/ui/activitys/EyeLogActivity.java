package cn.looksafe.client.ui.activitys;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.databinding.ActivityEyeRecordBinding;
import cn.looksafe.client.utils.LineChartManager2;
import cn.looksafe.client.viewmodel.EyeRecordViewModel;


@Route(path = "/eye/log")
public class EyeLogActivity extends BaseActivity<ActivityEyeRecordBinding> {


    private LineChart lineChart1, lineChart2;
    private LineData lineData;
    private EyeRecordViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eye_record;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(EyeRecordViewModel.class);
        getEyeRecord();
    }

    private void getEyeRecord() {
        mViewModel.getEyesRecord(SpManager.getInstance(mContext).getSP("phone"))
                .observe(this,resource->resource.work(new ResourceListener<EyeLogHttp>() {
                    @Override
                    public void onSuccess(EyeLogHttp data) {
                        initChart2(data);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                }));
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

    private class Presenter{

        public void addRecord(){
            String left = "";
            String right = "";
            mViewModel.addEyesRecord(SpManager.getInstance(mContext).getSP("phone"),left,right)
                    .observe(EyeLogActivity.this,resource->resource.work(new ResourceListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {

                        }

                        @Override
                        public void onError(String msg) {

                        }
                    }));
        }
    }

}
