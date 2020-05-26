package cn.looksafe.client.tools;

import android.os.Environment;

public class AppConfig {
    public static final String SPNAME = "look";
    public static final int TOAST_LONG = 1;
    public static final int TOAST_SHORT = 0;
    public static final String SERVER = "http://47.114.33.38/luke/";
//        public static final String SERVER = "http://192.168.31.104:8080/luke/";
    public static final String apksDir = Environment.getExternalStorageDirectory().getPath() + "/look/apks/";
    public static final String rootDir = Environment.getExternalStorageDirectory().getPath() + "/look/";
    public static final String wx_key = "47ZheknE3E9w4wSphsP0KlDz1QcXYUbG";
    public static final String wx_appid = "wx2e1387c82a97dc62";
}
