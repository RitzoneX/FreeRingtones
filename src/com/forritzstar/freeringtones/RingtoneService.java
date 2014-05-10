package com.forritzstar.freeringtones;

import com.forritzstar.my.RingtoneReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class RingtoneService extends Service {
	private BroadcastReceiver receiver = new RingtoneReceiver();
	private KuGuo kuGuo;

	@Override
	public void onCreate() {
		super.onCreate();

		registerReceiver(receiver, new IntentFilter(
				"android.intent.action.PHONE_STATE"));

		kuguo();
	}

	@Override
	public void onDestroy() {
		kuGuo.stopMessage();
		unregisterReceiver(receiver);
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
