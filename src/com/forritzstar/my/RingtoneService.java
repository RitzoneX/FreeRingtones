package com.forritzstar.my;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class RingtoneService extends Service {
	private BroadcastReceiver receiver = new RingtoneReceiver();

	@Override
	public void onCreate() {
		super.onCreate();

		registerReceiver(receiver, new IntentFilter(
				"android.intent.action.PHONE_STATE"));
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
