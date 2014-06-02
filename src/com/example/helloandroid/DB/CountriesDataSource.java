package com.example.helloandroid.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.helloandroid.models.Country;

public class CountriesDataSource {

	private static final String LOGTAG = "CountriesDataSource";

	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	Context context;

	private static final String[] allColums = { ZomaraDBOpenHelper.COLUMN_COUNTRY_CODE,
			ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME };

	public CountriesDataSource(Context context) {
		this.context = context;
		dbHelper = ZomaraDBOpenHelper.getInstance(context);
	}

	public void open() {
		Log.i(LOGTAG, "DataBase opened");
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "DataBase closed");
		dbHelper.close();
	}

	public Country create(Country country) {

		ContentValues values = new ContentValues();
		values.put(ZomaraDBOpenHelper.COLUMN_COUNTRY_CODE, country.countryCode);
		values.put(ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME, country.countryName);

		db.insert(ZomaraDBOpenHelper.TABLE_COUNTRIES, null, values);
		return country;
	}

	public List<Country> finaAll() {
		List<Country> countries = new ArrayList<Country>();

		Cursor cursor = db.query(ZomaraDBOpenHelper.TABLE_COUNTRIES, allColums, null, null, null, null,
				ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME + " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String countryCode = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_COUNTRY_CODE));
				String countryName = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME));
				Country country = new Country(countryCode, countryName);
				countries.add(country);
			}
		}
		return countries;
	}

//	public void loadCountriesIntoSQLite() {
//		InputStream inputStream = context.getResources().openRawResource(R.raw.countries);
//
//		InputStreamReader inputreader = new InputStreamReader(inputStream);
//		BufferedReader buffreader = new BufferedReader(inputreader);
//		String countryString;
//
//		try {
//			while ((countryString = buffreader.readLine()) != null) {
//				if (countryString.startsWith("#") || countryString.trim().length() == 0)
//					continue;
//				Log.i(LOGTAG, countryString);
//				String[] split = countryString.split("\t+");
//				String countryCode = split[1];
//				String countryName = split[4];
//				if (isAlpha(countryName)) {
//					Country country = new Country(countryCode, countryName);
//					create(country);
//				}
//			}
//		} catch (Exception e) {
//			Log.i(LOGTAG, "Error in reading from file");
//		}
//	}

	private boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

	public List<Country> searchByInputText(String inputText) throws SQLException {

		List<Country> countries = new ArrayList<Country>();
		String query = "SELECT " + ZomaraDBOpenHelper.COLUMN_COUNTRY_CODE + " as _id" + ","
				+ ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME + " from " + ZomaraDBOpenHelper.TABLE_COUNTRIES + " where "
				+ ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME + " LIKE '" + inputText + "%' " + " ORDER BY "
				+ ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME + " ASC" + " ;";

		Log.i(LOGTAG, "Query " + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String countryCode = cursor.getString(cursor.getColumnIndex("_id"));
				String countryName = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_COUNTRY_NAME));
				Log.i(LOGTAG, "Query " + countryCode);
				Country country = new Country(countryCode, countryName);
				countries.add(country);
			} while (cursor.moveToNext());
		}

		Log.i(LOGTAG, "Row match " + cursor.getCount());
		return countries;

	}
}
