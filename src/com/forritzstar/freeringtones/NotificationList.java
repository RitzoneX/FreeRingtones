package com.forritzstar.freeringtones;

import android.media.AudioManager;

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

}
