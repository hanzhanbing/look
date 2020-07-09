package cn.looksafe.client.utils;

import cn.looksafe.client.BuildConfig;

/**
 * 常量
 */
public class Constant {
    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头
    public static final int REQ_PERM_EXTERNAL_STORAGE = 11004; // 读写文件

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";


    public static final String BUGLY_ID() {
        if (BuildConfig.branch == 0) {
            return "f8c58598be";
        } else {
            return "efd8cbae27";
        }
    }

    public static final String UMENG_ID() {
        if (BuildConfig.branch == 0) {
            return "5eec6f3adbc2ec08212af508";
        } else {
            return "5f03e2cf978eea07661be6e6";
        }
    }
}
