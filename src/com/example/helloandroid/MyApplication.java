package com.example.helloandroid;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private static final String MY_APPLICATION = "MyApplication";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "WACS4e47V9IPxP76i8Nnw2FcZk8UwvtZBXJKPRIt",
				"fkGkrL8iDhL96qY4yY6HWGyYd83GrexoKWi1ycjB");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		Log.d(MY_APPLICATION,
				"Successfully initialied Prse and parseFacebookUtils");

		// for push notification
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
}
