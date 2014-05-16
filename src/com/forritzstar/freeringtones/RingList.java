package com.forritzstar.freeringtones;

import android.media.AudioManager;
import android.media.RingtoneManager;

import com.forritzstar.dao.DBHelper;

public class RingList extends RingtonesList {

	@Override
	protected String getTableName() {
		return DBHelper.RINGTONES;
	}

	@Override
	protected int getStreamType() {
		return AudioManager.STREAM_RING;
	}

	@Override
	protected int getType() {
		return RingtoneManager.TYPE_RINGTONE;
	}

}
