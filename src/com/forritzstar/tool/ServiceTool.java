package com.forritzstar.tool;

import android.content.Context;
import android.content.Intent;

import com.forritzstar.freeringtones.AlarmService;
import com.forritzstar.freeringtones.MainActivity;
import com.forritzstar.freeringtones.NotificationService;
import com.forritzstar.freeringtones.R;
import com.forritzstar.freeringtones.RingtoneService;

public class ServiceTool {
	/**
	 * 根据SharedPreferences判断是否启动Service
	 * 
	 * @param context
	 */
	public static void ifStartService(Context context) {
		DefaultSharedPreferences sharedPreferences = new DefaultSharedPreferences(
				context);
		if (!sharedPreferences.getString(R.string.key_ringtone_mode).equals(
				MainActivity.MODE_DEFAULT))
			context.startService(new Intent(context, RingtoneService.class));
		if (!sharedPreferences.getString(R.string.key_notification_mode)
				.equals(MainActivity.MODE_DEFAULT))
			context.startService(new Intent(context, NotificationService.class));
		if (!sharedPreferences.getString(R.string.key_alarm_mode).equals(
				MainActivity.MODE_DEFAULT))
			context.startService(new Intent(context, AlarmService.class));
	}
}
