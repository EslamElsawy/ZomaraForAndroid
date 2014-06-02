package com.example.helloandroid.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloandroid.R;

public class LocationListAdapter extends BaseAdapter {

	private static final String LOGTAG = "MyListAdapter";

	private List<Object> myListItems;
	private LayoutInflater myLayoutInflater;

	public LocationListAdapter(Context context, List<Object> arrayList) {
		myListItems = arrayList;
		myLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return myListItems.size();
	}

	@Override
	public Object getItem(int i) {
		return myListItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();

			view = myLayoutInflater.inflate(R.layout.locationlistitem, null);
			holder.itemName = (TextView) view.findViewById(R.id.textView1);

			view.setTag(holder);
		} else {

			holder = (ViewHolder) view.getTag();
		}

		String stringItem = myListItems.get(position).toString();
		if (stringItem != null) {
			if (holder.itemName != null) {
				// set the item name on the TextView
				holder.itemName.setText(stringItem);
			}
		}

		return view;
	}

	private static class ViewHolder {

		protected TextView itemName;
	}
}
