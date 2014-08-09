package com.forritzstar.my;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.forritzstar.tool.Setting;

//首先创建Receiver
public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Setting.changeAlarm(context);
//		changeRingtone(context);
		updateSharedPreferences(context);
	}

	private void updateSharedPreferences(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit()
				.putLong("alarm",
						System.currentTimeMillis() + AlarmManager.INTERVAL_DAY)
				.commit();
	}

//	private void changeRingtone(Context context) {
//		Ringtone ringtone = new Ring(context, DBHelper.ALARMS).next();
//		if (ringtone.getUri() != null)
//			RingtoneManager.setActualDefaultRingtoneUri(context,
//					RingtoneManager.TYPE_ALARM, ringtone.getUri());
//	}
}
