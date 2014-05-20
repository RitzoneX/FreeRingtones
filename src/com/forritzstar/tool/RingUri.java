package com.forritzstar.tool;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.freeringtones.R;
import com.forritzstar.my.Share;

public class RingUri {

	private String tableName;
	private Context context;
	private RingtoneDAO dao;
	private Cursor cursor;

	public RingUri(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
		dao = new RingtoneDAO(context, tableName);
		cursor = dao.getAll();
	}

	public Uri next() {
		if (cursor.getCount() == 0)
			return null;

		cursor.moveToPosition(getPosition());
		String uri = cursor.getString(cursor.getColumnIndex("uri"));

		cursor.close();
		dao.closeDB();
		return Uri.parse(uri);
	}

	private int getPosition() {
		String mode = new DefaultSharedPreferences(context).getString(getKey());
		if (mode.equals(Share.MODE_RANDOM))
			return random();
		else
			return loop();
	}

	private int getKey() {
		if (tableName.equals(DBHelper.RINGTONES))
			return R.string.key_ringtone_mode;
		else if (tableName.equals(DBHelper.NOTIFICATIONS))
			return R.string.key_notification_mode;
		else
			return R.string.key_alarm_mode;
	}

	private int loop() {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context,
				getType());
		return (getRow(uri) + 1) % cursor.getCount();
	}

	private int getRow(Uri uri) {
		int uriIndex = cursor.getColumnIndex("uri");
		while (cursor.moveToNext())
			if (cursor.getString(uriIndex).equals(uri.toString()))
				return cursor.getPosition();
		return -1;
	}

	private int getType() {
		if (tableName.equals(DBHelper.RINGTONES))
			return RingtoneManager.TYPE_RINGTONE;
		else if (tableName.equals(DBHelper.NOTIFICATIONS))
			return RingtoneManager.TYPE_NOTIFICATION;
		else
			return RingtoneManager.TYPE_ALARM;
	}

	private int random() {
		return (int) (Math.random() * cursor.getCount());
	}
}
