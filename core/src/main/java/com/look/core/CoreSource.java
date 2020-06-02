package com.look.core;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by huyg on 2020-05-29.
 */
public class CoreSource {


    private static CoreSource mCore;
    private Context mContext;

    public static CoreSource getInstance() {
        if (mCore == null) {
            mCore = new CoreSource();
            return mCore;
        }
        return mCore;
    }


    public void init(Context context) {
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }

}
