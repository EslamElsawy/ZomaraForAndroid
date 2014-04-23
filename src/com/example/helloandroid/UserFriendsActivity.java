package com.example.helloandroid;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class UserFriendsActivity extends Activity {

	private static final String FRIENDS_ACTIVITY = "FriendsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_activity);

		// Fetch Facebook user friends if the session is active
		List<BaseListElement> listElements = null;
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			Log.d(FRIENDS_ACTIVITY, "Sesion is not null");
			makeFriendsRequest();
		} else {
			Log.d(FRIENDS_ACTIVITY, "Sesion is not null");
		}
	}

	public void onLogoutButtonClicked(View v) {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startMainActivity();
	}

	private void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void makeFriendsRequest() {
		Log.d(FRIENDS_ACTIVITY, "make frineds request method is called");
		Request request = Request.newMyFriendsRequest(
				ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {

					TextView label = (TextView) findViewById(R.id.label);
					ListView listView = (ListView) findViewById(R.id.mylistview);
					List<BaseListElement> listElements = new ArrayList<BaseListElement>();

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
						Log.d(FRIENDS_ACTIVITY,
								"make friends request callback is called");

						for (int i = 0; i < 20; i++) {
							GraphUser currUser = users.get(i);
							listElements.add(new PeopleListElement(currUser
									.getId(), currUser.getName(), currUser
									.getId() + "", i));
						}
						listView.setAdapter(new ActionListAdapter(
								UserFriendsActivity.this, R.id.mylistview,
								listElements));
					}
				});

		// excute request
		request.executeAsync();
	}

	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
		private List<BaseListElement> listElements;

		public ActionListAdapter(Context context, int resourceId,
				List<BaseListElement> listElements) {
			super(context, resourceId, listElements);
			this.listElements = listElements;
			// Set up as an observer for list item changes to
			// refresh the view.
			for (int i = 0; i < listElements.size(); i++) {
				listElements.get(i).setAdapter(this);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) UserFriendsActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.listitem, null);
			}

			BaseListElement listElement = listElements.get(position);
			if (listElement != null) {
				// view.setOnClickListener(listElement.getOnClickListener());
				// alarm button
				Button alarmButton = (Button) view.findViewById(R.id.alarmbutton);
				alarmButton
						.setOnClickListener(listElement.getOnClickListener());
				alarmButton.setTag(R.string.fromId,ParseUser.getCurrentUser().get("facebookId"));
				alarmButton.setTag(R.string.toId,listElement.getID());
				alarmButton.setTag(R.string.fromUser,ParseUser.getCurrentUser().get("facebookName"));
				alarmButton.setTag(R.string.toUser,listElement.getText1());

				// provile picture and text
				ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.icon);
				TextView text1 = (TextView) view.findViewById(R.id.text1);
				TextView text2 = (TextView) view.findViewById(R.id.text2);
				if (profilePictureView != null) {
					profilePictureView.setProfileId(listElement.getID());
				}
				if (text1 != null) {
					text1.setText(listElement.getText1());
				}
				if (text2 != null) {
					text2.setText(listElement.getText2());
				}
			}
			return view;
		}

	}

	private class PeopleListElement extends BaseListElement {

		public PeopleListElement(String id, String name, String description,
				int requestCode) {
			super(id, name, description, requestCode);
		}

		@Override
		protected View.OnClickListener getOnClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(FRIENDS_ACTIVITY, "Click !!!");
					Log.d(FRIENDS_ACTIVITY, (String) view.getTag(R.string.fromId));
					Log.d(FRIENDS_ACTIVITY, (String) view.getTag(R.string.toId));
					Log.d(FRIENDS_ACTIVITY, (String) view.getTag(R.string.fromUser));
					Log.d(FRIENDS_ACTIVITY, (String) view.getTag(R.string.toUser));
					
					ParseObject notification = new ParseObject("Notification");
					notification.put("fromId", (String) view.getTag(R.string.fromId));
					notification.put("toId", (String) view.getTag(R.string.toId));
					notification.put("fromUser",(String) view.getTag(R.string.fromUser));
					notification.put("toUser", (String) view.getTag(R.string.toUser));
					notification.saveInBackground();
					
				}
			};
		}
	}


}
