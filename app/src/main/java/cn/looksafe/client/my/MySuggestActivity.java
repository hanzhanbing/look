package cn.looksafe.client.my;

import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityFeedbackBinding;
import cn.looksafe.client.viewmodel.SuggestViewModel;

public class MySuggestActivity extends BaseActivity<ActivityFeedbackBinding> {

    private SuggestViewModel mViewModel;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(SuggestViewModel.class);
        mBinding.setPresenter(new Presenter());
    }

    public class Presenter {
        public void submit() {
            String content = mBinding.edit.getText().toString();
            if (TextUtils.isEmpty(content)){
                toast("请输入内容");
                return;
            }
            mViewModel.suggest(SpManager.getInstance(mContext).getSP("phone"),content)
                    .observe(MySuggestActivity.this,resource->resource.work(new ResourceListener<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            toast("提交成功");
                            finish();
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    }));
        }
    }

    @Override
    public void setActionBar() {
        mTitle.setText("意见反馈");
    }
}
