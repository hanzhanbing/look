package cn.looksafe.client.ui.activitys;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.look.core.http.BaseResponse;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityModifyPwdBinding;
import cn.looksafe.client.viewmodel.UserViewModel;


@Route(path = "/user/modifyPwd")
public class ModifyPwdActivity extends BaseActivity<ActivityModifyPwdBinding> {


    private UserViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mBinding.setPresenter(new Presenter());
    }

    public class Presenter {
        public void modifyPwd() {
            String old = mBinding.oldPassword.getText().toString().trim();
            String pwd = mBinding.password.getText().toString().trim();
            if (TextUtils.isEmpty(old) || TextUtils.isEmpty(pwd) || old.length() < 6 || pwd.length() < 6) {
                Toast.makeText(mContext, "密码输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            mViewModel.resetPwd(SpManager.getInstance(mContext).getSP("phone"),old,pwd)
                    .observe(ModifyPwdActivity.this,resource->resource.work(new ResourceListener<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            toast("修改成功");
                            finish();
                        }

                        @Override
                        public void onError(String msg) {
                            toast(msg);
                        }
                    }));
        }
    }

}
