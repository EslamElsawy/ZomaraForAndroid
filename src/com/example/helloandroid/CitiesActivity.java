package com.example.helloandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.example.helloandroid.DB.CitiesDataSource;
import com.example.helloandroid.adapters.LocationListAdapter;
import com.example.helloandroid.models.City;

public class CitiesActivity extends ListActivity implements OnQueryTextListener, OnCloseListener {

	private static final String LOGTAG = "CitiesActivity";

	CitiesDataSource citiesDataSource;

	private ListView listView;
	private SearchView searchView;
	private LocationListAdapter myListAdapter;

	private static String COUNTRY_NAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cities);

		// Open DB connection
		citiesDataSource = new CitiesDataSource(this);
		citiesDataSource.open();

		// Read Country
		Intent intent = getIntent();
		String countryName = intent.getStringExtra("data");
		COUNTRY_NAME = countryName;

		List<City> cities = citiesDataSource.finaAll(countryName);
//		if (cities.size() == 0) {
//			citiesDataSource.loadCitiesIntoSQLite();
//			cities = citiesDataSource.finaAll(countryName);
//		}

		RefreshListViewWithData(cities);

		searchView = (SearchView) findViewById(R.id.searchcities);
		searchView.setIconifiedByDefault(false);

		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		citiesDataSource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		citiesDataSource.close();
	}

	// Methods to implement searchable
	@Override
	public boolean onClose() {
		Log.i(LOGTAG, "ON Close is called");
		listView.setAdapter(myListAdapter);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.i(LOGTAG, "On Query Text Submit " + query);
		List<City> cities = citiesDataSource.searchByInputText(query, COUNTRY_NAME);
		RefreshListViewWithData(cities);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		List<City> cities = citiesDataSource.searchByInputText(newText, COUNTRY_NAME);
		RefreshListViewWithData(cities);
		return false;
	}

	private void RefreshListViewWithData(final List<City> cities) {

		List<Object> citiesObjectList = new ArrayList<Object>();
		for (City c : cities) {
			citiesObjectList.add((Object) c);
		}

		listView = (ListView) findViewById(android.R.id.list);
		myListAdapter = new LocationListAdapter(CitiesActivity.this, citiesObjectList);
		listView.setAdapter(myListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				Intent intent = new Intent(CitiesActivity.this, SettingsActivity.class);
				intent.putExtra("data", cities.get(position).getDetails());
				startActivity(intent);

			}
		});
	}
}
