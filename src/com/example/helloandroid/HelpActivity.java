package com.example.helloandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.helloandroid.adapters.HelpTabsAdapter;
import com.example.helloandroid.fragments.HelpSleepingFragment;
import com.example.helloandroid.models.BaseListElement;
import com.example.helloandroid.models.PeopleListElement;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HelpActivity extends ActionBarActivity implements ActionBar.TabListener {

	private static final String Help_Activity = "HelpActivity";
	private ViewPager viewPager;
	private ActionBar actionBar;
	private HelpTabsAdapter mAdapter;

	// Tab titles
	private String[] tabs = { "Sleeping", "Awake" };

	@Override
	protected void onResume() {
		overridePendingTransition(0, 0);
		super.onResume();
	}

	@Override
	protected void onPause() {
		overridePendingTransition(0, 0);
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// Fetch Facebook user friends if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			Log.d(Help_Activity, "Sesion is not null");
			makeFriendsRequest();
		} else {
			Log.d(Help_Activity, "Sesion is not null");
		}

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.helppager);
		actionBar = getActionBar();
		mAdapter = new HelpTabsAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// on swiping the viewpager make respective tab selected
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// disable title of the Action bar
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

	}

	public void onLogoutButtonClicked(View v) {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startMainActivity();
	}

	private void startMainActivity() {
		Intent intent = new Intent(this, SignInActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void makeFriendsRequest() {
		Log.d(Help_Activity, "make frineds request method is called");
		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {

					List<BaseListElement> listElements = new ArrayList<BaseListElement>();

					@Override
					public void onCompleted(List<GraphUser> users, Response response) {
						Log.d(Help_Activity, "make friends request callback is called");

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
								PeopleListElement element = new PeopleListElement(getApplicationContext(), currId,
										currName, currId + "", i);
								listElements.add(element);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}

						HelpSleepingFragment helpSleepingFragment = (HelpSleepingFragment) mAdapter
								.getRegisteredFragment(viewPager.getCurrentItem());
						helpSleepingFragment.drawFriendsOnGridView(listElements);
					}
				});

		// excute request
		request.executeAsync();
	}

	// Buttom Action Bar Methods
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.Help) {
			Toast toast = Toast.makeText(getApplicationContext(), "This is the current Tab", Toast.LENGTH_LONG);
			toast.show();
			return true;

		} else if (id == R.id.Statistics) {
			Intent intent = new Intent(this, StatisticsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Tabs Methods
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(arg0.getPosition());

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}
