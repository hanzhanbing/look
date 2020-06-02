package com.look.core.vo;

/**
 * Created by huyg on 2020-02-11.
 */
public interface ResourceListener<T> {
    void onSuccess(T data);

    void onError(String msg);
}
