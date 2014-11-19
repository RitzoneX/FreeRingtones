package com.forritzstar.freeringtones.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class File {
	public static final String RINGTONE = "ringtone.log";
	public static final String NOTIFICATION = "notification.log";
	public static final String ALARM = "alarm.log";

	/**
	 * 保存文件
	 * 
	 * @param context
	 * @param fileName
	 * @param text
	 * @throws java.io.IOException
	 */
	public static void save(Context context, String fileName, String text)
			throws IOException {
		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_APPEND);
		fos.write(text.getBytes());
		fos.close();
	}

	/**
	 * 读取文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 * @throws java.io.IOException
	 */
	public static String read(Context context, String fileName)
			throws IOException {
		FileInputStream fis = context.openFileInput(fileName);
		int length = fis.available();
		byte[] buffer = new byte[length];
		fis.read(buffer);
		fis.close();
		return new String(buffer);

	}

	/**
	 * 清空文件
	 * 
	 * @param context
	 * @param fileName
	 * @throws java.io.IOException
	 */
	public static void clear(Context context, String fileName)
			throws IOException {
		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		fos.close();
	}
}
