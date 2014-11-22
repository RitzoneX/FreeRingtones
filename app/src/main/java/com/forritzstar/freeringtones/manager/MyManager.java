package com.forritzstar.freeringtones.manager;

import android.content.Context;
import android.content.Intent;

import com.forritzstar.freeringtones.MyApp;

public class MyManager {

    public static void update() {
        updateRingtone();
        updateNotification();
        updateAlarm();
    }

    private static void updateAlarm() {
        if (MyPreference.alarmModeOn())
            MyAlarm.setAlarm();
        else
            MyAlarm.cancel(AlarmReceiver.class);
    }

    private static void updateNotification() {
        Context context = MyApp.app;
        Intent service = new Intent(context, NotificationService.class);
        if (MyPreference.notificationModeOn())
            if (MyPreference.notificationEachOn()) {
                context.startService(service);
                MyAlarm.cancel(NotificationReceiver.class);
            } else {
                MyAlarm.setNotification();
                context.stopService(service);
            }
        else {
            context.stopService(service);
            MyAlarm.cancel(NotificationReceiver.class);
        }
    }

    private static void updateRingtone() {
        if (MyPreference.ringtoneModeOn() && !MyPreference.ringtoneEachOn())
            MyAlarm.setRingtone();
        else
            MyAlarm.cancel(RingtoneReceiver.class);
    }

}
