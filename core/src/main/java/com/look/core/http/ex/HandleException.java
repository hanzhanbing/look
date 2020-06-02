package com.look.core.http.ex;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * Created by huyg on 2020-01-14.
 */
public class HandleException {


    private static final int ACCESS_DENIED = 302;
    private static final int UNAUTHORIZED = 401;    //未认证
    private static final int FORBIDDEN = 403;   //被禁止
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    //服务器异常状态码
    private static final int SERVICE_EXCEPTION = 1000;

    public static Throwable handle(Throwable e) {
        String errorMsg = "";
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case UNAUTHORIZED:
                    //errorMsg = "token过期";
                    break;
                case FORBIDDEN:
                    errorMsg = "请先登录";
                    break;
                case NOT_FOUND:
                    errorMsg = "HTTP NOT FOUND";
                    break;
                case REQUEST_TIMEOUT:
                    errorMsg = "请求超时";
                    break;
                case GATEWAY_TIMEOUT:
                    errorMsg = "网关超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    errorMsg = "内部服务器错误";
                    break;
                case BAD_GATEWAY:
                    errorMsg = "无效网关";
                    break;
                case SERVICE_UNAVAILABLE:
                    errorMsg = "找不到服务器";
                    break;
                case ACCESS_DENIED:
                    errorMsg = "拒绝访问";
                    break;
                default:
                    errorMsg = "网络错误";
                    break;
            }
        } else if (e instanceof JsonParseException) {
            errorMsg = "解析错误";
        } else if (e instanceof ConnectException) {
            errorMsg = "连接失败";
        } else if (e instanceof UnknownHostException) {
            errorMsg = "网络连接失败";
        } else if (e instanceof SSLHandshakeException) {
            errorMsg = "证书验证失败";
        } else if (e instanceof CertPathValidatorException) {
            errorMsg = "证书路径没找到";
        } else if (e instanceof ConnectTimeoutException) {
            errorMsg = "连接超时";
        } else if (e instanceof ClassCastException) {
            errorMsg = "类型转换出错";
        } else if (e instanceof NullPointerException) {
            errorMsg = "数据有空";
        } else {
            errorMsg = e.getMessage();
        }
        return new Throwable(errorMsg, e);
    }


}
