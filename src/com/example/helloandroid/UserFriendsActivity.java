package com.example.helloandroid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserFriendsActivity extends Activity {

	private static final String FRIENDS_ACTIVITY = "FriendsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_activity);

		// Fetch Facebook user friends if the session is active
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
		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {

					ListView listView = (ListView) findViewById(R.id.mylistview);
					List<BaseListElement> listElements = new ArrayList<BaseListElement>();

					@Override
					public void onCompleted(List<GraphUser> users, Response response) {
						Log.d(FRIENDS_ACTIVITY, "make friends request callback is called");

						ArrayList<String> ids = new ArrayList<String>();
						for (int i = 0; i < users.size(); i++) {
							GraphUser currUser = users.get(i);
							ids.add(currUser.getId());
						}

						// find if the user previously used the application
						ParseQuery<ParseUser> query = ParseUser.getQuery();
						query.whereContainedIn("facebookId", ids);
						try {
							List<ParseUser> matchedUsers = query.find();
							for (int i = 0; i < matchedUsers.size(); i++) {
								ParseUser curr = matchedUsers.get(i);
								String currId = curr.getString("facebookId");
								String currName = curr.getString("facebookName");
								PeopleListElement element = new PeopleListElement(currId, currName, currId + "", i);
								listElements.add(element);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}

						listView.setAdapter(new ActionListAdapter(UserFriendsActivity.this, R.id.mylistview,
								listElements));
					}
				});

		// excute request
		request.executeAsync();
	}

	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
		private List<BaseListElement> listElements;

		public ActionListAdapter(Context context, int resourceId, List<BaseListElement> listElements) {
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

			// prepare view
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
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

	private class PeopleListElement extends BaseListElement {

		public PeopleListElement(String id, String name, String description, int requestCode) {
			super(id, name, description, requestCode);
		}

		@Override
		protected View.OnClickListener getOnAlarmClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(FRIENDS_ACTIVITY, "Alarm !!!");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.fromId)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.toId)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.fromUser)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.toUser)) + "");

					// Save notifiction to Parse
					ParseObject notification = new ParseObject("Notification");
					notification.put("fromId", ((String) view.getTag(R.string.fromId)) + "");
					notification.put("toId", (String) view.getTag(R.string.toId));
					notification.put("fromUser", ((String) view.getTag(R.string.fromUser)) + "");
					notification.put("toUser", (String) view.getTag(R.string.toUser));
					notification.put("type", "alarm");
					notification.saveInBackground();

					// Send push notification
					ParsePush push = new ParsePush();
					JSONObject data = null;
					try {
						data = new JSONObject("{  \"action\": \"com.example.helloandroid.UPDATE_STATUS\", "
								+ "\"type\": \"alarm\"," + "\"fromId\": \"" + ((String) view.getTag(R.string.fromId))
								+ "\"," + "\"fromUser\": \"" + ((String) view.getTag(R.string.fromUser)) + "\" }");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					push.setChannel("user" + (String) view.getTag(R.string.toId));
					push.setData(data);
					push.sendInBackground();

					// testing message
					Toast toast = Toast.makeText(getApplicationContext(),
							"Sending Alarm to " + (String) view.getTag(R.string.toUser), Toast.LENGTH_LONG);
					toast.show();
				}
			};
		}

		// Voice recording feature
		boolean mStartRecording = true;
		private static final String LOG_TAG = "AudioRecordTest";
		private MediaRecorder mRecorder = null;
		private String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.3gp";

		@Override
		protected OnClickListener getOnMicClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(FRIENDS_ACTIVITY, "Mic !!!");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.fromId)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.toId)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.fromUser)) + "");
					Log.d(FRIENDS_ACTIVITY, ((String) view.getTag(R.string.toUser)) + "");

					// start recording
					onRecord(mStartRecording, view);
					if (mStartRecording) {
						Log.d(LOG_TAG, "Start recording");
						ImageView micButton = (ImageView) view.findViewById(R.id.mic);
						micButton.setImageResource(R.drawable.stop);
					} else {
						Log.d(LOG_TAG, "Stop recording");
						ImageView micButton = (ImageView) view.findViewById(R.id.mic);
						micButton.setImageResource(R.drawable.mic);
					}
					mStartRecording = !mStartRecording;

				}
			};
		}

		private void onRecord(boolean start, View view) {
			if (start) {
				startRecording();
			} else {
				stopRecording(view);
			}
		}

		private void startRecording() {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setOutputFile(fileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
			} catch (IOException e) {
				Log.e(LOG_TAG, "prepare() failed");
			}
			mRecorder.start();
		}

		private void stopRecording(View view) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;

			sendVoice(view);
		}

		private void sendVoice(final View view) {
			Log.d(LOG_TAG, "Start sending");

			// Reading saved audio from SD card
			File fileInput = new File(fileName);
			int size = (int) fileInput.length();
			byte[] byteArray = new byte[size];
			try {
				BufferedInputStream buf = new BufferedInputStream(new FileInputStream(fileInput));
				buf.read(byteArray, 0, byteArray.length);
				buf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Don't Send Large Voice files > 25KB
			if ((byteArray.length / 1000) > 25) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Sorry Can't send voice files with size larger than 10KB \n Your file is "
								+ (byteArray.length / 1000) + " KB", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			// Testing message
			Toast toast = Toast.makeText(getApplicationContext(), "Sending Voice of Size " + (byteArray.length / 1000)
					+ " KB to " + (String) view.getTag(R.string.toUser), Toast.LENGTH_LONG);
			toast.show();

			// Save Notification to parse with the voice
			ParseObject notification = new ParseObject("Notification");
			notification.put("fromId", ((String) view.getTag(R.string.fromId)) + "");
			notification.put("toId", (String) view.getTag(R.string.toId));
			notification.put("fromUser", ((String) view.getTag(R.string.fromUser)) + "");
			notification.put("toUser", (String) view.getTag(R.string.toUser));
			notification.put("type", "voice");
			ParseFile file = new ParseFile("voice.3gp", byteArray);
			notification.put("file", file);
			notification.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException arg0) {

					// Send push notification to the receiver to start download
					// the voice
					ParsePush push = new ParsePush();
					JSONObject data = null;
					try {
						data = new JSONObject("{  \"action\": \"com.example.helloandroid.UPDATE_STATUS\", "
								+ "\"type\": \"voice\"," + "\"fromId\": \"" + ((String) view.getTag(R.string.fromId))
								+ "\"," + "\"fromUser\": \"" + ((String) view.getTag(R.string.fromUser)) + "\"  }");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					push.setChannel("user" + (String) view.getTag(R.string.toId));
					push.setData(data);
					push.sendInBackground();
				}
			});
		}
	}

}
