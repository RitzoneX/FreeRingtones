package com.forritzstar.freeringtones.manager;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.forritzstar.freeringtones.dao.DBHelper;
import com.forritzstar.freeringtones.dao.Ringtone;
import com.forritzstar.freeringtones.dao.RingtoneDAO;
import com.forritzstar.freeringtones.ui.SettingsActivity;

/**
 * 切换铃声
 * 
 * @author Administrator
 * 
 */
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

	/**
	 * 返回下一首铃声
	 * 
	 * @return
	 */
	public Ringtone next() {
		if (cursor.getCount() == 0)
			return null;
		cursor.moveToPosition(getPosition());
        Ringtone ringtone = new Ringtone(cursor);
		cursor.close();
		dao.closeDB();
		return ringtone;
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
		if (uri != null && uri.getScheme().equals("content")) {
			long id = ContentUris.parseId(uri);
			return (getRow(id) + 1) % cursor.getCount();
		} else
			return 0;
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
			return SettingsActivity.PREF_KEY_RINGTONE_MODE;
		else if (tableName.equals(DBHelper.NOTIFICATIONS))
			return SettingsActivity.PREF_KEY_NOTIFICATION_MODE;
		else
			return SettingsActivity.PREF_KEY_ALARM_MODE;
	}

}
