package com.forritzstar.freeringtones;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.forritzstar.freeringtones.MyAdapter.ViewHolder;

public abstract class MyListFragment extends ListFragment {
	private MyPlayer player = new MyPlayer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(getAdapter());
	}

	abstract ListAdapter getAdapter();

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ViewHolder holder = (ViewHolder) v.getTag();
		player.playOrPause(holder.data);
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onStop() {
		player.stop();
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		player.release();
		super.onDestroy();
	}

}
