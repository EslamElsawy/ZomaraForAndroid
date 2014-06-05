package com.badrit.zomara.activities;

import com.badit.zomara.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private static final String MY_APPLICATION = "MyApplication";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "WACS4e47V9IPxP76i8Nnw2FcZk8UwvtZBXJKPRIt",
				"fkGkrL8iDhL96qY4yY6HWGyYd83GrexoKWi1ycjB");
		Log.d(MY_APPLICATION,
				"Successfully parse.initialize");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		Log.d(MY_APPLICATION,
				"Successfully parsefacebook.initilize");

		// for push notification
		PushService.setDefaultPushCallback(this, SignInActivity.class);
		Log.d(MY_APPLICATION,
				"Successfully set default push bac");
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Log.d(MY_APPLICATION,
							"succesfully saved parseInstallation");
				} else {
					Log.d(MY_APPLICATION,
							"failed to save parseInstallation");
					e.printStackTrace();
				}
			}
		});
		Log.d(MY_APPLICATION,
				"Successfully instalation save");
	}
}
