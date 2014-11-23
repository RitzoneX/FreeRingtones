package com.forritzstar.freeringtones.manager;

import android.app.AlarmManager;

import com.forritzstar.freeringtones.MyApp;
import com.forritzstar.freeringtones.ui.SettingsActivity;

/**
 * Created by zzz on 14-11-20.
 */
public class MyPreference {
    private static final long[] intervals = {AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HOUR,
            AlarmManager.INTERVAL_HALF_DAY, AlarmManager.INTERVAL_DAY,
            7 * AlarmManager.INTERVAL_DAY, 30 * AlarmManager.INTERVAL_DAY};
//    static long[] intervals = {2000, 4000, 6000, 8000, 10000, 15000, 20000};

    public static final String PREF_ALARM_TRIGGER = "alarm_trigger";
    public static final String PREF_RINGTONE_TRIGGER = "ringtone_trigger";
    public static final String PREF_NOTIFICATION_TRIGGER = "notification_trigger";

    static long getLong(String key) {
        return MyApp.preferences.getLong(key, 0);
    }

//    static String getString(String key) {
//        return getString(key, "");
//    }

    static String getString(String key, String defValue) {
        return MyApp.preferences.getString(key, defValue);
    }

//    static long getInterval(String prefKey) {
//        int i = Integer.parseInt(getString(prefKey, ""));
//        return i != -1 ? intervals[i] : 0;
//    }

    static void putLong(String key, long value) {
        MyApp.preferences.edit().putLong(key, value).commit();
    }

    // 查看是否开启模式
    private static boolean isModeOn(String key) {
        String pref = getString(key, Mode.MODE_RANDOM);
        return pref.equals(Mode.MODE_RANDOM) || pref.equals(Mode.MODE_LOOP);
    }

    // 切换模式是否每一次
    private static boolean isEach(String key) {
        return getString(key, "-1").equals("-1");
    }

//    /**
//     * 来电铃声是否间隔模式
//     *
//     * @return
//     */
//    static boolean isRingtoneModeNotEach() {
//        return isModeOn(SettingsActivity.PREF_KEY_RINGTONE_MODE) &&
//                !isEach(SettingsActivity.PREF_KEY_RINGTONE_INTERVAL);
//    }

    // 是否开启模式
    static boolean ringtoneModeOn() {
        return isModeOn(SettingsActivity.PREF_KEY_RINGTONE_MODE);
    }

    // 切换间隔是否每一次
    static boolean ringtoneEachOn() {
        return isEach(SettingsActivity.PREF_KEY_RINGTONE_INTERVAL);
    }

    // 是否开启模式
    static boolean notificationModeOn() {
        return isModeOn(SettingsActivity.PREF_KEY_NOTIFICATION_MODE);
    }

    // 切换间隔是否每一次
    static boolean notificationEachOn() {
        return isEach(SettingsActivity.PREF_KEY_NOTIFICATION_INTERVAL);
    }

    // 是否开启模式
    static boolean alarmModeOn() {
        return isModeOn(SettingsActivity.PREF_KEY_ALARM_MODE);
    }

    static long getRingtoneInterval() {
        int i = Integer.parseInt(getString(SettingsActivity.PREF_KEY_RINGTONE_INTERVAL, "-1"));
        return i != -1 ? intervals[i] : 0;
    }

    static long getNotificationInterval() {
        int i = Integer.parseInt(getString(SettingsActivity.PREF_KEY_NOTIFICATION_INTERVAL, "-1"));
        return i != -1 ? intervals[i] : 0;
    }

    static long getAlarmInterval() {
        int i = Integer.parseInt(getString(SettingsActivity.PREF_KEY_ALARM_INTERVAL, "4"));
        return intervals[i];
    }
}
