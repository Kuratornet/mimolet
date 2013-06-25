package com.mimolet.android.global;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

class GlobalMethod {
	final private static long DELAY = 60 * 1000 * 10;
	
	public static void startTimer (final String url) {
		final ScheduledExecutorService scheduler = Executors
				.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					final HttpURLConnection connection = (HttpURLConnection) new URL(
							url + "ping").openConnection();
					connection.disconnect();
				} catch (Exception e) {
					Log.e("ERROR", e.toString());
				}
			}

		}, DELAY, DELAY, TimeUnit.MILLISECONDS);
	}
}