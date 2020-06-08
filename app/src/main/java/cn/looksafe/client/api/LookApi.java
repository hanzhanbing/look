package cn.looksafe.client.api;

import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;

import cn.looksafe.client.beans.EyeLogHttp;
import cn.looksafe.client.beans.HttpBean;
import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.beans.PointHttp;
import cn.looksafe.client.beans.UploadFile;
import cn.looksafe.client.beans.UserInfo;
import cn.looksafe.client.beans.VideoType;
import cn.looksafe.client.beans.VersionHttp;
import cn.looksafe.client.beans.VideosBean;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    LiveData<ApiResponse<BaseResponse>> getSmsCode(@Field("loginname") String phone);


    /**
     * 老用户获取验证码
     *
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("getSmsCodeUserApp")
    LiveData<ApiResponse<BaseResponse>> getCode(@Field("loginname") String phone);


    /**
     * 首页推荐
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getHotListApp")
    LiveData<ApiResponse<VideosBean>> getHotVideo(@Field("loginname") String phone);

    /**
     * 快乐学习
     *
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("getHappyLearnApp")
    LiveData<ApiResponse<VideosBean>> getHappyLearnApp(@Field("loginname") String phone);


    /**
     * 轻松一刻
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getRelaxApp")
    LiveData<ApiResponse<VideosBean>> getRelaxApp(@Field("loginname") String loginName);


    /**
     * 教学广场
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getLovelyApp")
    LiveData<ApiResponse<VideosBean>> getLovelyApp(@Field("loginname") String loginName);

    /**
     * 公益广场
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getFreeApp")
    LiveData<ApiResponse<VideosBean>> getFreeApp(@Field("loginname") String loginName);


    /**
     * 获取视频详情
     *
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
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("getEyeLogsApp")
    LiveData<ApiResponse<EyeLogHttp>> getEyesRecord(@Field("loginname") String loginName);


    /**
     * 增加记录
     *
     * @param loginName
     * @param left
     * @param right
     * @return
     */
    @FormUrlEncoded
    @POST("upEyeLogsApp")
    LiveData<ApiResponse<BaseResponse>> addEyesRecord(@Field("loginname") String loginName,
                                                      @Field("lefte") String left,
                                                      @Field("righte") String right);


    /**
     * 获取版本
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("versionApp")
    LiveData<ApiResponse<VersionHttp>> checkVersion(@Field("loginname") String loginName);


    /**
     * 发现
     *
     * @param loginName
     * @return
     */
    @FormUrlEncoded
    @POST("queryPointsApp")
    LiveData<ApiResponse<PointHttp>> getPoints(@Field("loginname") String loginName);


    /**
     * 反馈
     *
     * @param loginName
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("suggestApp")
    LiveData<ApiResponse<BaseResponse>> suggest(@Field("loginname") String loginName, @Field("content") String content);


    /**
     * 密码修改
     *
     * @param loginname
     * @param prePwd
     * @param newPwd
     * @return
     */
    @FormUrlEncoded
    @POST("resetPwdApp")
    LiveData<ApiResponse<BaseResponse>> resetPwd(@Field("loginname") String loginname,
                                                 @Field("prePwd") String prePwd,
                                                 @Field("newPwd") String newPwd);


    /**
     * 获取轮播图
     *
     * @param loginname
     * @return
     */
    @FormUrlEncoded
    @POST("getLoopImgsApp")
    LiveData<ApiResponse<LoopImgHttp>> getLoopImgs(@Field("loginname") String loginname);


    /**
     * 获取用户信息
     *
     * @param loginname
     * @return
     */
    @FormUrlEncoded
    @POST("getUserInfoApp")
    LiveData<ApiResponse<UserInfo>> getUserInfo(@Field("loginname") String loginname);


    /**
     * 注册
     *
     * @param phone
     * @param pwd
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("loginRegisterApp")
    LiveData<ApiResponse<BaseResponse>> register(@Field("loginname") String phone,
                                                 @Field("pwd") String pwd,
                                                 @Field("smscode") String code);


    /**
     * 更新用户信息
     * @param loginname
     * @param nickName
     * @param headImg
     * @return
     */
    @FormUrlEncoded
    @POST("modifyUserInfoApp")
    LiveData<ApiResponse<BaseResponse>> modifyUserInfo(@Field("loginname") String loginname,
                                                       @Field("nickname") String nickName,
                                                        @Field("headimg") String headImg);


    /**
     * 上传头像
     * @param file
     * @return
     */
    @Multipart
    @POST("uploadFileApp")
    LiveData<ApiResponse<UploadFile>> upload(@Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("playtimesApp")
    LiveData<ApiResponse<BaseResponse>> addPlayTime(@Field("loginname") String loginname,
                                                    @Field("vid") int vid);


    @FormUrlEncoded
    @POST("activeApp")
    LiveData<ApiResponse<BaseResponse>> activeApp(@Field("loginname") String loginname,
                                                  @Field("acode") String qrCode);


    @FormUrlEncoded
    @POST("getVideoTypesApp")
    LiveData<ApiResponse<VideoType>> getVideoType(@Field("loginname") String loginname);



    @FormUrlEncoded
    @POST("getVideosByTypeApp")
    LiveData<ApiResponse<VideosBean>> getVidoesByType(@Field("loginname") String loginname,
                                                      @Field("tlv") int tlv,
                                                      @Field("id") int id);
}
