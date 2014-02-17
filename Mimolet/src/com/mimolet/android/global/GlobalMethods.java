package com.mimolet.android.global;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.mimolet.android.OrdersListActivity;
import com.mimolet.android.R;

import entity.Order;

public class GlobalMethods {
	final private static long DELAY = 60 * 1000 * 10;
	private static final String TAG = "GlobalMethods";

	public static void startTimer(final String url) {
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
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(activity.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static void goToOrderList(Activity activity, View view,
			List<Order> orders) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				OrdersListActivity.class);
		Log.d(TAG, "Orders = " + orders);
		if (orders != null) {
			final int[] ordersId = new int[orders.size()];
			final String[] ordersName = new String[orders.size()];
			final String[] imageSourcesLinks = new String[orders.size()];
			final String[] ordersDate = new String[orders.size()];
			final String[] bindingsDate = new String[orders.size()];
			final String[] paperData = new String[orders.size()];
			final String[] printData = new String[orders.size()];
			final String[] coverData = new String[orders.size()];
			final String[] blockSizeData = new String[orders.size()];
			final String[] pagesData = new String[orders.size()];
			for (int i = 0; i < orders.size(); i++) {
				ordersId[i] = orders.get(i).getId();
				ordersName[i] = orders.get(i).getDescription();
				imageSourcesLinks[i] = orders.get(i).getImagelink();
				ordersDate[i] = orders.get(i).getCreateData().toString();
				bindingsDate[i] = orders.get(i).getBinding().toString();
				paperData[i] = orders.get(i).getPaper().toString();
				printData[i] = orders.get(i).getPrint().toString();
				coverData[i] = orders.get(i).getBinding().toString();
				blockSizeData[i] = orders.get(i).getBlocksize().toString();
				pagesData[i] = orders.get(i).getPages().toString();
			}
			
			intent.putExtra("ids", ordersId);
			intent.putExtra("orders", ordersName);
			intent.putExtra("imageSources", imageSourcesLinks);
			intent.putExtra("createData", ordersDate);
			intent.putExtra("bindingsDate", bindingsDate);
			intent.putExtra("paperData", paperData);
			intent.putExtra("printData", printData);
			intent.putExtra("coverData", coverData);
			intent.putExtra("blockSizeData", blockSizeData);
			intent.putExtra("pagesData", pagesData);
			intent.putExtra("ordersListSize", orders.size());
		} else {
			intent.putExtra("ordersListSize", 0);
		}
		activity.startActivity(intent);
		activity.finish();
	}

	public static void checkConnectionToServer(final Activity activity) {
		Log.i(TAG, "Test internet connection");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					Properties connectionProperties = new Properties();
					connectionProperties.load(activity.getAssets().open(
							"connection.properties"));
					String serverUrl = connectionProperties
							.getProperty("server_url") + "ping";
					HttpGet httpPost = new HttpGet(serverUrl);
					httpClient.execute(httpPost);

				} catch (Exception e) {
					e.printStackTrace();
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new AlertDialog.Builder(activity)
					           .setMessage(R.string.server_unavailable)
					           .setCancelable(false)
					           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int id) {
					            	   activity.finish();
					               }
					           }).show();
						}
					});
				}
			}
		});
		thread.start();
	}
}
