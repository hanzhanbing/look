package cn.looksafe.client.ui.fragments;

import android.animation.ObjectAnimator;

import androidx.annotation.NonNull;

import com.look.core.ui.BaseFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.FragmentPlanBinding;
import cn.looksafe.client.db.Action;
import cn.looksafe.client.db.ActionDao;
import cn.looksafe.client.db.AppDatabase;
import cn.looksafe.client.ui.adapter.PlanAdapter;

/**
 * Created by huyg on 2020-05-29.
 */
public class PlanFragment extends BaseFragment<FragmentPlanBinding> {

    private PlanAdapter mAdapter;
    private List<Action> mDatas;
    private ActionDao mActionDao;

    public static PlanFragment newInstance() {
        PlanFragment planFragment = new PlanFragment();
        return planFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan;
    }

    @Override
    protected void init() {
        mActionDao = AppDatabase.getAppDatabase(getActivity()).actionDao();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAction();
    }

    private void getAction() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DATE), 0, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DATE), 23, 59, 59);
        List<Action> actions = mActionDao.getAll(startTime.getTimeInMillis(),endTime.getTimeInMillis());
        mAdapter.setNewData(actions);
        if (actions != null) {
            long times = 0;
            for (Action action : actions) {
                times += action.playTime;
            }
            setData(20 * 60, times);
        }

        mBinding.refresh.finishRefresh();
    }

    private void setData(int max, float current) {
        float percentage = (100f * current) / max;
        if (percentage > 100) {
            mBinding.progress.setText("100%");
        } else {
            mBinding.progress.setText((int) percentage + "%");
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.percent, "percentage", 0, percentage);
        animator.setDuration(2000);
        animator.start();
    }

    private void initView() {
        initRecycler();
        initRefresh();
    }

    private void initRefresh() {
        mBinding.refresh.setEnableLoadMore(false);
        mBinding.refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getAction();
            }
        });
    }

    private void initRecycler() {
        mAdapter = new PlanAdapter(R.layout.item_plan, mDatas);
        mBinding.recycler.setAdapter(mAdapter);
    }
}
