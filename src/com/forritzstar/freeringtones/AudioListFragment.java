package com.forritzstar.freeringtones;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.my.KuGuo;

public abstract class AudioListFragment extends MyListFragment {
	private BannerView bannerView;
	// 查询列表名
	protected String[] projection = new String[] { MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };

	@Override
	ListAdapter getAdapter() {
		RingtoneDAO dao = new RingtoneDAO(getActivity(), RingList.tableName);
		return new AudioListAdapter(getActivity(), getCursor(), dao);
	}

	abstract Cursor getCursor();

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment_audio, container,
				false);
		bannerView = (BannerView) view.findViewById(R.id.banner);
		bannerView.showBanner(KuGuo.COO_ID, KuGuo.CHANNEL_ID);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bannerView != null) {
			bannerView.finishBanner();
		}
	}

	private class AudioListAdapter extends MyAdapter {
		private ArrayList<Integer> ids = new ArrayList<Integer>();

		public AudioListAdapter(Context context, Cursor c, RingtoneDAO dao) {
			super(context, c, dao);
			Cursor cursor = dao.getAll();
			int i = cursor.getColumnIndex("_id");
			while (cursor.moveToNext())
				ids.add(cursor.getInt(i));
			cursor.close();
		}

		@Override
		int getMenuRes() {
			return R.menu.audio_pager_action_mode;
		}

		@Override
		boolean getEnabled(Integer id) {
			return !ids.contains(id);
		}

		@Override
		void closeAD() {
			bannerView.stopBanner();
			bannerView.setVisibility(View.GONE);
		}

		@Override
		void showAD() {
			bannerView.showBanner(KuGuo.COO_ID, KuGuo.CHANNEL_ID);
		}

	}

	public static class RingtoneListFragment extends AudioListFragment {

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
					MediaStore.Audio.Media.IS_RINGTONE + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

	}

	public static class NotificationListFragment extends AudioListFragment {

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
					MediaStore.Audio.Media.IS_NOTIFICATION + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

	}

	public static class AlarmListFragment extends AudioListFragment {

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
					MediaStore.Audio.Media.IS_ALARM + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

	}

	public static class SDListFragment extends AudioListFragment {

		@Override
		Cursor getCursor() {
			return getActivity().getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
					MediaStore.Audio.Media.DURATION + "!= ?",
					new String[] { "0" },
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}

	}

}
