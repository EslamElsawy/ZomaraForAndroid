package com.example.helloandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;

public class MainActivity extends Activity {

	private static final String MAIN_ACTIVITY = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// auto generated code
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void onLogInButtonClicked(View v) {
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d(MAIN_ACTIVITY,
							"The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(MAIN_ACTIVITY,
							"User signed up and logged in through Facebook!"
									+ user.getUsername());
					makeMeRequest();
					startFriendsActivity();
				} else {
					Log.d(MAIN_ACTIVITY, "User logged in through Facebook!"
							+ user.getUsername());
					startFriendsActivity();
				}
			}
		});
	}

	private void makeMeRequest() {
		// create request
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							ParseUser currentUser = ParseUser.getCurrentUser();
							currentUser.put("facebookId", user.getId());
							currentUser.put("facebookName", user.getName());
							currentUser.put("facebookUsername",
									user.getUsername());
							currentUser.put("facebookLink", user.getLink());
							currentUser.saveInBackground();

							/*
							 * ParseInstllation is required for push notification
							 * (Not Completed - Not Functioning Well)
							 */
							PushService.setDefaultPushCallback(
									getBaseContext(), MainActivity.class);
							ParseInstallation parseInstallation = ParseInstallation
									.getCurrentInstallation();

							parseInstallation.add("facebookId", user.getId());
							parseInstallation.add("facebookName",
									user.getName());
							parseInstallation.add("facebookUsername",
									user.getUsername());
							// PushService.subscribe(getBaseContext(),
							// user.getId(), MainActivity.class);
							parseInstallation.addAllUnique("channels",
									Arrays.asList(user.getId()));
							parseInstallation.saveInBackground();
							ParseInstallation.getCurrentInstallation()
									.saveInBackground();
							Set<String> setOfAllSubscriptions = PushService
									.getSubscriptions(getBaseContext());
							System.err.println(setOfAllSubscriptions);
						}
					}
				});

		// excute request
		request.executeAsync();
	}

	private void startFriendsActivity() {
		Intent intent = new Intent(this, UserFriendsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// parse handle to facebook results
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

}

// push notification

// PushService.setDefaultPushCallback(this, MainActivity.class);
// ParseInstallation.getCurrentInstallation().saveInBackground(); Set<String>
// setOfAllSubscriptions = PushService .getSubscriptions(getBaseContext());
// System.err.println(setOfAllSubscriptions);
//
// // build json object JSONObject mainObj = null; try { JSONObject jo = new
// JSONObject(); jo.put("firstName", "Eslam"); jo.put("lastName", "Ashraf");
//
// JSONArray ja = new JSONArray(); ja.put(jo);
//
// mainObj = new JSONObject(); mainObj.put("employees", ja); } catch (Exception
// e) { e.printStackTrace(); }
//
// // sending push notification PushService .subscribe(getBaseContext(),
// "MyChannel", MainActivity.class); System.err.println(setOfAllSubscriptions);
// ParsePush push = new ParsePush(); push.setChannel("MyChannel");
// push.setMessage("Hello World From Channel"); push.setData(mainObj);
//
// // send push notification push.sendInBackground();

