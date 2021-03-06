// GENERATED BY UNITY. REMOVE THIS COMMENT TO PREVENT OVERWRITING WHEN EXPORTING AGAIN
package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents {
    protected UnityPlayer mUnityPlayer;
    private String url;
    private FrameLayout mRoot;
    private static final String TAG = "UnityPlayerActivity";


    // don't change the name of this variable; referenced from native code
    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    // Setup activity layout
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);
        url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_player);
        mRoot = findViewById(R.id.root);
        initView();
    }

    private void initView() {
        mUnityPlayer = new UnityPlayer(this, this);
        FrameLayout.LayoutParams playerParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(100, 100);
        if (isAllScreenDevice(this) && isHUAWEI()) {
            playerParam.leftMargin = 80;
            params.leftMargin = 80;
        }
        mRoot.addView(mUnityPlayer, playerParam);

        params.gravity = Gravity.LEFT | Gravity.TOP;
        Button button = new Button(this);
        button.setBackgroundColor(Color.TRANSPARENT);
        mRoot.addView(button, params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        mUnityPlayer.requestFocus();
    }

    protected void hideBottomUIMenu() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

    public String getUrl() {
        return url;
    }

    public void finishAc() {
        finish();
        setResult(999);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // When Unity player unloaded move task to background
    @Override
    public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // When Unity player quited kill process
    @Override
    public void onUnityPlayerQuitted() {
        Process.killProcess(Process.myPid());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {

        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        mUnityPlayer.pause();
        super.onPause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        mUnityPlayer.resume();
        super.onResume();

    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        mUnityPlayer.lowMemory();
        super.onLowMemory();

    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mUnityPlayer.configurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);

    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        if (paramKeyEvent.getKeyCode() == 4) {
            finish();
            return true;
        }
        return (paramKeyEvent.getAction() == 2) ? this.mUnityPlayer.injectEvent((InputEvent) paramKeyEvent) : super.dispatchKeyEvent(paramKeyEvent);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice(Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }

    public static boolean isHUAWEI() {
        return ("Huawei").equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }
}
