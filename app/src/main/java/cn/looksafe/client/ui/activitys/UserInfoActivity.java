package cn.looksafe.client.ui.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.look.core.http.BaseResponse;
import com.look.core.manager.GlideManager;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.look.core.vo.ResourceListener;

import java.io.File;
import java.util.List;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.UploadFile;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.databinding.ActivityUserInfoBinding;
import cn.looksafe.client.viewmodel.UserCenterViewModel;

/**
 * Created by huyg on 2020-06-04.
 */
@Route(path = "/user/info")
public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> implements TakePhoto.TakeResultListener, InvokeListener {
    private UserCenterViewModel mViewModel;
    private String[] items = {"拍摄", "从相册选择"};
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private UserInfo.UserinfoBean mUserInfo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {
        mViewModel = ViewModelProviders.of(this).get(UserCenterViewModel.class);
        mBinding.setPresenter(new Presenter());
        getUserInfo();
    }

    private void getUserInfo() {
        mViewModel.getUserInfo(SpManager.getInstance(mContext).getSP("phone")).observe(this, resource -> resource.work(new ResourceListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo data) {
                mUserInfo = data.getUserinfo();
                if (mUserInfo != null) {
                    if (!TextUtils.isEmpty(mUserInfo.getNickname())) {
                        mBinding.name.setText(mUserInfo.getNickname());
                    } else {
                        mBinding.name.setText("未设置");
                    }
                    GlideManager.getInstance().displayNetImageWithCircle(mContext, mUserInfo.getHeadimg(), mBinding.avatar, getResources().getDrawable(R.mipmap.ic_user_avatar));
                }
            }

            @Override
            public void onError(String msg) {

            }
        }));
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result.getImage() == null) {
            return;
        }
        mViewModel.upload(new File(result.getImage().getOriginalPath()))
                .observe(this, resource -> resource.work(new ResourceListener<UploadFile>() {
                    @Override
                    public void onSuccess(UploadFile data) {
                        modifyAvatar(data.getUrl());
                    }

                    @Override
                    public void onError(String msg) {

                    }
                }));
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    public class Presenter {

        public void modifyAvatar() {
            getPhoto();
        }

        public void modifyName() {
            final EditText inputServer = new EditText(UserInfoActivity.this);
            inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
            builder.setTitle("修改昵称").setIcon(null).setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String nick = inputServer.getText().toString();
                    if (!TextUtils.isEmpty(nick)) {
                        modifyNickName(nick);
                    } else {
                        toast("请输入昵称");
                    }
                }
            });
            builder.show();
        }
    }

    private void modifyNickName(String nickName) {
        mViewModel.modifyUserInfo(SpManager.getInstance(mContext).getSP("phone"), nickName, mUserInfo.getHeadimg())
                .observe(this, resource -> resource.work(new ResourceListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        getUserInfo();
                    }

                    @Override
                    public void onError(String msg) {

                    }
                }));
    }

    private void modifyAvatar(String avatar) {
        mViewModel.modifyUserInfo(SpManager.getInstance(mContext).getSP("phone"), mUserInfo.getNickname(), avatar)
                .observe(this, resource -> resource.work(new ResourceListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        getUserInfo();
                    }

                    @Override
                    public void onError(String msg) {
                        toast(msg);
                    }
                }));
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public void getPhoto() {
        XXPermissions.with(this)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(
                        Permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            showDialog();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        toast("请同意拍照或录像权限");
                    }
                });
    }


    private void showDialog() {
        new MaterialDialog.Builder(mContext)
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        //设置裁剪参数
                        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
                        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                        getTakePhoto().onEnableCompress(compressConfig, true);  //设置为需要压缩
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        Uri imageUri = Uri.fromFile(file);
                        if (position == 0) {
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                            getTakePhoto().onPickFromCaptureWithCrop(imageUri, cropOptions);
                        } else {
                            getTakePhoto().onPickFromGalleryWithCrop(imageUri, cropOptions);
                        }
                    }
                }).show();
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void setActionBar() {
        mTitle.setText("用户中心");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
