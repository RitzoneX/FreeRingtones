package com.forritzstar.freeringtones;

import android.media.AudioManager;
import android.media.RingtoneManager;

import com.forritzstar.dao.DBHelper;

public class AlarmList extends RingtonesList {

	@Override
	protected String getTableName() {
		return DBHelper.ALARMS;
	}

	@Override
	protected int getStreamType() {
		return AudioManager.STREAM_ALARM;
	}

	@Override
	protected int getType() {
		return RingtoneManager.TYPE_ALARM;
	}

}
