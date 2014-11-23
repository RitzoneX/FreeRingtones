package com.forritzstar.freeringtones.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.forritzstar.freeringtones.util.Setting;


//首先创建Receiver
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Setting.changeAlarm(context);
        updateSharedPreferences();
    }

    private void updateSharedPreferences() {
        MyPreference.putLong(MyPreference.PREF_ALARM_TRIGGER,
                System.currentTimeMillis() + MyPreference.getAlarmInterval());
    }

}
