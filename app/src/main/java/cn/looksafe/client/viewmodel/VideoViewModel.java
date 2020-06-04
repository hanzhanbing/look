package cn.looksafe.client.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.look.core.http.ApiResponse;
import com.look.core.http.BaseResponse;
import com.look.core.repository.NetworkOnlyResource;
import com.look.core.viewmodel.BaseViewModel;
import com.look.core.vo.Resource;

import cn.looksafe.client.beans.LoopImgHttp;
import cn.looksafe.client.beans.VideosBean;
import cn.looksafe.client.repository.VideoRepository;

/**
 * Created by huyg on 2020-05-29.
 */
public class VideoViewModel extends BaseViewModel {

    private VideoRepository mVideoRepository;

    public VideoViewModel(){
        mVideoRepository = new VideoRepository();
    }



    public LiveData<Resource<VideosBean>> getHappyLearnApp(String loginName){
        return mVideoRepository.getHappyLearnApp(loginName);
    }

    public LiveData<Resource<VideosBean>> getRelaxApp(String loginName){
        return mVideoRepository.getRelaxApp(loginName);
    }


    public LiveData<Resource<VideosBean>> getLovelyApp(String loginName){
        return mVideoRepository.getLovelyApp(loginName);
    }

    public LiveData<Resource<VideosBean>> getFreeApp(String loginName){
        return mVideoRepository.getFreeApp(loginName);
    }


    public LiveData<Resource<VideosBean>> getHotVideo(String phone){
        return mVideoRepository.getHotVideo(phone);
    }


    public LiveData<Resource<VideosBean>> getVideoById(String loginName, int vid) {
        return mVideoRepository.getVideoById(loginName,vid);
    }

    public LiveData<Resource<LoopImgHttp>> getLoopImgs(String loginname){
        return mVideoRepository.getLoopImgs(loginname);
    }

    public LiveData<Resource<BaseResponse>> addPlayTime(String loginname, int vid) {
        return mVideoRepository.addPlayTime(loginname,vid);
    }
}
