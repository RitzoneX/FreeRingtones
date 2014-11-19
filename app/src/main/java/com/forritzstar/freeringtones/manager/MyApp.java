package com.forritzstar.freeringtones.manager;

import android.app.Application;

/**
 * Created by zzz on 14-11-17.
 */
public class MyApp extends Application {
    public static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
