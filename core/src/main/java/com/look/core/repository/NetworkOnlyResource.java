package com.look.core.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.look.core.http.ApiResponse;
import com.look.core.vo.Resource;

import static com.look.core.vo.Status.LOADING;

/**
 * Created by huyg on 2020-01-14.
 * 不需要存储
 */
public abstract class NetworkOnlyResource<RequestType> {
    private final MediatorLiveData<Resource<RequestType>> result = new MediatorLiveData<>();
    private LiveData<ApiResponse<RequestType>> apiResponse;
    private MutableLiveData<RequestType> liveData = new MutableLiveData();

    @MainThread
    public NetworkOnlyResource() {
        result.setValue(new Resource<>(LOADING));
        fetchFromNetwork();
    }

    //从网络获取
    private void fetchFromNetwork() {
        apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(ApiResponse<RequestType> response) {
                result.removeSource(apiResponse);
                if (response instanceof ApiResponse.ApiSuccessResponse) {
                    result.addSource(liveData,
                            new Observer<RequestType>() {
                                @Override
                                public void onChanged(RequestType newData) {
                                    result.setValue(result.getValue().success(newData));
                                }
                            });
                    liveData.postValue(((ApiResponse.ApiSuccessResponse<RequestType>) response).getData());
                } else if (response instanceof ApiResponse.ApiErrorResponse) {
                    NetworkOnlyResource.this.onFetchFailed();
                    ApiResponse.ApiErrorResponse<RequestType> errorResponse = (ApiResponse.ApiErrorResponse<RequestType>) response;
                    result.setValue(result.getValue().error(errorResponse.getCode(),errorResponse.getData(),errorResponse.getMessage()));
                }

            }
        });
    }


    //从网络中获取
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();


    //网络获取失败
    @MainThread
    protected void onFetchFailed() {

    }

    public final LiveData<Resource<RequestType>> asLiveData() {
        return result;
    }


}
