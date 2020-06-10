package com.look.core.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.look.core.http.ApiResponse;
import com.look.core.vo.Resource;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.look.core.vo.Status.LOADING;

/**
 * Created by huyg on 2020-01-14.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private LiveData<ApiResponse<RequestType>> apiResponse;
    protected MutableLiveData<RequestType> liveData = new MutableLiveData();

    @MainThread
    public NetworkBoundResource() {
        result.setValue(new Resource<>(LOADING));
        LiveData<ResultType> dbSource = loadFromDb();
        if (!dbSource.hasActiveObservers()) {
            fetchFromNetwork(dbSource);
        } else {
            result.addSource(dbSource, data -> {
                result.removeSource(dbSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource,
                            newData -> result.setValue(result.getValue().success(newData)));
                }
            });
        }
    }


    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }


    //从网络获取
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        apiResponse = createCall();
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            if (response instanceof ApiResponse.ApiSuccessResponse) {
                saveResultAndReInit((ApiResponse.ApiSuccessResponse) response);
            } else if (response instanceof ApiResponse.ApiErrorResponse) {
                onFetchFailed();
                result.addSource(dbSource, resultType -> {
                    ApiResponse.ApiErrorResponse<ResultType> errorResponse = (ApiResponse.ApiErrorResponse<ResultType>) response;
                    result.setValue(result.getValue().error(errorResponse.getCode(),errorResponse.getData(),errorResponse.getMessage()));
                });
            }

        });
    }

    @MainThread
    private void saveResultAndReInit(ApiResponse.ApiSuccessResponse<RequestType> response) {
        Observable.fromCallable(new Callable<ApiResponse<RequestType>>() {
            @Override
            public ApiResponse<RequestType> call() throws Exception {
                saveCallResult(response.getData());
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
        result.addSource(loadFromDb(),
                newData -> {
                    result.setValue(result.getValue().success(newData));
                });
    }


    //将网络获取的数据存储到db中
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);


    //是否需要从网络重新获取
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);


    //从db内获取cache数据
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    //从网络中获取
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();


    //网络获取失败
    @MainThread
    protected void onFetchFailed() {

    }



    public final LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

}
