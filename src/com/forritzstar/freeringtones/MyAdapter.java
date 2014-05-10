package com.forritzstar.freeringtones;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.forritzstar.dao.Ringtone;

/**
 * 自定义Adapter
 * 
 * @author Administrator
 * 
 */
public abstract class MyAdapter extends CursorAdapter {
	private LayoutInflater inflater;
	private ListStatus listStatus;

	ListStatus getListStatus() {
		return listStatus;
	}

	public MyAdapter(Context context, Cursor c, ListStatus listStatus) {
		super(context, c);
		inflater = LayoutInflater.from(context);
		this.listStatus = listStatus;
	}

	public MyAdapter(ListFragment fragment, Cursor c, ListStatus listStatus) {
		this(fragment.getActivity(), c, listStatus);
	}

	public void selectFan() {
		listStatus.selectFan();
		notifyDataSetChanged();
	}

	public void selectAll() {
		listStatus.selectAll();
		notifyDataSetChanged();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// 找到布局和控件
		ViewHolder holder = new ViewHolder();
		View view = inflater.inflate(R.layout.list_item, null);
		holder.text = (TextView) view.findViewById(android.R.id.text1);
		holder.chk = (CheckBox) view.findViewById(R.id.chk_item);
		view.setTag(holder);
		return view;// 返回的view传给bindView。
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// 复用布局。
		// 把数据设置到界面上
		ViewHolder holder = (ViewHolder) view.getTag();
		String text = cursor.getString(cursor.getColumnIndex("title"));
		holder.text.setText(text);

		listStatus.refreshRingtone();
		setEnabled(holder.chk);
		holder.chk.setChecked(listStatus.isChecked());

		// 必须注册OnClickListener
		holder.chk.setOnClickListener(new OnClickListener() {
			private Ringtone ringtone = listStatus.getRingtone();

			@Override
			public void onClick(View v) {
				listStatus.setChecked(ringtone, ((CheckBox) v).isChecked());
			}
		});

	}

	abstract void setEnabled(CheckBox chk);

	/**
	 * 返回选中的铃声列表
	 * 
	 * @return
	 */
	public ArrayList<Ringtone> getRingtones() {
		return listStatus.getRingtones();
	}

	/**
	 * 保存列表状态
	 * 
	 * @author Administrator
	 * 
	 */
	abstract static class ListStatus {
		// 保存CheckBox的选中状况
		private SparseBooleanArray isChecked = new SparseBooleanArray();
		// 保存已选择Ringtone
		private ArrayList<Ringtone> ringtones = new ArrayList<Ringtone>();

		protected Ringtone ringtone;
		private Cursor cursor;

		public ListStatus(Cursor cursor) {
			this.cursor = cursor;
		}

		public boolean isChecked() {
			return isChecked.get(ringtone.getId());
		}

		protected void setChecked(boolean checked) {
			setChecked(ringtone, checked);
		}

		public void setChecked(Ringtone ringtone, boolean checked) {
			isChecked.put(ringtone.getId(), checked);

			if (checked)
				ringtones.add(ringtone);
			else
				ringtones.remove(ringtone);
		}

		public Cursor getCursor() {
			return cursor;
		}

		/**
		 * 全选
		 */
		abstract void selectAll();

		/**
		 * 反选
		 */
		abstract void selectFan();

		public Ringtone getRingtone() {
			return ringtone;
		}

		/**
		 * 刷新Ringtone
		 */
		public void refreshRingtone() {
			ringtone = new Ringtone(getId(), getTitle(), getUri());
		}

		int getId() {
			return getCursor().getInt(0);
		}

		private String getTitle() {
			return getCursor().getString(1);
		}

		abstract String getUri();

		public ArrayList<Ringtone> getRingtones() {
			return ringtones;
		}

	}

	static class ViewHolder {
		TextView text;
		CheckBox chk;
	}

}
