package com.look.core.module;

import android.app.Application;
import android.content.Context;

/**
 * Created by huyg on 2019/1/30.
 */
public interface IAppLife {


    void attachBaseContext(Context context);

    void onCreate(Application application);

    void onTerminate(Application application);


}
