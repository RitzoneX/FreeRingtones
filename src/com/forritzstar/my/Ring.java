package com.forritzstar.my;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.freeringtones.MainActivity;

public class Ring {
	private String tableName;
	private RingtoneDAO dao;
	private SharedPreferences preferences;
	private Cursor cursor;
	private Context context;

	public Ring(Context context, String tableName) {
		this.tableName = tableName;
		this.context = context;
		dao = new RingtoneDAO(context, tableName);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		cursor = dao.getAll();
	}

	public Uri next() {
		if (cursor.getCount() == 0)
			return null;
		cursor.moveToPosition(getPosition());
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		String path = cursor.getString(cursor.getColumnIndex("_data"));
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(path); 
		uri = ContentUris.withAppendedId(uri, id);

		cursor.close();
		dao.closeDB();
		return uri;
	}

	private int getPosition() {
		String mode = preferences.getString(getKey(), "");
		if (mode.equals(Share.MODE_RANDOM))
			return random();
		else
			return loop();
	}

	private int loop() {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context,
				getType());
		long id = ContentUris.parseId(uri);
		return (getRow(id) + 1) % cursor.getCount();
	}

	private int getRow(long id) {
		int index = cursor.getColumnIndex("_id");
		while (cursor.moveToNext())
			if (cursor.getLong(index) == id)
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

	private String getKey() {
		if (tableName.equals(DBHelper.RINGTONES))
			return MainActivity.PREF_KEY_RINGTONE_MODE;
		else if (tableName.equals(DBHelper.NOTIFICATIONS))
			return MainActivity.PREF_KEY_NOTIFICATION_MODE;
		else
			return MainActivity.PREF_KEY_ALARM_MODE;
	}

}
