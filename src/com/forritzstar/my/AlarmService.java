package com.forritzstar.my;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class AlarmService extends Service {
	private AlarmManager alarmMgr;
	private PendingIntent pendIntent;

	@Override
	public void onCreate() {
		super.onCreate();

		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		int requestCode = 0;
		pendIntent = PendingIntent.getBroadcast(getApplicationContext(),
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmMgr.setInexactRepeating(AlarmManager.RTC, getTriggerAtMillis(),
				AlarmManager.INTERVAL_DAY, pendIntent);
	}

	private long getTriggerAtMillis() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		long alarm = preferences.getLong("alarm", 0);
		return alarm > System.currentTimeMillis() ? alarm : System
				.currentTimeMillis();
	}

	@Override
	public void onDestroy() {
		alarmMgr.cancel(pendIntent);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
