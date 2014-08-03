package com.forritzstar.freeringtones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.freeringtones.AudioPager.AlarmPager;
import com.forritzstar.freeringtones.AudioPager.NotificationPager;
import com.forritzstar.freeringtones.AudioPager.RingtonePager;
import com.umeng.analytics.MobclickAgent;

/**
 * 铃声列表
 * 
 * @author Administrator
 * 
 */

public abstract class RingList extends FragmentActivity {
	public static String tableName;
	private RingListFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResID());
		fragment = (RingListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.ring_list_fragment);
	}

	abstract int getLayoutResID();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ring_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			onOptionAdd(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onOptionAdd(MenuItem i) {
		Intent intent = new Intent(this, getPagerClass());
		startActivityForResult(intent, 0);
	}

	abstract Class<?> getPagerClass();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			fragment.requery();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static class RingtoneList extends RingList {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			tableName = DBHelper.RINGTONES;
		}

		@Override
		Class<?> getPagerClass() {
			return RingtonePager.class;
		}

		@Override
		int getLayoutResID() {
			return R.layout.fragment_ringtone_list;
		}

	}

	public static class NotificationList extends RingList {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			tableName = DBHelper.NOTIFICATIONS;
		}

		@Override
		Class<?> getPagerClass() {
			return NotificationPager.class;
		}

		@Override
		int getLayoutResID() {
			return R.layout.fragment_notification_list;
		}

	}

	public static class AlarmList extends RingList {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			tableName = DBHelper.ALARMS;
		}

		@Override
		Class<?> getPagerClass() {
			return AlarmPager.class;
		}

		@Override
		int getLayoutResID() {
			return R.layout.fragment_alarm_list;
		}

	}

}
