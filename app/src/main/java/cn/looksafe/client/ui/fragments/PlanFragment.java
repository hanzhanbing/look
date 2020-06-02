package cn.looksafe.client.ui.fragments;

import com.look.core.ui.BaseFragment;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.FragmentPlanBinding;

/**
 * Created by huyg on 2020-05-29.
 */
public class PlanFragment extends BaseFragment<FragmentPlanBinding> {


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

    }
}
