package com.badrit.zomara.models;

import android.view.View;
import android.widget.BaseAdapter;

public abstract class BaseListElement {

	private String id;
	private String text1;
	private String text2;
	private int requestCode;
	private BaseAdapter adapter;

	public abstract View.OnClickListener getOnAlarmClickListener();
	public abstract View.OnClickListener getOnMicClickListener();

	public BaseListElement(String id, String text1, String text2,
			int requestCode) {
		super();
		this.id = id;
		this.text1 = text1;
		this.text2 = text2;
		this.requestCode = requestCode;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		this.text2 = text2;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

}
