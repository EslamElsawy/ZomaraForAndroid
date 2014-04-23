package com.example.helloandroid;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private static final String MY_APPLICATION = "MyApplication";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "gsh7ocCqdp76hlXMBGJrUBaqKfugPnL8lfQmzR52",
				"oBPeLekOEjVOfzPrpaJCCjlpkWX924T4Cxiy7Hrl");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		Log.d(MY_APPLICATION,
				"Successfully initialied Prse and parseFacebookUtils");
	}
}
