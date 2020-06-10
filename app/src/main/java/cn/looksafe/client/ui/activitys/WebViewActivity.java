package cn.looksafe.client.ui.activitys;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;

import cn.looksafe.client.R;
import cn.looksafe.client.databinding.ActivityWebBinding;

/**
 * Created by huyg on 2020-06-10.
 */
@Route(path = "/web/web")
public class WebViewActivity extends BaseActivity<ActivityWebBinding> {

    private String url;
    private String title;

    @Override
    protected void init() {
        initIntent();
        initWebView();
    }

    private void initIntent() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    private void initWebView() {
        initSetting();
        mTitle.setText(title);
        mBinding.webview.loadUrl(url);
    }


    @SuppressLint({"SetJavaScriptEnabled"})
    private void initSetting() {
        WebSettings settings = mBinding.webview.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mBinding.webview.setWebViewClient(new MyWebViewClient());
        mBinding.webview.clearCache(true);
        mBinding.webview.addJavascriptInterface(new JsInterface(), "bridge");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(getClass().getSimpleName(),url);
            showProgress();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(getClass().getSimpleName(),url);
            closeProgress();

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

    private class JsInterface {

        @JavascriptInterface
        public void saveData(String tempLeft,String tempright) {
            toast(tempLeft+"----"+tempright);
            Intent intent = new Intent();
            intent.putExtra("left",tempLeft);
            intent.putExtra("right",tempright);
            setResult(888,intent);
            finish();
        }

        @JavascriptInterface
        public void saveCalibration(String coin){
            SpManager.getInstance(mContext).putSP("coin",coin);
        }

        @JavascriptInterface
        public void start(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.webview.loadUrl("javascript:setCalibration("+ SpManager.getInstance(mContext).getSP("coin")+")");
                }
            });

        }

        @JavascriptInterface
        public void repeat(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.webview.loadUrl("javascript:repeat("+ SpManager.getInstance(mContext).getSP("coin")+")");
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.webview.canGoBack()) {
            mBinding.webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
