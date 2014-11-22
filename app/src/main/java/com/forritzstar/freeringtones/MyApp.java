package com.forritzstar.freeringtones;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zzz on 14-11-17.
 */
public class MyApp extends Application {
    public static MyApp app;
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
    }
}
