package cn.looksafe.client;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.liulishuo.filedownloader.FileDownloader;
import com.look.core.CoreSource;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.looksafe.client.beans.VideoType;
import cn.looksafe.client.ui.activitys.MainActivity;


public class MyApplication extends MultiDexApplication {


    private List<VideoType.MainlistBean> mTypeBeans;
    private static MyApplication mApp;
    static {
        //初始化上拉刷新及上拉加载
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((Context context, RefreshLayout layout) -> new MaterialHeader(context));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((Context context, RefreshLayout layout) -> new ClassicsFooter(context));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
//        Bugly.init(getApplicationContext(), "c4fa4ecd31", false);
        Bugly.init(getApplicationContext(), "f8c58598be", false);
//        CrashReport.initCrashReport(getApplicationContext(), "f8c58598be", false);
        Beta.canShowUpgradeActs.add(MainActivity.class);
        MultiDex.install(this);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //
        CoreSource.getInstance().init(this);
        FileDownloader.init(getApplicationContext());
        initARouter();
        closeAndroidPDialog();

    }

    public static MyApplication getInstance() {
        return mApp;
    }


    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTypeBeans(List<VideoType.MainlistBean> mTypeBeans) {
        this.mTypeBeans = mTypeBeans;
    }


    public List<VideoType.MainlistBean> getTypeBeans() {
        return mTypeBeans;
    }
}
