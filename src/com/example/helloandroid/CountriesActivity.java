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

import com.example.helloandroid.DB.CountriesDataSource;
import com.example.helloandroid.adapters.LocationListAdapter;
import com.example.helloandroid.models.Country;

public class CountriesActivity extends ListActivity implements OnQueryTextListener, OnCloseListener {

	private static final String LOGTAG = "MainActivity";

	CountriesDataSource countriesDataSource;

	private ListView listView;
	private SearchView searchView;
	private LocationListAdapter myListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countries);

		// Open DB connection
		countriesDataSource = new CountriesDataSource(this);
		countriesDataSource.open();

		List<Country> countries = countriesDataSource.finaAll();
//		if (countries.size() == 0) {
//			countriesDataSource.loadCountriesIntoSQLite();
//			countries = countriesDataSource.finaAll();
//		}

		RefreshListViewWithData(countries);

		searchView = (SearchView) findViewById(R.id.searchcountries);
		searchView.setIconifiedByDefault(false);

		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		countriesDataSource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		countriesDataSource.close();
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
		List<Country> countries = countriesDataSource.searchByInputText(query);
		RefreshListViewWithData(countries);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		List<Country> countries = countriesDataSource.searchByInputText(newText);
		RefreshListViewWithData(countries);
		return false;
	}
	
	private void RefreshListViewWithData(final List<Country> countries) {

		List<Object> countriesObjectList = new ArrayList<Object>();
		for (Country c : countries) {
			countriesObjectList.add((Object) c);
		}

		listView = (ListView) findViewById(android.R.id.list);
		myListAdapter = new LocationListAdapter(CountriesActivity.this, countriesObjectList);
		listView.setAdapter(myListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				Intent intent = new Intent(CountriesActivity.this, CitiesActivity.class);
				intent.putExtra("data", countries.get(position).countryName);
				startActivity(intent);

			}
		});
	}
}
