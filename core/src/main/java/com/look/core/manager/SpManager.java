package com.look.core.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huyg on 2019/2/13.
 */
public class SpManager {

    public static final String mTAG = "test";
    // 创建一个写入器
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SpManager mSpManager;


    // 构造方法
    public SpManager(Context context) {
        mPreferences = context.getSharedPreferences(mTAG, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    // 单例模式
    public static SpManager getInstance(Context context) {
        if (mSpManager == null) {
            mSpManager = new SpManager(context);
        }
        return mSpManager;
    }

    // 存入数据
    public void putSP(String key, Object value) {
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            mEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean){
            mEditor.putBoolean(key,(Boolean) value);
        } else if (value instanceof Float){
            mEditor.putFloat(key,(Float) value);
        } else if (value instanceof Long){
            mEditor.putLong(key,(Long) value);
        }
        mEditor.commit();
    }

    public void putIntSP(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    // 获取数据
    public String getSP(String key) {
        return mPreferences.getString(key, "");
    }

    public int getIntSP(String key) {
        return mPreferences.getInt(key, 0);
    }


    public long getLongSP(String key) {
        return mPreferences.getLong(key, 0);
    }
    public Boolean getBooleanSp(String key) {
        return mPreferences.getBoolean(key, true);
    }

    // 移除数据
    public void removeSP(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    // 移除所有数据
    public void clear() {
        mPreferences.edit().clear().commit();
    }
}
