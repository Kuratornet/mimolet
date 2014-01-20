package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.mimolet.android.task.AuthorizationTask.ExecutionResult;

public class SocialAuthTask extends AsyncTask<String, Void, AuthorizationTask.ExecutionResult> {
	private static final String TAG = "AuthorizationTask";
	private final ProgressDialog dialog;
	private Activity parent;
	
	public SocialAuthTask(Activity parent) {
		this.parent = parent;
		dialog = new ProgressDialog(this.parent);
	}
	
	@Override
	protected ExecutionResult doInBackground(String... params) {
		Log.i(TAG, "Start social auth");
		final HttpClient httpClient = new DefaultHttpClient();
		final Properties connectionProperties = new Properties();
		try {
			connectionProperties.load(parent.getAssets().open("connection.properties"));
			final String serverUrl = connectionProperties.getProperty("server_url")
					+ connectionProperties.getProperty("whooot?!");
			final HttpPost httpPost = new HttpPost(serverUrl);
			final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("email", new StringBody(params[0]));
			reqEntity.addPart("validationid", new StringBody(params[1]));
			reqEntity.addPart("source", new StringBody(params[2]));
			Log.i(TAG, "Request rdy");
			httpPost.setEntity(reqEntity);
			final HttpResponse response = httpClient.execute(httpPost);
			Log.i(TAG, "Get responce");
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				Log.v(TAG, line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Logging in...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}
	
	protected void onPostExecute(Void result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}

}
