package com.mimolet.android.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.mimolet.android.AuthorizationActivity;
import com.mimolet.android.util.Registry;

public class AuthorizationTask extends
		AsyncTask<String, Void, AuthorizationTask.ExecutionResult> {
	private static final String TAG = "AuthorizationTask";

	public enum ExecutionResult {
		SUCCESS, FAIL
	}

	private AuthorizationActivity parent;

	public AuthorizationTask(AuthorizationActivity parent) {
		this.parent = parent;
	}

	/**
	 * Accepts login, password, url
	 */
	@Override
	protected ExecutionResult doInBackground(final String... params) {
		try {
			final DefaultHttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(params[2]);
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("j_username", params[0]));
			nameValuePairs.add(new BasicNameValuePair("j_password", params[1]));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient.execute(httpPost);
			final List<Cookie> cookies = httpClient.getCookieStore()
					.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					Registry.register("JSESSIONID", cookie.getValue());
				}
			}
			return ExecutionResult.SUCCESS;
		} catch (Exception ex) {
			Log.v(TAG, "Could not send login request", ex);
		}
		return ExecutionResult.FAIL;
	}

	@Override
	protected void onPostExecute(ExecutionResult result) {
		switch (result) {
		case SUCCESS:
			parent.goToAddBook(null);
			break;
		case FAIL:
			// do something horrible here
			break;
		}
	}
}
