package com.forritzstar.freeringtones.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by zzz on 14-11-15.
 */
public class MyAdapter extends SimpleCursorAdapter {
    private RingFragment fragment;
    private SparseArray<CheckBox> checkBoxs = new SparseArray<CheckBox>();
    private SparseBooleanArray enables = new SparseBooleanArray();

    public MyAdapter(RingFragment fragment, int layout, Cursor c, String[] from, int[] to) {
        super(fragment.getActivity(), layout, c, from, to);
        this.fragment = fragment;

        updateEnables();
    }

    public void updateEnables() {
        Cursor cursor = fragment.getDao().getAll();
        int i = cursor.getColumnIndex("_id");
        while (cursor.moveToNext()) {
            enables.put(cursor.getInt(i), true);
        }
        cursor.close();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);
        ViewHolder holder = new ViewHolder();
        holder.checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListView().setItemChecked((Integer) v.getTag(), ((CheckBox) v).isChecked());
            }
        });
        view.setTag(holder);
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        if (fragment.getType() < RingFragment.TYPE_SYSTEM_RINGTONE || getListView().getCheckedItemCount() == 0)
            return true;
        return !enables.get(positionToId(position));
    }

    public boolean isEnabledCheckBox(int position) {
        if (fragment.getType() < RingFragment.TYPE_SYSTEM_RINGTONE)
            return true;
        return !enables.get(positionToId(position));
    }

    private int positionToId(int position) {
        getCursor().moveToPosition(position);
        return getCursor().getInt(getCursor().getColumnIndex("_id"));
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        int position = cursor.getPosition();
        ViewHolder holder = (ViewHolder) view.getTag();
        boolean checked = getListView().getCheckedItemPositions().get(position);
        holder.checkBox.setChecked(checked);
        holder.checkBox.setEnabled(isEnabledCheckBox(position));
        holder.checkBox.setTag(position);
        holder.path = cursor.getString(cursor.getColumnIndex("_data"));
        checkBoxs.put(position, holder.checkBox);
    }

    private ListView getListView() {
        return fragment.getListView();
    }

    public void setItemChecked(int position, boolean checked) {
        CheckBox box = checkBoxs.get(position);
        if (box != null)
            box.setChecked(checked);
    }

    static class ViewHolder {
        CheckBox checkBox;
        String path;
    }
}
