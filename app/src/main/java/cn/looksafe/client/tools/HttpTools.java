package cn.looksafe.client.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.beans.HotListHttp;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.beans.OrderHttp;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.beans.VipMenuHttp;
import cn.looksafe.client.beans.WxpayInitBean;
import cn.looksafe.client.models.NormalModel;
import cn.looksafe.client.utils.HttpCallBack;
import cn.looksafe.client.utils.HttpStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpTools {

    private static final int HTTP_LOGIN = 0x01;//密码登录
    private static final int HTTP_GET_CODE = 0x02;//获取验证码
    private static final int HTTP_REGISTER_LOGIN = 0x03;//注册登录
    private static final int HTTP_GET_CODE_USER = 0x04;//老用户获取验证码
    private static final int HTTP_LOGIN_USER = 0x05;//老用户改密码登录
    private static final int HTTP_ACTIVE_APP = 0x06;//激活APP
    private static final int HTTP_GET_LOOP_IMG = 0x07;//获取轮播图片
    private static final int HTTP_GET_HOT_LIST = 0x08;//获取热门列表
    private static final int HTTP_GET_ALL_LIST = 0x09;//获取全部列表
    private static final int HTTP_GET_KUAI_LIST = 0x10;//获取快乐学习列表
    private static final int HTTP_GET_QS_LIST = 0x11;//获取轻松一刻列表
    private static final int HTTP_GET_MF_LIST = 0x12;//获取免费视训列表
    private static final int HTTP_GET_GY_LIST = 0x13;//获取公益广告列表
    private static final int HTTP_GET_POINT_LIST = 0x14;//获取服务点列表
    private static final int HTTP_GET_EYE_LIST = 0x15;//获取视力列表
    private static final int HTTP_UP_EYE_LOG = 0x16;//上传视力列表
    private static final int HTTP_RESET_PWD = 0x17;//重置密码
    private static final int HTTP_GET_VERSION = 0x18;//获取版本
    private static final int HTTP_GET_VIP_MENU = 0x19;//获取VIP套餐
    private static final int HTTP_PRE_PAY = 0x20;//准备进行支付
    private static final int HTTP_SUGGEST = 0x21;//提交建议
    private static final int HTTP_GET_VIDEOS_MAINID = 0x22;//获取一部视频
    private static final int HTTP_PLUS_PLAY = 0x23;//播放次数累加
    private static final int HTTP_GET_VIP_DATE = 0x24;//获取会员到期日
    private static final int HTTP_GET_MY_ORDERS = 0x25;//获取我的订单
    private static Gson gson = new Gson();
    private static HttpUtils httpUtils;

    private static synchronized void getHttp(final Context context, String url, final int type, final HttpCallBack httpCallBack) {
        Log.d("xxx", url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (httpCallBack != null)
                    httpCallBack.requestFailure(HttpStatus.HTTP_ERROR, "通讯失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = Tools.readResponse(response.body().byteStream());
                    if (TextUtils.isEmpty(json)) {
                        if (httpCallBack != null)
                            httpCallBack.requestFailure(HttpStatus.HTTP_ERROR, "数据异常");
                        return;
                    }
                    Log.i("xxx", json);
                    HttpBean httpBean = gson.fromJson(json, HttpBean.class);
                    if (httpBean.getCode() != 0) {
                        if (httpCallBack != null)
                            httpCallBack.requestFailure(HttpStatus.HTTP_ERROR, httpBean.getMsg());
                        return;
                    }
                    if (!TextUtils.isEmpty(httpBean.getToken()))
                        Contents.getInstance().setToken(httpBean.getToken());
                    switch (type) {
                        case HTTP_LOGIN:
                            Contents.getInstance().setUserinfo(httpBean);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_LOGIN_SUC, httpBean, null);
                            break;
                        case HTTP_GET_CODE:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_SMS_SUC, null, null);
                            break;
                        case HTTP_REGISTER_LOGIN:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_REGISTER_LOGIN_SUC, httpBean, null);
                            break;
                        case HTTP_GET_CODE_USER:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_SMS_USER_SUC, null, null);
                            break;
                        case HTTP_LOGIN_USER:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_LOGIN_USER_SUC, httpBean, null);
                            break;
                        case HTTP_ACTIVE_APP:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_ACTIVE_APP_SUC, null, null);
                            break;
                        case HTTP_GET_LOOP_IMG:
                            LoopImgHttp loopImgHttp = gson.fromJson(json, LoopImgHttp.class);
                            if (loopImgHttp.list != null && loopImgHttp.list.size() > 0) {
                                DbTools.getInstance(context).insertAllLoopImgs(loopImgHttp);
                            }
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_LOOP_IMGS_SUC, null, null);
                            break;
                        case HTTP_GET_HOT_LIST:
                            VideosBean videosBeanh = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_HOT_LIST_SUC, videosBeanh, null);
                            break;
                        case HTTP_GET_ALL_LIST:
                            VideosBean videosBean = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_ALL_LIST_SUC, videosBean, null);
                            break;
                        case HTTP_GET_KUAI_LIST:
                            VideosBean videosBeank = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_KUAI_LIST_SUC, videosBeank, null);
                            break;
                        case HTTP_GET_QS_LIST:
                            VideosBean videosBeanq = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_QS_LIST_SUC, videosBeanq, null);
                            break;
                        case HTTP_GET_MF_LIST:
                            VideosBean videosBeanm = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_FREE_LIST_SUC, videosBeanm, null);
                            break;
                        case HTTP_GET_GY_LIST:
                            VideosBean videosBeang = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_GY_LIST_SUC, videosBeang, null);
                            break;
                        case HTTP_GET_POINT_LIST:
                            PointHttp pointHttp = gson.fromJson(json, PointHttp.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_POINT_LIST_SUC, pointHttp, null);
                            break;
                        case HTTP_GET_EYE_LIST:
                            EyeLogHttp eyeLogHttp = gson.fromJson(json, EyeLogHttp.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_EYE_LIST_SUC, eyeLogHttp, null);
                            break;
                        case HTTP_UP_EYE_LOG:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_UP_EYE_LOG_SUC, null, null);
                            break;
                        case HTTP_RESET_PWD:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_RESET_PWD_SUC, null, null);
                            break;
                        case HTTP_GET_VERSION:
                            VersionHttp versionHttp = gson.fromJson(json, VersionHttp.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_VERSION_SUC, versionHttp, null);
                            break;
                        case HTTP_GET_VIP_MENU:
                            VipMenuHttp vipMenuHttp = gson.fromJson(json, VipMenuHttp.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_VIP_MENU_SUC, vipMenuHttp, null);
                            break;
                        case HTTP_PRE_PAY:
                            WxpayInitBean wxpayInitBean = gson.fromJson(json, WxpayInitBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_PRE_PAY_SUC, wxpayInitBean, null);
                            break;
                        case HTTP_SUGGEST:
                            httpCallBack.requestSuccess(HttpStatus.HTTP_SUGGEST_SUC, null, null);
                            break;
                        case HTTP_GET_VIDEOS_MAINID:
                            VideosBean videosBean1 = gson.fromJson(json, VideosBean.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_VIDEOS_MAINID_SUC, videosBean1, null);
                            break;
                        case HTTP_PLUS_PLAY:
                            break;
                        case HTTP_GET_VIP_DATE:
                            Contents.getInstance().getUserinfo().setVipexp(httpBean.getVipexp());
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_VIP_DATE_SUC, httpBean, null);
                            break;
                        case HTTP_GET_MY_ORDERS:
                            OrderHttp orderHttp = gson.fromJson(json, OrderHttp.class);
                            httpCallBack.requestSuccess(HttpStatus.HTTP_GET_MY_ORDERS_SUC, orderHttp, null);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private static String fileDir = null;

    /**
     * @param url     最新版客户端url
     * @param name    apk名称
     * @param context
     */
    public static synchronized void downApk(String url, final String name, final Context context, final NormalModel normalModel, final HttpCallBack httpCallBack) {
        //如果上次已存在该安装包，则进行删除，防止错误包干扰
        fileDir = AppConfig.apksDir;
        File apkFile = new File(fileDir);
        if (!apkFile.exists()) apkFile.mkdirs();
        File[] files = apkFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].toString().contains(name))
                files[i].delete();
        }

        httpUtils = new HttpUtils();
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.configSoTimeout(8000);
        httpUtils.download(url, fileDir + name, true, false, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.i("xxx", "更新" + name);
                String fileName = AppConfig.apksDir + name;
                //有些系统下载的文件会被加序列号，所以进行检测对比
                if (!new File(fileName).exists()) {
                    File zongsFile = new File(fileDir);
                    File[] files = zongsFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getAbsolutePath().contains(name)) {
                            fileName = files[i].getAbsolutePath();
                        }
                    }
                }
                httpCallBack.requestSuccess(HttpStatus.HTTP_DOWN_APK_SUC, fileName, null);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                normalModel.setTip2("下载进度:" + (current * 100 / total) + "%");
            }

            @Override
            public void onFailure(HttpException e, String s) {
                httpCallBack.requestSuccess(-1, "更新失败", null);
            }
        });
    }

    /**
     * 密码登录
     *
     * @param context
     * @param loginname
     * @param pwd
     * @param httpCallBack
     */
    public static synchronized void login(Context context, String loginname, String pwd, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "loginpwdApp" + "?loginname=" + loginname + "&pwd=" + pwd;
        Contents.getInstance().setLoginname(loginname);
        getHttp(context, url, HTTP_LOGIN, httpCallBack);
    }

    /**
     * 获取验证码
     *
     * @param context
     * @param loginname
     * @param httpCallBack
     */
    public static synchronized void getSmsCode(Context context, String loginname, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getSmsCodeRegisterApp" + "?loginname=" + loginname;
        getHttp(context, url, HTTP_GET_CODE, httpCallBack);
    }

    /**
     * 老用户获取验证码
     *
     * @param context
     * @param loginname
     * @param httpCallBack
     */
    public static synchronized void getSmsCodeUser(Context context, String loginname, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getSmsCodeUserApp" + "?loginname=" + loginname;
        getHttp(context, url, HTTP_GET_CODE_USER, httpCallBack);
    }


    /**
     * 注册登录
     *
     * @param context
     * @param loginname
     * @param pwd
     * @param smsCode
     * @param httpCallBack
     */
    public static synchronized void registerLogin(Context context, String loginname, String pwd, String smsCode, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "loginRegisterApp" + "?loginname=" + loginname + "&pwd=" + pwd + "&smscode=" + smsCode;
        Contents.getInstance().setLoginname(loginname);
        getHttp(context, url, HTTP_REGISTER_LOGIN, httpCallBack);
    }

    /**
     * 新密码登录
     *
     * @param context
     * @param loginname
     * @param pwd
     * @param smsCode
     * @param httpCallBack
     */
    public static synchronized void loginNewPwd(Context context, String loginname, String pwd, String smsCode, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "loginUserNewPwdApp" + "?loginname=" + loginname + "&pwd=" + pwd + "&smscode=" + smsCode;
        Contents.getInstance().setLoginname(loginname);
        getHttp(context, url, HTTP_LOGIN_USER, httpCallBack);
    }


    /**
     * 激活
     *
     * @param context
     * @param qrcode
     * @param httpCallBack
     */
    public static synchronized void activeApp(Context context, String qrcode, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "activeApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&acode=" + qrcode + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_ACTIVE_APP, httpCallBack);
    }

    /**
     * 获取轮播图片
     *
     * @param context
     * @param httpCallBack
     */
    public static synchronized void getLoopImgs(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getLoopImgsApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_LOOP_IMG, httpCallBack);
    }

    public static synchronized void getHotList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getHotListApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_HOT_LIST, httpCallBack);

    }

    public static synchronized void getAllList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "queryVideosApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_ALL_LIST, httpCallBack);

    }

    /**
     * 快乐学习
     *
     * @param context
     * @param httpCallBack
     */
    public static synchronized void getKuaiList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getHappyLearnApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_KUAI_LIST, httpCallBack);

    }

    /**
     * 轻松一刻
     *
     * @param context
     * @param httpCallBack
     */
    public static synchronized void getQsList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getRelaxApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_QS_LIST, httpCallBack);

    }

    /**
     * @param context
     * @param httpCallBack
     */
    public static synchronized void getFreeList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getFreeApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_MF_LIST, httpCallBack);

    }


    public static synchronized void getGyList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getLovelyApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_GY_LIST, httpCallBack);

    }

    public static synchronized void getPointList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "queryPointsApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_POINT_LIST, httpCallBack);

    }

    public static synchronized void getEyeList(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getEyeLogsApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_EYE_LIST, httpCallBack);

    }

    public static synchronized void upEyeLog(Context context, HttpCallBack httpCallBack, String lefte, String righte) {
        String url = AppConfig.SERVER + "upEyeLogsApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&lefte=" + lefte + "&righte=" + righte;
        getHttp(context, url, HTTP_UP_EYE_LOG, httpCallBack);

    }

    public static synchronized void resetPwd(Context context, HttpCallBack httpCallBack, String prePwd, String newPwd) {
        String url = AppConfig.SERVER + "resetPwdApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&prePwd=" + prePwd + "&newPwd=" + newPwd;
        getHttp(context, url, HTTP_RESET_PWD, httpCallBack);

    }


    public static synchronized void getViplist(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getVipListApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_VIP_MENU, httpCallBack);

    }


    public static synchronized void getVersion(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "versionApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_VERSION, httpCallBack);

    }


    /**
     * 准备进行支付
     *
     * @param context
     * @param httpCallBack
     * @param price
     * @param menuid
     */
    public static synchronized void prePay(Context context, HttpCallBack httpCallBack, int price, int menuid) {
        String url = AppConfig.SERVER + "wxpayApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&price=" + price + "&menuid=" + menuid;
        getHttp(context, url, HTTP_PRE_PAY, httpCallBack);

    }

    /**
     * 提交意见
     *
     * @param context
     * @param httpCallBack
     * @param content
     */
    public static synchronized void suggest(Context context, HttpCallBack httpCallBack, String content) {
        String url = AppConfig.SERVER + "suggestApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&content=" + content;
        getHttp(context, url, HTTP_SUGGEST, httpCallBack);

    }

    /**
     * @param context
     * @param httpCallBack
     * @param vid          根据主视频id获取整部视频信息
     */
    public static synchronized void getVideosByMainId(Context context, HttpCallBack httpCallBack, int vid) {
        String url = AppConfig.SERVER + "getVideoByIdApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&vid=" + vid;
        getHttp(context, url, HTTP_GET_VIDEOS_MAINID, httpCallBack);

    }

    /**
     * @param context
     * @param httpCallBack
     * @param vid          播放次数增加
     */
    public static synchronized void plusPlayTimes(Context context, HttpCallBack httpCallBack, int vid) {
        String url = AppConfig.SERVER + "playtimesApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken()
                + "&vid=" + vid;
        getHttp(context, url, HTTP_PLUS_PLAY, httpCallBack);

    }

    /**
     * @param context      获取会员到期日
     * @param httpCallBack
     */
    public static synchronized void getVipDate(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "getVipDateApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_VIP_DATE, httpCallBack);
    }


    public static synchronized void getMyOrders(Context context, HttpCallBack httpCallBack) {
        String url = AppConfig.SERVER + "queryMyOrdersApp" + "?loginname=" + Contents.getInstance().getLoginname() + "&token=" + Contents.getInstance().getToken();
        getHttp(context, url, HTTP_GET_MY_ORDERS, httpCallBack);
    }


}
