package com.forritzstar.freeringtones;

import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.forritzstar.freeringtones.b.BannerView;
import com.forritzstar.freeringtones.p.MyManager;

public class KuGuo {
	private static final String COO_ID = "52e3580ea21b471586b167206a411342";
	private static final String CHANNEL_ID = "wandoujia";

	private BannerView bannerView;
	private Context context;
	private MyManager pm;
	private ListFragment fragment;

	public KuGuo(Context context) {
		this.context = context;
	}

	public KuGuo(ListFragment fragment) {
		this(fragment.getActivity());
		this.fragment = fragment;
	}

	void showFooterBanner() {
		getListView().addFooterView(getBannerView());
		bannerView.showBanner(COO_ID, CHANNEL_ID);
	}

	void showHeaderBanner() {
		getListView().addHeaderView(getBannerView());
		bannerView.showBanner(COO_ID, CHANNEL_ID);
	}

	void showBanner() {
		bannerView = new BannerView(context);
		View view = null;
		if (fragment == null)
			view = ((Activity) context).findViewById(R.id.layout_ad);
		else
			view = fragment.getView().findViewById(R.id.layout_ad);
		((LinearLayout) view).addView(bannerView);
		bannerView.showBanner(COO_ID, CHANNEL_ID);
	}

	private View getBannerView() {
		bannerView = new BannerView(context);
		LinearLayout view = new LinearLayout(context);
		view.setGravity(Gravity.CENTER_HORIZONTAL);
		view.addView(bannerView);
		return view;
	}

	private ListView getListView() {
		if (fragment == null)
			return ((ListActivity) context).getListView();
		else
			return fragment.getListView();
	}

	void push() {
		if (new Random().nextBoolean()) {
			pm = MyManager.getInstance(context);
			// 设置cooId
			pm.setCooId(context, COO_ID);//

			// 设置channelId
			pm.setChannelId(context, CHANNEL_ID);
			// 接收push
			pm.receiveMessage(context, true);
		}
	}

	void stopMessage() {
		if (pm != null)
			pm.stopMessage(context);
	}

	void finishBanner() {
		if (bannerView != null)
			bannerView.finishBanner();
	}
}
