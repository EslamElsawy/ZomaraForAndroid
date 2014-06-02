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

import com.example.helloandroid.models.City;

public class CitiesDataSource {

	private static final String LOGTAG = "CitiesDataSource";

	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	Context context;

	private static final String LIMIT = "100";

	private static final String[] allColums = { ZomaraDBOpenHelper.COLUMN_CITY_ID, ZomaraDBOpenHelper.COLUMN_CITY_NAME,
			ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY, ZomaraDBOpenHelper.COLUMN_CITY_TIMEZONE,
			ZomaraDBOpenHelper.COLUMN_CITY_LATITUDE, ZomaraDBOpenHelper.COLUMN_CITY_LONGITUDE };

	public CitiesDataSource(Context context) {
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

	public City create(City city) {

		ContentValues values = new ContentValues();
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_ID, city.cityId);
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_NAME, city.cityName);
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY, city.cityCountry);
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_TIMEZONE, city.cityTimeZone);
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_LATITUDE, city.cityLatitude);
		values.put(ZomaraDBOpenHelper.COLUMN_CITY_LONGITUDE, city.cityLongitude);

		db.insert(ZomaraDBOpenHelper.TABLE_CITIES, null, values);
		return city;
	}

	public List<City> finaAll(String countryName) {
		List<City> cities = new ArrayList<City>();

		Cursor cursor = db.query(ZomaraDBOpenHelper.TABLE_CITIES, allColums, ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY
				+ "=?", new String[] { countryName }, null, null, ZomaraDBOpenHelper.COLUMN_CITY_NAME + " ASC", LIMIT);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String cityId = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_ID));
				String cityName = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_NAME));
				String cityCountry = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY));
				String cityTimeZone = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_TIMEZONE));
				String cityLatitude = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_LATITUDE));
				String cityLongitude = cursor
						.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_LONGITUDE));
				City city = new City(cityId, cityName, cityCountry, cityTimeZone, cityLatitude, cityLongitude);
				cities.add(city);
			}
		}
		return cities;
	}

//	public void loadCitiesIntoSQLite() {
//		InputStream inputStream = context.getResources().openRawResource(R.raw.cities);
//
//		InputStreamReader inputreader = new InputStreamReader(inputStream);
//		BufferedReader buffreader = new BufferedReader(inputreader);
//		String cityString;
//
//		try {
//			while ((cityString = buffreader.readLine()) != null) {
//				if (cityString.startsWith("#") || cityString.trim().length() == 0)
//					continue;
//				String[] split = cityString.split("\t+");
//				City city = new City(split[0], split[1], split[2], split[3], split[4], split[5]);
//				create(city);
//			}
//		} catch (Exception e) {
//			Log.i(LOGTAG, "Error in reading from file");
//		}
//	}

	public List<City> searchByInputText(String inputText, String countryName) throws SQLException {

		List<City> cities = new ArrayList<City>();
		String query = "SELECT " + ZomaraDBOpenHelper.COLUMN_CITY_ID + " as _id" + ","
				+ ZomaraDBOpenHelper.COLUMN_CITY_NAME + "," + ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY + ","
				+ ZomaraDBOpenHelper.COLUMN_CITY_TIMEZONE + "," + ZomaraDBOpenHelper.COLUMN_CITY_LATITUDE + ","
				+ ZomaraDBOpenHelper.COLUMN_CITY_LONGITUDE + " from " + ZomaraDBOpenHelper.TABLE_CITIES + " where "
				+ ZomaraDBOpenHelper.COLUMN_CITY_NAME + " LIKE '" + inputText + "%' AND "
				+ ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY + "='" + countryName + "' " + " ORDER BY "
				+ ZomaraDBOpenHelper.COLUMN_CITY_NAME + " ASC" + " LIMIT " + LIMIT + " ;";

		Log.i(LOGTAG, "Query " + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String cityId = cursor.getString(cursor.getColumnIndex("_id"));
				String cityName = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_NAME));
				String cityCountry = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_COUNTRY));
				String cityTimeZone = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_TIMEZONE));
				String cityLatitude = cursor.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_LATITUDE));
				String cityLongitude = cursor
						.getString(cursor.getColumnIndex(ZomaraDBOpenHelper.COLUMN_CITY_LONGITUDE));

				City city = new City(cityId, cityName, cityCountry, cityTimeZone, cityLatitude, cityLongitude);
				cities.add(city);
			} while (cursor.moveToNext());
		}

		Log.i(LOGTAG, "Row match " + cursor.getCount());
		return cities;

	}
}
