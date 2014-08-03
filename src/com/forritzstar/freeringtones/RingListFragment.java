package com.forritzstar.freeringtones;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.forritzstar.dao.DBHelper;
import com.forritzstar.dao.RingtoneDAO;
import com.forritzstar.my.KuGuo;

public abstract class RingListFragment extends MyListFragment {

	private RingListAdapter adapter;
	private BannerView bannerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ringlist_fragment, container,
				false);
		bannerView = (BannerView) view.findViewById(R.id.banner);
		bannerView.showBanner(KuGuo.COO_ID, KuGuo.CHANNEL_ID);
		return view;
	}

	@Override
	ListAdapter getAdapter() {
		RingtoneDAO dao = new RingtoneDAO(getActivity(), getTableName());
		adapter = new RingListAdapter(getActivity(), dao.getAll(), dao);
		return adapter;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bannerView != null) {
			bannerView.finishBanner();
		}
	}

	public void requery() {
		adapter.getCursor().requery();
	}

	abstract String getTableName();

	private class RingListAdapter extends MyAdapter {

		public RingListAdapter(Context context, Cursor c, RingtoneDAO dao) {
			super(context, c, dao);
		}

		@Override
		int getMenuRes() {
			return R.menu.ring_list_action_mode;
		}

		@Override
		boolean getEnabled(Integer id) {
			return true;
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

	public static class RingtoneListFragment extends RingListFragment {

		@Override
		String getTableName() {
			return DBHelper.RINGTONES;
		}

	}

	public static class NotificationListFragment extends RingListFragment {

		@Override
		String getTableName() {
			return DBHelper.NOTIFICATIONS;
		}

	}

	public static class AlarmListFragment extends RingListFragment {

		@Override
		String getTableName() {
			return DBHelper.ALARMS;
		}

	}

}
