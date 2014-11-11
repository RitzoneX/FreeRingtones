package com.forritzstar.freeringtones;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.forritzstar.tool.File;
import com.umeng.analytics.MobclickAgent;

/**
 * 铃声日志
 * 
 * @author Administrator
 * 
 */
public abstract class LogActivity extends Activity {
	private TextView tvLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		tvLog = (TextView) findViewById(R.id.tv_log);
		try {
			tvLog.setText(File.read(this, getFileName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void clearLog(MenuItem item) {
		try {
			File.clear(this, getFileName());
			tvLog.setText(null);
			Toast.makeText(this, "清空日志成功", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "清空日志失败", Toast.LENGTH_SHORT).show();
		}
	}

	abstract String getFileName();

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static class RingtoneLogActivity extends LogActivity {

		@Override
		String getFileName() {
			return File.RINGTONE;
		}

	}

	public static class NotificationLogActivity extends LogActivity {

		@Override
		String getFileName() {
			return File.NOTIFICATION;
		}

	}

	public static class AlarmLogActivity extends LogActivity {

		@Override
		String getFileName() {
			return File.ALARM;
		}

	}
}
