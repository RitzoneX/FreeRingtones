package com.forritzstar.freeringtones.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.forritzstar.freeringtones.ui.SettingsActivity;

public class ServiceManager {

	public static void startServices(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
//		if (!preferences.getString(SettingsActivity.PREF_KEY_RINGTONE_MODE, "")
//				.equals(Share.MODE_DEFAULT))
//			context.startService(new Intent(context, RingtoneService.class));
		if (!preferences.getString(SettingsActivity.PREF_KEY_NOTIFICATION_MODE, "")
				.equals(Share.MODE_DEFAULT))
			context.startService(new Intent(context, NotificationService.class));
		if (!preferences.getString(SettingsActivity.PREF_KEY_ALARM_MODE, "")
				.equals(Share.MODE_DEFAULT))
			context.startService(new Intent(context, AlarmService.class));
	}
}
