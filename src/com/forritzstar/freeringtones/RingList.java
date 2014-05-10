package com.forritzstar.freeringtones;

import android.media.AudioManager;

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

}
