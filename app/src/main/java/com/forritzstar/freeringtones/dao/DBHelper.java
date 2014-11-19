package com.forritzstar.freeringtones.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String RINGTONES = "ringtones";
	public static final String NOTIFICATIONS = "notifications";
	public static final String ALARMS = "alarms";

	private static final String NAME = "data.db";
	private static final int VERSION = 4;

	public DBHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db, RINGTONES);
		createTable(db, NOTIFICATIONS);
		createTable(db, ALARMS);
	}

	// 创建表
	private void createTable(SQLiteDatabase db, String table) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ table
				+ "(_order INTEGER PRIMARY KEY, _id INTEGER, title TEXT, _data TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			createTable(db, NOTIFICATIONS);
		case 2:
			createTable(db, ALARMS);
		case 3:
			dropTable(db, RINGTONES);
			dropTable(db, NOTIFICATIONS);
			dropTable(db, ALARMS);
			createTable(db, RINGTONES);
			createTable(db, NOTIFICATIONS);
			createTable(db, ALARMS);
		default:
			break;
		}
	}
	
	private void dropTable(SQLiteDatabase db, String table) {
		db.execSQL("DROP TABLE IF EXISTS " + table);
	}
}
