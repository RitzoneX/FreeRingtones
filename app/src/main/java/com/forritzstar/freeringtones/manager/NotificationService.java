package com.forritzstar.freeringtones.manager;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;

import com.forritzstar.freeringtones.util.Setting;


public class NotificationService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();

		// onCreate函数中注册内容观察者：
		getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, observer);
	}

	@Override
	public void onDestroy() {
		// onDestroy函数中记得注销：
		getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private ContentObserver observer = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			Setting.changeNotification(NotificationService.this);
		}
	};

}
