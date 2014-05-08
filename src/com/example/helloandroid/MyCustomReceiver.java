package com.example.helloandroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MyCustomReceiver extends BroadcastReceiver {

	public static String LOG_TAG = "MyCustomReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {

		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

			Iterator itr = json.keys();
			String type = "";
			String fromId = "";
			String fromUser = "";
			while (itr.hasNext()) {
				String key = (String) itr.next();
				String value = json.getString(key);
				if (key.equals("type")) {
					type = value;
				} else if (key.equals("fromId")) {
					fromId = value;
				} else if (key.equals("fromUser")) {
					fromUser = value;
				}
			}

			Toast toast = Toast.makeText(context, "Recieving " + type + " from " + fromUser, Toast.LENGTH_LONG);
			toast.show();
			Log.d(LOG_TAG, type);
			Log.d(LOG_TAG, fromId);
			Log.d(LOG_TAG, fromUser);

			if (type.equals("alarm")) {
				Log.d(LOG_TAG, "Running Alarm");
				MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm);
				mp.start();
			} else if (type.equals("voice")) {
				Log.d(LOG_TAG, "Running Voice");

				// Get audio file from Parse
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
				query.whereEqualTo("type", type);
				query.whereEqualTo("fromId", fromId);
				query.orderByDescending("createdAt");
				query.setLimit(1);

				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> notifications, ParseException e) {
						if (e == null) {
							Log.d(LOG_TAG, "Retrieved " + notifications.size() + " Notifications");
							ParseObject recentNotifiction = notifications.get(0);

							ParseFile soundFile = (ParseFile) recentNotifiction.get("file");
							soundFile.getDataInBackground(new GetDataCallback() {
								public void done(byte[] data, ParseException e) {
									if (e == null) {
										Log.d(LOG_TAG, "Successfully dowloaded voice from Parse");
										Log.d(LOG_TAG, "Voice file is " + data.length + " bytes");
										playSound(data);
									} else {
										Log.d(LOG_TAG, "Failed to download voice from Prse");
									}
								}
							});
						} else {
							Log.d(LOG_TAG, "Error: " + e.getMessage());
						}
					}
				});
			}

		} catch (JSONException e) {
			Log.d(LOG_TAG, "JSONException: " + e.getMessage());
		}

	}

	private void playSound(byte[] byteArray) {
		try {

			File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/musicfile.3gp");
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(byteArray);
			fos.close();

			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/musicfile.3gp");
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (IOException ex) {
			Log.d(LOG_TAG, "Error In Playing Sound");
			ex.printStackTrace();
		}
	}

}
