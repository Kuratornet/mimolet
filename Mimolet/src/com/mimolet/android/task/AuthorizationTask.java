package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class AuthorizationTask extends AsyncTask<String, Void, String> {
	private static final String TAG = "AuthorizationTask";

	/**
	 * Accepts login, password, url
	 */
	@Override
	protected String doInBackground(final String... params) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(params[0], params[1]
						.toCharArray());
			}
		});
		try {
			final HttpURLConnection connection = (HttpURLConnection) new URL(
					params[2]).openConnection();
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();
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
