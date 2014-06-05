package com.badrit.zomara.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.badrit.zomara.models.BaseListElement;
import com.badit.zomara.R;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseUser;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<BaseListElement> listElements;

	public ImageAdapter(Context c, List<BaseListElement> elements) {
		mContext = c;
		listElements = elements;
	}

	public int getCount() {
		return listElements.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		// prepare view
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem, parent, false);
		}

		// fill view with data
		BaseListElement listElement = listElements.get(position);
		if (listElement != null) {

			// Alarm button
			ImageView alarmButton = (ImageView) view.findViewById(R.id.alarmbutton);
			alarmButton.setOnClickListener(listElement.getOnAlarmClickListener());

			alarmButton.setTag(R.string.fromId, ParseUser.getCurrentUser().get("facebookId"));
			alarmButton.setTag(R.string.toId, listElement.getID());
			alarmButton.setTag(R.string.fromUser, ParseUser.getCurrentUser().get("facebookName"));
			alarmButton.setTag(R.string.toUser, listElement.getText1());

			// Mic button
			ImageView micButton = (ImageView) view.findViewById(R.id.mic);
			micButton.setOnClickListener(listElement.getOnMicClickListener());

			micButton.setTag(R.string.fromId, ParseUser.getCurrentUser().get("facebookId"));
			micButton.setTag(R.string.toId, listElement.getID());
			micButton.setTag(R.string.fromUser, ParseUser.getCurrentUser().get("facebookName"));
			micButton.setTag(R.string.toUser, listElement.getText1());

			// profile picture and text
			ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilepicture);
			TextView text1 = (TextView) view.findViewById(R.id.text1);
			// TextView text2 = (TextView) view.findViewById(R.id.text2);

			if (text1 != null) {
				text1.setText(listElement.getText1());
			}
			// if (text2 != null) {
			// text2.setText(listElement.getText2());
			// }
			if (profilePictureView != null) {
				profilePictureView.setProfileId(listElement.getID());
			}
		}
		return view;
	}
}
