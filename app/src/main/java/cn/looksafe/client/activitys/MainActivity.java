package cn.looksafe.client.activitys;

import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;
import cn.looksafe.client.R;
import cn.looksafe.client.fragments.FragmentHome;
import cn.looksafe.client.fragments.FragmentMy;
import cn.looksafe.client.fragments.FragmentShare;
import cn.looksafe.client.fragments.FragmentType;
import cn.looksafe.client.tools.Contents;
import cn.looksafe.client.tools.MyApplication;
import cn.looksafe.client.utils.BaseActivity;

public class MainActivity extends FragmentActivity {
    //Tab选项卡的文字
    private String tabTextArray[] = {"首页", "分类", "发现", "我的"};
    //Tab选项卡的图片
    private int tabImageArray[] = {R.drawable.btn_tab_1, R.drawable.btn_tab_2, R.drawable.btn_tab_3,
            R.drawable.btn_tab_4};
    //存放fragment界面
    private final Class<?>[] fragments = {FragmentHome.class, FragmentType.class, FragmentShare.class, FragmentMy.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Contents.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec("fragment" + i).setIndicator(getTabItemView(i));
            fragmentTabHost.addTab(tabSpec, fragments[i], null);
        }
    }

    private View getTabItemView(int i) {
        //此处布局是一样的，如果需要显示消息气泡 可根据选项另外设置布局文件
        View view = getLayoutInflater().inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_Tab);
        TextView textView = (TextView) view.findViewById(R.id.tv_Tab);

        imageView.setBackgroundResource(tabImageArray[i]);
        textView.setText(tabTextArray[i]);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Contents.getInstance().removeActivity(this);
    }

    private long backtime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://返回
                if (System.currentTimeMillis() - backtime < 2000) {
                    finish();
                } else backtime = System.currentTimeMillis();
                break;
        }
        return false;
    }
}
