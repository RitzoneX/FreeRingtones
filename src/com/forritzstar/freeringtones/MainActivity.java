package com.forritzstar.freeringtones;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.forritzstar.my.AlarmService;
import com.forritzstar.my.NotificationService;
import com.forritzstar.my.RingtoneService;
import com.forritzstar.my.ServiceCtrl;
import com.forritzstar.my.Share;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {
	public static String PREF_KEY_RINGTONE_MODE = "pref_key_ringtone_mode";
	public static String PREF_KEY_NOTIFICATION_MODE = "pref_key_notification_mode";
	public static String PREF_KEY_ALARM_MODE = "pref_key_alarm_mode";
	public static String PREF_KEY_RINGTONE = "pref_key_ringtone";
	public static String PREF_KEY_NOTIFICATION = "pref_key_notification";
	public static String PREF_KEY_ALARM = "pref_key_alarm";

	private FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		// getFragmentManager().beginTransaction()
		// .replace(android.R.id.content, new SettingsFragment()).commit();
		setContentView(R.layout.activity_main);
		ServiceCtrl.startServices(this);
		umeng();
	}

	private void umeng() {
		UmengUpdateAgent.update(this);
		agent = new FeedbackAgent(this);
		agent.sync();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Set up ShareActionProvider's default share intent
		MenuItem shareItem = menu.findItem(R.id.action_share);
		ShareActionProvider mShareActionProvider = (ShareActionProvider) shareItem
				.getActionProvider();
		mShareActionProvider.setShareIntent(getDefaultIntent());

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 评价
	 * 
	 * @param item
	 */
	public void onComment(MenuItem item) {
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri data = Uri
				.parse("market://details?id=com.forritzstar.freeringtones");
		intent.setData(data);
		startActivity(intent);
	}

	/**
	 * 关于
	 * 
	 * @param item
	 */
	public void onAbout(MenuItem item) {
		startActivity(new Intent(this, AboutActivity.class));
	}

	/**
	 * 帮助
	 * 
	 * @param item
	 */
	public void onHelp(MenuItem item) {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	public void onFAQ(MenuItem item) {
		Intent intent = new Intent(this, FAQActivity.class);
		startActivity(intent);
	}

	public void onFeedback(MenuItem item) {

		agent.startFeedbackActivity();
	}

	/**
	 * Defines a default (dummy) share intent to initialize the action provider.
	 * However, as soon as the actual content to be used in the intent is known
	 * or changes, you must update the share intent by again calling
	 * mShareActionProvider.setShareIntent()
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, "每天都听着同一首铃声，还在为选择铃声而烦恼吗？\n"
				+ "不用怕，随意铃声帮你一次搞定，让你每天都欣赏到不同的铃声。\n" + "可以随机循环设置来电、短信、闹钟铃声。\n"
				+ "随意铃声，不一样的铃声！\n下载地址: http://app.xiaomi.com/detail/56912");
		intent.putExtra(Intent.EXTRA_TITLE, "随意铃声");
		return intent;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static class SettingsFragment extends PreferenceFragment implements
			OnPreferenceClickListener, OnSharedPreferenceChangeListener {
		private String[] entriesRingMode;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			entriesRingMode = getResources().getStringArray(
					R.array.entries_ring_mode);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);

			findPreference(PREF_KEY_RINGTONE)
					.setOnPreferenceClickListener(this);
			findPreference(PREF_KEY_NOTIFICATION).setOnPreferenceClickListener(
					this);
			findPreference(PREF_KEY_ALARM).setOnPreferenceClickListener(this);
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			if (preference.getKey().equals(PREF_KEY_RINGTONE))
				updateSummary(getPreferenceScreen().getSharedPreferences(),
						PREF_KEY_RINGTONE_MODE);
			else if (preference.getKey().equals(PREF_KEY_NOTIFICATION))
				updateSummary(getPreferenceScreen().getSharedPreferences(),
						PREF_KEY_NOTIFICATION_MODE);
			else if (preference.getKey().equals(PREF_KEY_ALARM))
				updateSummary(getPreferenceScreen().getSharedPreferences(),
						PREF_KEY_ALARM_MODE);
			return true;
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.equals(PREF_KEY_RINGTONE_MODE)
					|| key.equals(PREF_KEY_NOTIFICATION_MODE)
					|| key.equals(PREF_KEY_ALARM_MODE)) {
				String value = sharedPreferences.getString(key, "");
				Intent service = new Intent(getActivity(), getService(key));
				if (value.equals(Share.MODE_DEFAULT)) {
					getActivity().stopService(service);

				} else {
					getActivity().startService(service);
				}
				int i = updateSummary(sharedPreferences, key);
				Toast.makeText(getActivity(),
						"已切换至" + entriesRingMode[i] + "模式", Toast.LENGTH_SHORT)
						.show();
			}
		}

		private Class<?> getService(String key) {
			if (key.equals(PREF_KEY_RINGTONE_MODE))
				return RingtoneService.class;
			if (key.equals(PREF_KEY_NOTIFICATION_MODE))
				return NotificationService.class;
			if (key.equals(PREF_KEY_ALARM_MODE))
				return AlarmService.class;
			return null;
		}

		// 更新Summary
		private int updateSummary(SharedPreferences sharedPreferences,
				String key) {
			Preference connectionPref = findPreference(key);
			int i = Integer.parseInt(sharedPreferences.getString(key, ""));
			// Set summary to be the user-description for the selected value
			connectionPref.setSummary(entriesRingMode[i]);
			return i;
		}

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}

	}

}