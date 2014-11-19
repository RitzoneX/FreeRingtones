package com.forritzstar.freeringtones.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.forritzstar.freeringtones.MainActivity;
import com.forritzstar.freeringtones.R;
import com.forritzstar.freeringtones.dao.DBHelper;
import com.forritzstar.freeringtones.dao.Ringtone;
import com.forritzstar.freeringtones.dao.RingtoneDAO;
import com.forritzstar.freeringtones.util.MyPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RingFragment extends ListFragment {
    public static final int TYPE_RINGTONE = 0;
    public static final int TYPE_NOTIFICATION = 1;
    public static final int TYPE_ALARM = 2;
    public static final int TYPE_SYSTEM_RINGTONE = 3;
    public static final int TYPE_SYSTEM_NOTIFICATION = 4;
    public static final int TYPE_SYSTEM_ALARM = 5;
    public static final int TYPE_SD = 6;

    public static ActionMode mode;


    private String[] tables = new String[]{DBHelper.RINGTONES, DBHelper.NOTIFICATIONS, DBHelper.ALARMS};
    private int tableIndex;

    // 查询列表名
    private String[] projection = new String[]{MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};

    // Adapter
    private MyAdapter adapter;
    private Cursor cursor;
    private String[] from = {MediaStore.Audio.Media.TITLE};
    private int[] to = {android.R.id.text1};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_TYPE = "type";

    private int type;

    private OnFragmentInteractionListener mListener;
    private RingtoneDAO dao;

    public static RingFragment newInstance(int type) {
        RingFragment fragment = new RingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RingFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
        tableIndex = type == TYPE_SD ? MainActivity.type : type;
        dao = new RingtoneDAO(getActivity(), tables[tableIndex % 3]);

        initCursor();
        adapter = new MyAdapter(this, R.layout.list_item, cursor, from, to);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ring, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (type == MainActivity.type)
            cursor.requery();
    }

    public RingtoneDAO getDao() {
        return dao;
    }

    public int getType() {
        return type;
    }

    public Cursor getCursor() {
        if (cursor == null)
            initCursor();
        return cursor;
    }

    private void initCursor() {
        switch (type) {
            case TYPE_SYSTEM_RINGTONE:
                cursor = getActivity().getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
                        MediaStore.Audio.Media.IS_RINGTONE + "!= ?",
                        new String[]{"0"},
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                break;
            case TYPE_SYSTEM_NOTIFICATION:
                cursor = getActivity().getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
                        MediaStore.Audio.Media.IS_NOTIFICATION + "!= ?",
                        new String[]{"0"},
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                break;
            case TYPE_SYSTEM_ALARM:
                cursor = getActivity().getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
                        MediaStore.Audio.Media.IS_ALARM + "!= ?",
                        new String[]{"0"},
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                break;
            case TYPE_SD:
                cursor = getActivity().getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                        MediaStore.Audio.Media.DURATION + "!= ?",
                        new String[]{"0"},
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                break;
            default:
                cursor = dao.getAll();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new ModeCallback());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyPlayer.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) v.getTag();
        MyPlayer.playOrPause(holder.path);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(position + "");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    private class ModeCallback implements ListView.MultiChoiceModeListener {


        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            if (type >= TYPE_SYSTEM_RINGTONE)
                inflater.inflate(R.menu.list_select_menu_add, menu);
            else
                inflater.inflate(R.menu.list_select_menu_delete, menu);
            updateTitle(mode);
            RingFragment.mode = mode;
            return true;
        }

        private void updateTitle(ActionMode mode) {
            mode.setTitle(getListView().getCheckedItemCount() + " / " + getListView().getCount());
        }


        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    dao.delete(getCheckedRingtones());
                    cursor.requery();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                case R.id.add:
                    dao.add(getCheckedRingtones());
                    adapter.updateEnables();
                    Toast.makeText(getActivity(), "记得添加相应的权限，详细请看帮助", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                case R.id.select_all:
                    selectAll();
                    break;
                default:
                    Toast.makeText(getActivity(), "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }


        private void selectAll() {
            for (int i = 0; i < getListView().getCount(); ++i) {
                if (adapter.isEnabled(i))
                    getListView().setItemChecked(i, true);
            }
        }


        public void onDestroyActionMode(ActionMode mode) {
            RingFragment.mode = null;
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            adapter.setItemChecked(position, checked);

            updateTitle(mode);
        }

        // 返回选中的列表铃声
        private List<Ringtone> getCheckedRingtones() {
            ArrayList<Ringtone> ringtones = new ArrayList<Ringtone>();
            SparseBooleanArray positions = getListView().getCheckedItemPositions();
            for (int i = 0; i < getListView().getCount(); i++) {
                if (positions.get(i)) {
                    cursor.moveToPosition(i);
                    ringtones.add(new Ringtone(cursor));
                }
            }
            return ringtones;
        }
    }

}
