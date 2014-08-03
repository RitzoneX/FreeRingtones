package com.forritzstar.freeringtones;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.forritzstar.dao.Ringtone;
import com.forritzstar.dao.RingtoneDAO;

public abstract class MyAdapter extends CursorAdapter {
	private LayoutInflater inflater;
	// 保存CheckBox的选中状况
	private SparseBooleanArray isChecked = new SparseBooleanArray();
	// 保存已选择Ringtone
	private ArrayList<Ringtone> ringtones = new ArrayList<Ringtone>();
	private ActionMode mActionMode;
	private RingtoneDAO dao;
	private Activity activity;

	public MyAdapter(Context context, Cursor c, RingtoneDAO dao) {
		super(context, c);
		this.activity = (Activity) context;
		this.dao = dao;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// 找到布局和控件
		ViewHolder holder = new ViewHolder();
		View inflate = inflater.inflate(R.layout.list_item_ring, null);
		holder.text = (TextView) inflate.findViewById(android.R.id.text1);
		holder.chk = (CheckBox) inflate.findViewById(R.id.chk_item);
		inflate.setTag(holder);
		return inflate;// 返回的view传给bindView。
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		final Ringtone ringtone = getRingtone();

		// 复用布局。
		// 把数据设置到界面上
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(ringtone.getTitle());
		holder.data = ringtone.getData();
		holder.chk.setChecked(isChecked.get(ringtone.getId()));
		holder.chk.setEnabled(getEnabled(ringtone.getId()));

		// 必须注册OnClickListener
		holder.chk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionMode == null) {
					// Start the CAB using the ActionMode.Callback defined above
					mActionMode = ((Activity) context)
							.startActionMode(mActionModeCallback);
					closeAD();
				}

				setChecked(ringtone, ((CheckBox) v).isChecked());

				if (ringtones.isEmpty())
					mActionMode.finish();
			}

		});
	}

	abstract void closeAD();

	// 设置Checked方法
	private void setChecked(Ringtone ringtone, boolean checked) {
		isChecked.put(ringtone.getId(), checked);
		if (checked)
			ringtones.add(ringtone);
		else
			ringtones.remove(ringtone);
	}

	private Ringtone getRingtone() {
		String title = getCursor().getString(
				getCursor().getColumnIndex("title"));
		int id = getCursor().getInt(getCursor().getColumnIndex("_id"));
		String data = getCursor()
				.getString(getCursor().getColumnIndex("_data"));
		return new Ringtone(id, title, data);
	}

	abstract boolean getEnabled(Integer id);

	static class ViewHolder {
		TextView text;
		CheckBox chk;
		String data;
	}

	abstract int getMenuRes();

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(getMenuRes(), menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				actionDelete();
				mode.finish(); // Action picked, so close the CAB
				return true;
			case R.id.action_add:
				actionAdd();
				mode.finish(); // Action picked, so close the CAB
				return true;
			case R.id.action_selectall:
				actionSelectall();
				return true;
			case R.id.action_selectfan:
				actionSelectfan();
				return true;
			default:
				return false;
			}
		}

		// 反选
		private void actionSelectfan() {
			getCursor().moveToPosition(-1);
			while (getCursor().moveToNext()) {
				Ringtone ringtone = getRingtone();
				if (getEnabled(ringtone.getId())) {
					boolean checked = !isChecked.get(ringtone.getId());
					setChecked(ringtone, checked);
				}
			}
			if (ringtones.isEmpty())
				mActionMode.finish();
			notifyDataSetChanged();
		}

		// 全选
		private void actionSelectall() {
			getCursor().moveToPosition(-1);
			while (getCursor().moveToNext()) {
				Ringtone ringtone = getRingtone();
				if (getEnabled(ringtone.getId())
						&& !isChecked.get(ringtone.getId())) {
					setChecked(ringtone, true);
				}
			}
			notifyDataSetChanged();
		}

		// 添加
		private void actionAdd() {
			dao.add(ringtones);
			activity.setResult(Activity.RESULT_OK);
			activity.finish();
			Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			showAD();
			isChecked.clear();
			ringtones.clear();
			notifyDataSetChanged();
		}

		// 删除
		private void actionDelete() {
			dao.delete(ringtones);
			getCursor().requery();
			isChecked.clear();
			ringtones.clear();
			Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
		}
	};

	abstract void showAD();

}