package com.example.helloandroid.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class ZomaraDBOpenHelper extends SQLiteAssetHelper {

	private static final String LOGTAG = "SQLiteOpenHelper";

	private static final String DATABASE_NAME = "zomara.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_CITIES = "cities";
	public static final String COLUMN_CITY_ID = "cityID";
	public static final String COLUMN_CITY_NAME = "cityName";
	public static final String COLUMN_CITY_COUNTRY = "cityCountry";
	public static final String COLUMN_CITY_TIMEZONE = "cityTimezone";
	public static final String COLUMN_CITY_LATITUDE = "cityLatitude";
	public static final String COLUMN_CITY_LONGITUDE = "cityLongitude";

	public static final String TABLE_COUNTRIES = "countries";
	public static final String COLUMN_COUNTRY_CODE = "countryCode";
	public static final String COLUMN_COUNTRY_NAME = "countryName";

	private static final String TABLE_CITIES_CREATE = "CREATE TABLE " + TABLE_CITIES + " (" + COLUMN_CITY_ID
			+ " VARCHAR PRIMARY KEY" + " , " + COLUMN_CITY_NAME + " VARCHAR(20) " + " , " + COLUMN_CITY_COUNTRY
			+ " VARCHAR(20) " + " , " + COLUMN_CITY_TIMEZONE + " VARCHAR(20) " + " , " + COLUMN_CITY_LATITUDE
			+ " VARCHAR(20) " + " , " + COLUMN_CITY_LONGITUDE + " VARCHAR(20) " + ")";
	private static final String TABLE_COUNTRIES_CREATE = "CREATE TABLE " + TABLE_COUNTRIES + " (" + COLUMN_COUNTRY_CODE
			+ " VARCHAR PRIMARY KEY " + " , " + COLUMN_COUNTRY_NAME + " VARCHAR(20) "+ ")";

	public static ZomaraDBOpenHelper getInstance(Context context) {
		if (zomaraDBOpenHelper == null) {
			zomaraDBOpenHelper = new ZomaraDBOpenHelper(context);
		}
		return zomaraDBOpenHelper;
	}

	private static ZomaraDBOpenHelper zomaraDBOpenHelper;

	private ZomaraDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		Log.i(LOGTAG, "Helper onCreate, creating the database");
//		db.execSQL(TABLE_COUNTRIES_CREATE);
//		db.execSQL(TABLE_CITIES_CREATE);
//
//	}

//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
//		onCreate(db);
//		Log.i(LOGTAG, "Helper onUpgrade, upgrading the database");
//	}
}
