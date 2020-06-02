package com.look.core.vo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.look.core.bean.ResourceStatus;
import com.look.core.widget.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;

import static com.look.core.vo.Status.ERROR;
import static com.look.core.vo.Status.LOADING;
import static com.look.core.vo.Status.SUCCESS;

/**
 * Created by huyg on 2020-01-14.
 */
public class Resource<T> {
    @NonNull
    public Status status;
    @Nullable
    public T data;
    @Nullable
    public String message;
    public int code;

    protected CircleProgressDialog mDialog;

    public void work(ResourceListener<T> listener) {

        switch (status) {
            case LOADING:
                EventBus.getDefault().post(new ResourceStatus(LOADING));
                break;
            case SUCCESS:
                EventBus.getDefault().post(new ResourceStatus(SUCCESS));
                listener.onSuccess(data);
                break;
            case ERROR:
                EventBus.getDefault().post(new ResourceStatus(ERROR));
                listener.onError(message);
                break;
        }
    }


    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Resource(@NonNull Status status) {
        this.status = status;
    }

    public Resource<T> success(@NonNull T data) {
        this.status = SUCCESS;
        this.data = data;
        return this;
    }

    public Resource<T> error(int code, T data, String msg) {
        this.status = ERROR;
        this.code = code;
        this.data = data;
        this.message = msg;
        return this;
    }

    public Resource<T> loading(@Nullable T data) {
        this.status = LOADING;
        return this;
    }

}
