package com.forritzstar.freeringtones;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.forritzstar.my.AlarmReceiver;
import com.forritzstar.tool.DefaultSharedPreferences;

public class AlarmService extends Service {
	private KuGuo kuGuo;
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

		kuguo();
	}

	private long getTriggerAtMillis() {
		long alarm = new DefaultSharedPreferences(this).getLong("alarm");
		return alarm > System.currentTimeMillis() ? alarm : System
				.currentTimeMillis();
	}

	@Override
	public void onDestroy() {
		kuGuo.stopMessage();
		alarmMgr.cancel(pendIntent);
		super.onDestroy();
	}

	private void kuguo() {
		kuGuo = new KuGuo(this);
		kuGuo.push();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
