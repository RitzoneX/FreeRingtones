package com.forritzstar.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultSharedPreferences {
	private Context context;
	private SharedPreferences prefs;

	public DefaultSharedPreferences(Context context) {
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public boolean getBoolean(int resId) {
		return getBoolean(context.getString(resId));
	}

	public boolean getBoolean(String key) {
		return prefs.getBoolean(key, false);
	}

	public int getInt(int resId) {
		return getInt(context.getString(resId));
	}

	public int getInt(String key) {
		return prefs.getInt(key, 0);
	}

	public long getLong(String key) {
		return prefs.getLong(key, 0);
	}

	public String getString(int resId) {
		return getString(context.getString(resId));
	}

	public String getString(String key) {
		return prefs.getString(key, "");
	}
}
