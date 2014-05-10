package com.forritzstar.dao;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RingtoneDAO {
	private DBHelper helper;
	private SQLiteDatabase db;
	private String tableName;

	private static RingtoneDAO dao;

	/**
	 * 
	 * @param context
	 * @param tableName
	 *            表格名字
	 */
	public RingtoneDAO(Context context, String tableName) {
		this.tableName = tableName;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 工厂
	 * 
	 * @param context
	 * @param tableName
	 * @return
	 */
	public static RingtoneDAO create(Context context, String tableName) {
		return dao = new RingtoneDAO(context, tableName);
	}

	public static RingtoneDAO getDao() {
		return dao;
	}

	/**
	 * 添加纪录
	 * 
	 * @param ringtone
	 */
	public void add(Ringtone ringtone) {
		db.execSQL("insert into " + tableName + " (title, uri) values(?,?)",
				new Object[] { ringtone.getTitle(), ringtone.getUri() });
	}

	/**
	 * 添加列表
	 * 
	 * @param ringtones
	 */
	public void add(List<Ringtone> ringtones) {
		db.beginTransaction(); // 开始事务
		for (Ringtone ringtone : ringtones)
			add(ringtone);
		db.setTransactionSuccessful(); // 设置事务成功完成
		db.endTransaction(); // 结束事务
	}

	public void delete(List<Ringtone> ringtones) {
		Integer[] ids = new Integer[ringtones.size()];
		for (int i = 0; i < ids.length; i++)
			ids[i] = ringtones.get(i).getId();
		delete(ids);
	}
	
	/**
	 * 使用不同的方法删除记录1到多条记录
	 * 
	 * @param ids
	 */
	public void delete(Integer... ids) {
		if (ids.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				sb.append('?').append(',');
			}
			// 删除最后一个元素
			sb.deleteCharAt(sb.length() - 1);
			db.execSQL("delete from " + tableName + " where _id in (" + sb
					+ ")", (Object[]) ids);
		}
	}

	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	public Ringtone find(int id) {
		Cursor cursor = db.rawQuery("select * from " + tableName
				+ " where _id = ?", new String[] { id + "" });
		Ringtone ringtone = null;
		if (cursor.moveToNext()) {
			ringtone = new Ringtone(
					cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("title")),
					cursor.getString(cursor.getColumnIndex("uri")));
		}
		cursor.close();
		return ringtone;
	}

	/**
	 * 查询有没包含ringtone
	 * 
	 * @param ringtone
	 * @return
	 */
	public boolean contains(Ringtone ringtone) {
		Cursor cursor = db.rawQuery("select * from " + tableName
				+ " where uri = ?", new String[] { ringtone.getUri() });
		boolean b = cursor.getCount() != 0;
		cursor.close();
		return b;
	}

	/**
	 * 查找所有的记录
	 * 
	 * @return
	 */
	public Cursor getAll() {
		return db.rawQuery("select * from " + tableName, null);
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
