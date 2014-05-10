package com.forritzstar.freeringtones;

import android.view.View;
import android.view.View.OnClickListener;

public class MyListener implements OnClickListener {
	private MyAdapter adapter;

	public MyListener(MyAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select_all: // 全选
			adapter.selectAll();
			break;
		case R.id.btn_select_fan: // 反选
			adapter.selectFan();
			break;
		}
	}

}
