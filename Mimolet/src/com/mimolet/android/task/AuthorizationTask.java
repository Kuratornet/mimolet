package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.util.Log;

public class AuthorizationTask extends AsyncTask<String, Void, String> {
	private static final String TAG = "AuthorizationTask";

	/**
	 * Accepts login, password, url
	 */
	@Override
	protected String doInBackground(final String... params) {
		try {
			final String encodedData = "j_username=" + URLEncoder.encode(params[0], "UTF-8") +
					"&j_password=" + URLEncoder.encode(params[1], "UTF-8");
			//prepare header of request
			final HttpURLConnection connection = (HttpURLConnection) new URL(
					params[2]).openConnection();
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.addRequestProperty("Content-Length", Integer.toString(encodedData.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			//write request data
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
    	    wr.writeBytes(encodedData);
    	    wr.flush();
    	    wr.close();
    	    //read response
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			final StringBuilder response = new StringBuilder();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			connection.disconnect();
			return response.toString();
		} catch (Exception ex) {
			Log.v(TAG, "Could not send login request", ex);
		}
		return null;
	}
}
