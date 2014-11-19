package com.forritzstar.freeringtones.util;

import android.content.Context;
import android.media.RingtoneManager;
import android.widget.Toast;

import com.forritzstar.freeringtones.dao.DBHelper;
import com.forritzstar.freeringtones.dao.Ringtone;
import com.forritzstar.freeringtones.manager.Ring;

public class Setting {
	/**
	 * 改变系统铃声
	 * 
	 * @param context
	 */
	public static void changeRingtone(Context context) {
		changeRing(context, DBHelper.RINGTONES, RingtoneManager.TYPE_RINGTONE,
				File.RINGTONE);
	}

	/**
	 * 更改通知铃声
	 * 
	 * @param context
	 */
	public static void changeNotification(Context context) {
		changeRing(context, DBHelper.NOTIFICATIONS,
				RingtoneManager.TYPE_NOTIFICATION, File.NOTIFICATION);
	}

	/**
	 * 更改闹钟铃声
	 * 
	 * @param context
	 */
	public static void changeAlarm(Context context) {
		changeRing(context, DBHelper.ALARMS, RingtoneManager.TYPE_ALARM,
				File.ALARM);
	}

	private static void changeRing(Context context, String tableName, int type,
			String fileName) {
		Ringtone ringtone = new Ring(context, tableName).next();
		if (ringtone != null) {
			RingtoneManager.setActualDefaultRingtoneUri(context, type,
					ringtone.getUri());

//			try {
//				// 保存日志
//				String text = DateTime.getDateTime(context) + "\n铃声切换为: "
//						+ ringtone.getTitle() + "\n";
//				File.save(context, fileName, text);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
