package com.forritzstar.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.forritzstar.tool.ServiceTool;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceTool.ifStartService(context);
	}

}
