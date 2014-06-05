package com.badrit.zomara.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.badit.zomara.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class PeopleListElement extends BaseListElement {
	private Context mContext;
	private static final String PeopleListElement = "PeopleListElement";

	public PeopleListElement(Context context, String id, String name, String description, int requestCode) {
		super(id, name, description, requestCode);
		mContext = context;
	}

	@Override
	public View.OnClickListener getOnAlarmClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(PeopleListElement, "Alarm !!!");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.fromId)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.toId)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.fromUser)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.toUser)) + "");

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
				Toast toast = Toast.makeText(mContext, "Sending Alarm to " + (String) view.getTag(R.string.toUser),
						Toast.LENGTH_LONG);
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
	public OnClickListener getOnMicClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(PeopleListElement, "Mic !!!");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.fromId)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.toId)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.fromUser)) + "");
				Log.d(PeopleListElement, ((String) view.getTag(R.string.toUser)) + "");

				// start recording
				onRecord(mStartRecording, view);
				if (mStartRecording) {
					Log.d(LOG_TAG, "Start recording");
					ImageView micButton = (ImageView) view.findViewById(R.id.mic);
					micButton.setImageResource(R.drawable.stop);
				} else {
					Log.d(LOG_TAG, "Stop recording");
					ImageView micButton = (ImageView) view.findViewById(R.id.mic);
					micButton.setImageResource(R.drawable.voice_icon);
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

		// Don't Send Large Voice files > 50KB
		if ((byteArray.length / 1000) > 50) {
			Toast toast = Toast.makeText(mContext,
					"Sorry Can't send voice files with size larger than 10KB \n Your file is "
							+ (byteArray.length / 1000) + " KB", Toast.LENGTH_LONG);
			toast.show();
			return;
		}

		// Testing message
		Toast toast = Toast.makeText(mContext, "Sending Voice of Size " + (byteArray.length / 1000) + " KB to "
				+ (String) view.getTag(R.string.toUser), Toast.LENGTH_LONG);
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