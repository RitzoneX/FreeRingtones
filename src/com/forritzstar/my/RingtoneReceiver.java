package com.forritzstar.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.tool.RingUri;

public class RingtoneReceiver extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		changeRingtone();
	}

	private void changeRingtone() {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (teleManager.getCallState()) {
		case TelephonyManager.CALL_STATE_RINGING: // 响铃
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // 接听
			break;
		case TelephonyManager.CALL_STATE_IDLE: // 挂断
			Uri uri = new RingUri(context, DBHelper.RINGTONES).next();
			if (uri != null)
				RingtoneManager.setActualDefaultRingtoneUri(context,
						RingtoneManager.TYPE_RINGTONE, uri);
			break;
		}
	}

}
