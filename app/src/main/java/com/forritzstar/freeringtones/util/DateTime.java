package com.forritzstar.freeringtones.util;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

/**
 * 日期时间类
 * 
 * @author Administrator
 * 
 */
public class DateTime {
	/**
	 * 返回格式化日期
	 * 
	 * @param context
	 * @return
	 */
	public static String getDate(Context context) {
		Date date = new Date();
		DateFormat dateFormat = android.text.format.DateFormat
				.getDateFormat(context);
		return dateFormat.format(date);
	}

	/**
	 * 返回格式化时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getTime(Context context) {
		Date date = new Date();
		DateFormat dateFormat = android.text.format.DateFormat
				.getTimeFormat(context);
		return dateFormat.format(date);
	}

	public static String getDateTime(Context context) {
		return getDate(context) + " " + getTime(context);
	}

}
