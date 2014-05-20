package com.forritzstar.freeringtones;

import java.util.Random;

import android.content.Context;

public class DaoYouDao {
//	private static final String XIAOMI = "xiaomi";
//	private static final String UMENG = "umeng";
//	private static final String ANZHI = "p-goapk";

	private static final String APPID = "1ae6d3b8227e7e7d7e55e766bf575fa8";
//	private static final String CHANNELED = ANZHI;

	private Uasd m;
	private Context context;

	public DaoYouDao(Context context) {
		this.context = context;
		m = Uasd.init(context);
	}

	public void push() {
		if (new Random().nextBoolean())
			m.g(context, APPID, 2);
	}
}
