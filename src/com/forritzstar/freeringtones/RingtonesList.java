package com.forritzstar.freeringtones;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.forritzstar.dao.Ringtone;
import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.my.MyPlayer;
import com.umeng.analytics.MobclickAgent;

/**
 * 铃声列表
 * 
 * @author Administrator
 * 
 */
public abstract class RingtonesList extends ListActivity {
	private KuGuo kuGuo;
	private static MyPlayer player;
	private RingtonesAdapter adapter;
	private static RingtoneDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_ringtones);

		init();
		setListAdapter(adapter);
	}

	private void init() {
		player = new MyPlayer(this);
		player.setAudioStreamType(getStreamType());
		dao = RingtoneDAO.create(this, getTableName());
		adapter = new RingtonesAdapter(this, dao.getAll());

		setVolumeControlStream(getStreamType());
		kuguo();
		setListeners();
	}

	protected abstract String getTableName();

	protected abstract int getStreamType();

	/**
	 * 酷果
	 */
	private void kuguo() {
		kuGuo = new KuGuo(this);
		kuGuo.showBanner();
	}

	private void setListeners() {
		findViewById(R.id.btn_add_ringtones).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(RingtonesList.this,
								TabRingtones.class);
						intent.putExtra("table_name", getTableName());
						startActivityForResult(intent, 0);
					}
				});
		findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.delete();
			}
		});

		OnClickListener listener = new MyListener(adapter);
		findViewById(R.id.btn_select_all).setOnClickListener(listener);
		findViewById(R.id.btn_select_fan).setOnClickListener(listener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			adapter.getCursor().requery();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Ringtone ringtone = dao.find((int) id);
		Uri uri = Uri.parse(ringtone.getUri());
		player.playOrPause(uri);
	}

	@Override
	protected void onStop() {
		player.stop();
		super.onStop();
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
		// 应用的最后一个Activity关闭时应释放DB
		dao.closeDB();
	}

	private static class RingtonesAdapter extends MyAdapter {

		public RingtonesAdapter(Context context, Cursor c) {
			super(context, c, new MyListStatus(context, c));
		}

		public void delete() {
			dao.delete(getRingtones());
			player.stop();
			((MyListStatus) getListStatus()).clear();
			getCursor().requery();
		}

		@Override
		void setEnabled(CheckBox chk) {
		}

		private static class MyListStatus extends ListStatus {
			private Context context;

			public MyListStatus(Context context, Cursor cursor) {
				super(cursor);
				this.context = context;
			}

			public void clear() {
				getRingtones().clear();
				hideButtons();
			}

			@Override
			public void setChecked(Ringtone ringtone, boolean checked) {
				super.setChecked(ringtone, checked);
				if (checked) {
					if (getRingtones().size() == 1)
						showButtons();
				} else if (getRingtones().isEmpty())
					hideButtons();
			}

			private void showButtons() {
				((Activity) context).findViewById(R.id.layout_ad)
						.setVisibility(View.GONE);
				((Activity) context).findViewById(R.id.layout_btns)
						.setVisibility(View.VISIBLE);
			}

			private void hideButtons() {
				((Activity) context).findViewById(R.id.layout_ad)
						.setVisibility(View.VISIBLE);
				((Activity) context).findViewById(R.id.layout_btns)
						.setVisibility(View.GONE);
			}

			@Override
			void selectAll() {
				getCursor().moveToPosition(-1);
				while (getCursor().moveToNext()) {
					refreshRingtone();
					if (!isChecked())
						setChecked(true);
				}
			}

			@Override
			void selectFan() {
				getCursor().moveToPosition(-1);
				while (getCursor().moveToNext()) {
					refreshRingtone();
					setChecked(!isChecked());
				}
			}

			@Override
			String getUri() {
				return getCursor().getString(getCursor().getColumnIndex("uri"));
			}

		}
	}

}
