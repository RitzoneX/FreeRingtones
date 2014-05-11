package com.forritzstar.freeringtones;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.freeringtones.AddRingtonesFragment.AlarmFragment;
import com.forritzstar.freeringtones.AddRingtonesFragment.NotificationFragment;
import com.forritzstar.freeringtones.AddRingtonesFragment.RingtoneFragment;
import com.forritzstar.freeringtones.AddRingtonesFragment.SDFragment;
import com.umeng.analytics.MobclickAgent;

public class TabRingtones extends FragmentActivity {
	private ViewPager pager;
	private String tableName;

	// private RingtoneDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_ringtones);
		init();
	}

	private void init() {
		Toast.makeText(this, "左右滑动切换列表", Toast.LENGTH_SHORT).show();
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager()));

		String tableName = getIntent().getStringExtra("table_name");
		// dao = new RingtoneDAO(this, tableName);
	}

	public String getTableName() {
		if (tableName == null)
			tableName = getIntent().getStringExtra("table_name");
		return tableName;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// dao.closeDB();
		super.onDestroy();
	}

	private class MyAdapter extends FragmentPagerAdapter {
		private List<String> titles = Arrays.asList("系统自带", "SD卡");

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				if (getTableName().equals(DBHelper.RINGTONES))
					return new RingtoneFragment();
				else if (getTableName().equals(DBHelper.NOTIFICATIONS))
					return new NotificationFragment();
				else if (getTableName().equals(DBHelper.ALARMS))
					return new AlarmFragment();
			case 1:
				return new SDFragment();
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}
	}

}
