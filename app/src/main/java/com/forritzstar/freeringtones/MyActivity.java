package com.forritzstar.freeringtones;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zzz on 14-11-18.
 */
public class MyActivity extends Activity {

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
