package cn.looksafe.client.tools;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SPutil {
    private static SharedPreferences mSp;
    private Context mContext;

    private static volatile SPutil instance = null;

    public SPutil(Context mContext) {
        this.mContext = mContext;
        if (mSp == null)
            mSp = mContext.getSharedPreferences(AppConfig.SPNAME, MODE_PRIVATE);
    }

    public static synchronized SPutil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPutil.class) {
                if (instance == null) {
                    instance = new SPutil(context);
                }
            }
        }
        return instance;

    }

    public void setLoginname(String loginname) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString("loginname", loginname);
        editor.commit();
    }

    public String getLoginname() {
        return mSp.getString("loginname", "");
    }


    public void setLoginpwd(String pwd) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString("loginpwd", pwd);
        editor.commit();
    }

    public String getLoginpwd() {
        return mSp.getString("loginpwd", "");
    }

    public void setVersionCode(int versionCode) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt("vcode", versionCode);
        editor.commit();
    }

    public int getVersionCode() {
        return mSp.getInt("vcode", 0);
    }
}
