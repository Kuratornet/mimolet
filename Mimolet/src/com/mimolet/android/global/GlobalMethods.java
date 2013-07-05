package com.mimolet.android.global;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class GlobalMethods {
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
  
  @SuppressWarnings("static-access")
  public static boolean isOnline(Activity activity) {
    ConnectivityManager cm =
        (ConnectivityManager) activity.getSystemService(activity.getApplicationContext().CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        return true;
    }
    return false;
}
}
