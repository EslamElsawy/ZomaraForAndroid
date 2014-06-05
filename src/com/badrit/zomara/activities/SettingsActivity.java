package com.badrit.zomara.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.badrit.zomara.models.City;
import com.badrit.zomara.prayertimes.PrayTime;
import com.badit.zomara.R;

public class SettingsActivity extends ActionBarActivity {

	private ActionBar actionBar;

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
		setContentView(R.layout.activity_settings);

		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// disable title of the Action bar
		actionBar.setDisplayShowHomeEnabled(false);

		// Genearting Setting values
		Intent intent = getIntent();
		String cityString = intent.getStringExtra("data");
		String[] split = cityString.split("\t+");
		City city = new City(split[0], split[1], split[2], split[3], split[4], split[5]);

		// getting the prayer time
		PrayTime prayTime = new PrayTime();
		prayTime.setTimeFormat(1);
		prayTime.setCalcMethod(5);
		Float timeZone = Float.parseFloat(city.cityTimeZone);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		month += 1;
		int year = calendar.get(Calendar.YEAR);
		String currDate = day + " " + month + " " + year;

		ArrayList<String> times = prayTime.getDatePrayerTimes(year, month, day, Double.parseDouble(city.cityLatitude),
				Double.parseDouble(city.cityLongitude), timeZone.intValue());

		String fajrTime = times.get(0);
		String sheroqTime = times.get(1);

		// showing results
		TextView textView = (TextView) findViewById(R.id.textviewsettings);
		textView.setText("City: " + city.cityName + "\n" + "Country: " + city.cityCountry + "\n" + "TimeZone: "
				+ timeZone.intValue() + "\n" + "Latitude: " + city.cityLatitude + "\n" + "Longituded: "
				+ city.cityLongitude + "\n" + 
				"SystemClock: " + new Date(System.currentTimeMillis()) + "\n" +
				"Date: " + currDate + "\n" +"Fajr: " + fajrTime + "\n" + "Sherouq: " + sheroqTime);
		
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
			Intent intent = new Intent(this, HelpActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		} else if (id == R.id.Statistics) {
			Intent intent = new Intent(this, StatisticsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		} else if (id == R.id.Settings) {
			Toast toast = Toast.makeText(getApplicationContext(), "This is the current Tab", Toast.LENGTH_LONG);
			toast.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
