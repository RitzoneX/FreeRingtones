package com.forritzstar.freeringtones.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.forritzstar.freeringtones.util.Setting;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Setting.changeNotification(context);
        updateSharedPreferences();
    }

    private void updateSharedPreferences() {
        MyPreference.putLong(MyPreference.PREF_NOTIFICATION_TRIGGER, System.currentTimeMillis() +
                MyPreference.getNotificationInterval());
    }
}
