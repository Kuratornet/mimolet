package com.mimolet.android.global;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mimolet.android.OrdersListActivity;

import entity.Order;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

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
  
  public static void goToOrderList(Activity activity, View view, List<Order> orders) {
    final String[] ordersName = new String[orders.size()];
    final String[] imageSourcesLinks = new String[orders.size()];
    final String[] ordersDate = new String[orders.size()];
    for (int i = 0; i < orders.size(); i++) {
      ordersName[i] = orders.get(i).getDescription();
      imageSourcesLinks[i] = orders.get(i).getImagelink();
      ordersDate[i] = orders.get(i).getCreateData().toString();
    }
    final Intent intent = new Intent(activity.getApplicationContext(),
        OrdersListActivity.class);
    intent.putExtra("orders", ordersName);
    intent.putExtra("imageSources", imageSourcesLinks);
    intent.putExtra("createData", ordersDate);
    activity.startActivity(intent);
  }
}
