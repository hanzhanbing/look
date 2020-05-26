package cn.looksafe.client.utils;

public interface HttpCallBack {
    void requestSuccess(int code, Object object, Object object2);

    void requestFailure(int code, Object object);
}
