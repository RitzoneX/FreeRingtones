package com.forritzstar.freeringtones;

import android.media.AudioManager;
import android.media.RingtoneManager;

import com.forritzstar.dao.DBHelper;

public class NotificationList extends RingtonesList {

	@Override
	protected String getTableName() {
		return DBHelper.NOTIFICATIONS;
	}

	@Override
	protected int getStreamType() {
		return AudioManager.STREAM_NOTIFICATION;
	}

	@Override
	protected int getType() {
		return RingtoneManager.TYPE_NOTIFICATION;
	}

}
