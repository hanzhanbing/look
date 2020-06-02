package cn.looksafe.client.ui.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import androidx.databinding.DataBindingUtil;
import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityVideoBinding;
import cn.looksafe.client.utils.BaseActivity;

public class GSYVideoActivity extends BaseActivity {
    ActivityVideoBinding binding;
    OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            //设置这些参数，有可能谁弹出系统提示“从顶部往下退出全屏模式”
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } catch (Exception e) {
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        String url = getIntent().getStringExtra("url");
        init(url);

    }

    private void init(String url) {
        binding.detailPlayer.setUp(url, true, "");

        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);
//        videoPlayer.setThumbImageView(imageView);
        //增加title
        binding.detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        binding.detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, binding.detailPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        binding.detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        binding.detailPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        binding.detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.detailPlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.detailPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.detailPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            binding.detailPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        binding.detailPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }


}
