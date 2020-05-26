package cn.looksafe.client.tools;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

import cn.looksafe.client.beans.HttpBean;

public class Contents {
    private String token = "test";
    private static Contents instance = null;
    private String loginname = "test";
    private List<Activity> mList = new LinkedList<Activity>();
    private HttpBean userinfo;
    private String WX_APP_ID="wxb4ba3c02aa476ea1";
    public static Contents getInstance() {
        if (instance == null) {
            instance = new Contents();
        }
        return instance;
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    public void addActivity(Activity activity) {
        String nowAct = activity.getLocalClassName();
        for (int i = 0; i < mList.size(); i++) {
            Activity at = mList.get(i);
            if ((at != null) && (at.getLocalClassName().equals(nowAct))) {
                at.finish();
                mList.remove(i);
                mList.add(activity);
                return;
            }
        }
        mList.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
        String nowAct = activity.getLocalClassName();
        for (int i = (mList.size() - 1); i > -1; i--) {
            Activity at = mList.get(i);
            if ((at != null) && (at.getLocalClassName().equals(nowAct))) {
                at.finish();
                mList.remove(i);
                return;
            }
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public HttpBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(HttpBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getWX_APP_ID() {
        return WX_APP_ID;
    }

    public void setWX_APP_ID(String WX_APP_ID) {
        this.WX_APP_ID = WX_APP_ID;
    }
}
