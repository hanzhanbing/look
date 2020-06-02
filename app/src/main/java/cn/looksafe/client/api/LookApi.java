package cn.looksafe.client.api;

import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.HttpResponse;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.beans.VideosBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by huyg on 2020-05-27.
 */
public interface LookApi {


    /**
     * 密码登录
     *
     * @param phone    手机号
     * @param password 密码
     */

    @FormUrlEncoded
    @POST("loginpwdApp")
    LiveData<ApiResponse<HttpBean>> login(@Field("loginname") String phone,
                                          @Field("pwd") String password);


    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("getSmsCodeRegisterApp")
    LiveData<ApiResponse<Void>> getSmsCode(@Field("loginname") String phone);


    /**
     * 首页推荐
     * @return
     */
    @FormUrlEncoded
    @POST("getHotListApp")
    LiveData<ApiResponse<VideosBean>> getHotVideo(@Field("loginname") String phone);

    /**
     * 快乐学习
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("getHappyLearnApp")
    LiveData<ApiResponse<VideosBean>> getHappyLearnApp(@Field("loginname") String phone);


    /**
     * 轻松一刻
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getRelaxApp")
    LiveData<ApiResponse<VideosBean>> getRelaxApp(@Field("loginname") String loginName);


    /**
     * 教学广场
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getLovelyApp")
    LiveData<ApiResponse<VideosBean>> getLovelyApp(@Field("loginname") String loginName);

    /**
     * 公益广场
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getFreeApp")
    LiveData<ApiResponse<VideosBean>> getFreeApp(@Field("loginname") String loginName);


    /**
     * 获取视频详情
     * @param loginName
     * @param vid
     * @return
     */
    @FormUrlEncoded
    @POST("getVideoByIdApp")
    LiveData<ApiResponse<VideosBean>> getVideoById(@Field("loginname") String loginName,
                                                   @Field("vid") int vid);


    /**
     * 获取视力记录
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getEyeLogsApp")
    LiveData<ApiResponse<EyeLogHttp>> getEyesRecord(@Field("loginname") String loginName);


    /**
     * 增加记录
     * @param loginName
     * @param left
     * @param right
     * @return
     */
    @FormUrlEncoded
    @POST("upEyeLogsApp")
    LiveData<ApiResponse<Boolean>> addEyesRecord(@Field("loginname") String loginName,
                                                    @Field("lefte") String left,
                                                    @Field("righte") String right);


    /**
     * 获取版本
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("versionApp")
    LiveData<ApiResponse<VersionHttp>> checkVersion(@Field("loginname") String loginName);


    /**
     * 发现
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("queryPointsApp")
    LiveData<ApiResponse<PointHttp>> getPoints(@Field("loginname") String loginName);


    @FormUrlEncoded
    @POST("suggestApp")
    LiveData<ApiResponse<Void>> suggest(@Field("loginname") String loginName,@Field("content") String content);
}
