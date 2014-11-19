package com.forritzstar.freeringtones.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.forritzstar.freeringtones.ui.SettingsActivity;
import com.forritzstar.freeringtones.util.Setting;


public class RingtoneReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getString(SettingsActivity.PREF_KEY_RINGTONE_MODE, "")
                .equals(Share.MODE_DEFAULT))
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
                Setting.changeRingtone(context);
                break;
        }
    }
}
