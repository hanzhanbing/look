package cn.looksafe.client.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.look.core.vo.Resource;

import cn.looksafe.client.beans.VideoType;
import cn.looksafe.client.repository.VideoRepository;

/**
 * Created by huyg on 2020-06-08.
 */
public class SplashViewModel extends ViewModel {
    private VideoRepository mVideoRepository;


    public SplashViewModel(){
        mVideoRepository = new VideoRepository();
    }


    public LiveData<Resource<VideoType>> getVideoType(){
        return mVideoRepository.getVideoType("test");
    }
}
