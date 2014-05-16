package com.forritzstar.freeringtones;

import android.app.Activity;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.forritzstar.dao.Ringtone;
import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.my.MyPlayer;

public abstract class AddRingtonesFragment extends ListFragment {
	private MyPlayer player;
	private AddRingtonesAdapter adapter;
	private static RingtoneDAO dao;
	private KuGuo kuGuo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		player = new MyPlayer(getActivity());
		dao = ((TabRingtones) getActivity()).dao;
		adapter = new AddRingtonesAdapter(this, getCursor());

		setStreamType(player);
	}

	protected abstract void setStreamType(MyPlayer player);

	/**
	 * 酷果
	 */
	private void kuguo() {
		kuGuo = new KuGuo(this);
		kuGuo.showBanner();
	}

	abstract Cursor getCursor();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_add_ringtones, container,
				false);
		setListeners(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		kuguo();
		setListAdapter(adapter);
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 查找按钮并添加事件
	 */
	private void setListeners(View view) {
		view.findViewById(R.id.btn_add).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dao.add(adapter.getRingtones());
						getActivity().setResult(Activity.RESULT_OK);
						getActivity().finish();
					}
				});

		OnClickListener listener = new MyListener(adapter);
		view.findViewById(R.id.btn_select_all).setOnClickListener(listener);
		view.findViewById(R.id.btn_select_fan).setOnClickListener(listener);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Uri uri = Uri.parse(getUri() + "/" + id);
		player.playOrPause(uri);
	}

	abstract Uri getUri();

	@Override
	public void onStop() {
		player.stop();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		kuGuo.finishBanner();
		super.onDestroy();
	}

	private static class AddRingtonesAdapter extends MyAdapter {

		public AddRingtonesAdapter(AddRingtonesFragment fragment, Cursor c) {
			super(fragment, c, new MyListStatus(c, fragment));
		}

		@Override
		void setEnabled(CheckBox chk) {
			MyListStatus listStatus = (MyListStatus) getListStatus();
			chk.setEnabled(listStatus.isEnabled());
		}

		private static class MyListStatus extends ListStatus {
			// 保存CheckBox的编辑状况
			private SparseBooleanArray isEnabled = new SparseBooleanArray();
			private AddRingtonesFragment fragment;

			public MyListStatus(Cursor cursor, AddRingtonesFragment fragment) {
				super(cursor);
				this.fragment = fragment;
			}

			@Override
			public void setChecked(Ringtone ringtone, boolean checked) {
				super.setChecked(ringtone, checked);
				if (checked) {
					if (getRingtones().size() == 1) {
						fragment.getView().findViewById(R.id.layout_ad)
								.setVisibility(View.GONE);
						fragment.getView().findViewById(R.id.layout_btns)
								.setVisibility(View.VISIBLE);
					}
				} else if (getRingtones().isEmpty()) {
					fragment.getView().findViewById(R.id.layout_ad)
							.setVisibility(View.VISIBLE);
					fragment.getView().findViewById(R.id.layout_btns)
							.setVisibility(View.GONE);
				}
			}

			@Override
			void selectAll() {
				getCursor().moveToPosition(-1);
				while (getCursor().moveToNext()) {
					refreshRingtone();
					if (isEnabled() && !isChecked())
						setChecked(true);
				}
			}

			@Override
			void selectFan() {
				getCursor().moveToPosition(-1);
				while (getCursor().moveToNext()) {
					refreshRingtone();
					if (isEnabled())
						setChecked(!isChecked());
				}
			}

			public boolean isEnabled() {
				int id = ringtone.getId();
				if (isEnabled.indexOfKey(id) < 0)
					isEnabled.put(id, !dao.contains(ringtone));
				return isEnabled.get(id);
			}

			@Override
			String getUri() {
				return fragment.getUri() + "/" + getId();
			}
		}
	}

	public static class RingtoneFragment extends AddRingtonesFragment {
		private static final Uri URI = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

		@Override
		Uri getUri() {
			return URI;
		}

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					URI,
					new String[] { MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.TITLE },
					MediaStore.Audio.Media.IS_RINGTONE + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

		@Override
		protected void setStreamType(MyPlayer player) {
			player.setAudioStreamType(getStreamType());
			getActivity().setVolumeControlStream(getStreamType());
		}

		private int getStreamType() {
			return AudioManager.STREAM_RING;
		}

	}

	public static class NotificationFragment extends AddRingtonesFragment {
		private static final Uri URI = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

		@Override
		Uri getUri() {
			return URI;
		}

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					URI,
					new String[] { MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.TITLE },
					MediaStore.Audio.Media.IS_NOTIFICATION + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

		@Override
		protected void setStreamType(MyPlayer player) {
			player.setAudioStreamType(getStreamType());
			getActivity().setVolumeControlStream(getStreamType());
		}

		private int getStreamType() {
			return AudioManager.STREAM_NOTIFICATION;
		}

	}

	public static class AlarmFragment extends AddRingtonesFragment {
		private static final Uri URI = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

		@Override
		Uri getUri() {
			return URI;
		}

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					URI,
					new String[] { MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.TITLE },
					MediaStore.Audio.Media.IS_ALARM + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

		@Override
		protected void setStreamType(MyPlayer player) {
			player.setAudioStreamType(getStreamType());
			getActivity().setVolumeControlStream(getStreamType());
		}

		private int getStreamType() {
			return AudioManager.STREAM_ALARM;
		}

	}

	public static class SDFragment extends AddRingtonesFragment {
		private static final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		@Override
		Uri getUri() {
			return URI;
		}

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					URI,
					new String[] { MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.TITLE }, null, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

		@Override
		protected void setStreamType(MyPlayer player) {
		}

	}

}
