package com.example.helloandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class MyCustomReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// alarm
		MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm);
		mp.start();

	}

}
