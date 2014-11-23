package com.forritzstar.freeringtones.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.forritzstar.freeringtones.util.Setting;


public class RingtoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
            if (MyPreference.ringtoneModeOn() && MyPreference.ringtoneEachOn())
                changeRingtone(context);
        } else {
            Setting.changeRingtone(context);
            updateSharedPreferences();
        }
    }

    private void updateSharedPreferences() {
        MyPreference.putLong(MyPreference.PREF_RINGTONE_TRIGGER, System.currentTimeMillis() +
                MyPreference.getRingtoneInterval());
    }

    private void changeRingtone(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (teleManager.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING: // 响铃
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: // 接听
                break;
            case TelephonyManager.CALL_STATE_IDLE: // 挂断
                Setting.changeRingtone(context);
                break;
        }
    }
}
