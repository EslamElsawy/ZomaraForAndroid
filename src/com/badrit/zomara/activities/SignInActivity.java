package com.badrit.zomara.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.badit.zomara.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class SignInActivity extends Activity {

	private static final String MAIN_ACTIVITY = "SignInActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		// auto generated code
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	public void onLogInButtonClicked(View v) {

		List<String> permissions = Arrays.asList("basic_info", "user_about_me", "user_relationships", "user_birthday",
				"user_location");

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d(MAIN_ACTIVITY, err.getMessage());
					Log.d(MAIN_ACTIVITY, "The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(MAIN_ACTIVITY, "User signed up and logged in through Facebook!" + user.getUsername());
					makeMeRequest();
					startFriendsActivity();
				} else {
					Log.d(MAIN_ACTIVITY, "User logged in through Facebook!" + user.getUsername());
					makeMeRequest();
					startFriendsActivity();
				}
			}
		});
	}

	private void makeMeRequest() {
		// create request
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					Log.d(MAIN_ACTIVITY, "Start adding fields to ParseUser");
					ParseUser currentUser = ParseUser.getCurrentUser();
					currentUser.put("facebookId", user.getId());
					currentUser.put("facebookName", user.getName());
					currentUser.put("facebookUsername", user.getUsername()+"");
					currentUser.put("facebookLink", user.getLink());
					currentUser.saveInBackground();
					Log.d(MAIN_ACTIVITY, "end adding fields to ParseUser");

					// Subscribe for push notification
					subscribe(user);

					// Notify my friends

				}
			}

			private void subscribe(GraphUser user) {

				Log.d(MAIN_ACTIVITY, "Start adding fields to parseInstallation");
				ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();

				parseInstallation.put("facebookId", user.getId());
				parseInstallation.put("facebookName", user.getName());
				parseInstallation.put("facebookUsername", user.getUsername()+"");
				PushService.subscribe(getApplicationContext(), "user" + user.getId(), HelpActivity.class);

				Log.d(MAIN_ACTIVITY, (String) parseInstallation.get("facebookId"));

				parseInstallation.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Log.d(MAIN_ACTIVITY, "succesfully saved parseInstallation");
						} else {
							Log.d(MAIN_ACTIVITY, "failed to save parseInstallation");
							e.printStackTrace();
						}
					}
				});
				Log.d(MAIN_ACTIVITY, "end adding fields to ParseUser");

			}
		});

		// excute request
		request.executeAsync();
	}

	private void startFriendsActivity() {
		Intent intent = new Intent(this, CountriesActivity.class);
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

