package com.forritzstar.freeringtones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.forritzstar.tool.ServiceTool;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MainActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {
	// 铃声模式
	public static final String MODE_RANDOM = "0";
	public static final String MODE_LOOP = "1";
	public static final String MODE_DEFAULT = "2";

	private FeedbackAgent agent;
	private KuGuo kuGuo;

	private String[] entriesRingMode;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		init();
		ServiceTool.ifStartService(this);
		umeng();
		kuguo();
	}

	private void init() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		entriesRingMode = getResources().getStringArray(
				R.array.entries_ring_mode);

		initSummary();
		setListeners();
	}

	private void initSummary() {
		setSummary(R.string.key_ringtone_mode);
		setSummary(R.string.key_notification_mode);
		setSummary(R.string.key_alarm_mode);
		findPreference(getString(R.string.key_update)).setSummary(
				"当前版本: " + getVersionName());
	}

	private void setSummary(int resId) {
		setSummary(getString(resId));
	}

	private void setSummary(String key) {
		findPreference(key).setSummary(getSummary(key));
	}

	private CharSequence getSummary(String key) {
		return ringMode(prefs.getString(key, ""));
	}

	private CharSequence ringMode(String index) {
		return entriesRingMode[Integer.parseInt(index)];
	}

	private void umeng() {
		update();
		feedbackAgent();
	}

	private void update() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					// UmengUpdateAgent.showUpdateDialog(MainActivity.this,
					// updateInfo);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(MainActivity.this, "应用程序为最新版本",
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(MainActivity.this, "没有wifi连接， 只在wifi下更新",
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(MainActivity.this, "超时,请检查网络连接",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	/**
	 * 友盟反馈
	 */
	private void feedbackAgent() {
		agent = new FeedbackAgent(this);
		agent.sync();
	}

	/**
	 * 酷果
	 */
	private void kuguo() {
		kuGuo = new KuGuo(this);
		kuGuo.showFooterBanner();
	}

	private void setListeners() {
		setChangeListener(R.string.key_ringtone_mode);
		setChangeListener(R.string.key_notification_mode);
		setChangeListener(R.string.key_alarm_mode);

		setClickListener(R.string.key_instructions_for_use);
		setClickListener(R.string.key_feedback);
		setClickListener(R.string.key_update);
		setClickListener(R.string.key_about);
		setClickListener(R.string.key_share);
	}

	private void setClickListener(int resId) {
		findPreference(getString(resId)).setOnPreferenceClickListener(this);
	}

	private void setChangeListener(int resId) {
		findPreference(getString(resId)).setOnPreferenceChangeListener(this);
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
		kuGuo.finishBanner();
		super.onDestroy();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(getString(R.string.key_feedback)))
			agent.startFeedbackActivity();
		else if (preference.getKey().equals(
				getString(R.string.key_instructions_for_use)))
			new AlertDialog.Builder(this)
					.setTitle(R.string.title_instructions_for_use)
					.setMessage(R.string.dialog_message_instructions_for_use)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(new Intent(MainActivity.this,
											InstructionsForUse.class));
								}
							}).show();
		else if (preference.getKey().equals(getString(R.string.key_about)))
			new AlertDialog.Builder(this).setTitle(R.string.title_about)
					.setMessage(R.string.dialog_message_about)
					.setPositiveButton(android.R.string.ok, null).show();
		else if (preference.getKey().equals(getString(R.string.key_share))) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT, "每天都听着同一首铃声，还在为选择铃声而烦恼吗？\n"
					+ "不用怕，随意铃声帮你一次搞定，让你每天都欣赏到不同的铃声。\n"
					+ "随意铃声，不一样的铃声！\n下载地址: http://app.xiaomi.com/detail/56912");
			intent.putExtra(Intent.EXTRA_TITLE, "随意铃声");
			startActivity(Intent.createChooser(intent, "请选择"));
		} else if (preference.getKey().equals(getString(R.string.key_update)))
			UmengUpdateAgent.forceUpdate(this);
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String toast = "";
		Intent service = null;
		if (preference.getKey().equals(getString(R.string.key_ringtone_mode))) {
			service = new Intent(this, RingtoneService.class);
			toast = "来电";
		} else if (preference.getKey().equals(
				getString(R.string.key_notification_mode))) {
			service = new Intent(this, NotificationService.class);
			toast = "短信";
		} else if (preference.getKey().equals(
				getString(R.string.key_alarm_mode))) {
			service = new Intent(this, AlarmService.class);
			toast = "闹钟";
		}

		if (newValue.equals(MODE_DEFAULT)) {
			stopService(service);
			toast = "已选择" + toast + "指定铃声";

		} else {
			startService(service);
			if (newValue.equals(MODE_RANDOM))
				toast = "已选择" + toast + "随机铃声";
			else
				toast = "已选择" + toast + "循环铃声";
		}

		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
		findPreference(preference.getKey()).setSummary(ringMode(newValue + ""));
		return true;
	}

	/**
	 * 返回当前程序版本名
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

}