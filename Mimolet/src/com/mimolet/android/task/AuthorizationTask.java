package com.mimolet.android.task;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mimolet.android.AuthorizationActivity;
import com.mimolet.android.R;
import com.mimolet.android.util.DataBaseLoginUtil;
import com.mimolet.android.util.Registry;

public class AuthorizationTask extends
		AsyncTask<String, Void, AuthorizationTask.ExecutionResult> {
	private static final String TAG = "AuthorizationTask";
	List<NameValuePair> nameValuePairs;

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
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("j_username", params[0]));
			nameValuePairs.add(new BasicNameValuePair("j_password", params[1]));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			final HttpResponse response = httpClient.execute(httpPost);
			final InputStream is = response.getEntity().getContent();
			final StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer, "UTF-8");
			final String serverAnswer = writer.toString();
			if (serverAnswer.equals("false")) {
				return ExecutionResult.FAIL;
			}
			final List<Cookie> cookies = httpClient.getCookieStore()
					.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					Registry.register("JSESSIONID", cookie.getValue());
				}
			}
			return ExecutionResult.SUCCESS;
			
				/*Log.e(TAG, "Register facebook account");
				if (serverAnswer.equals("false")) {
					final Properties connectionProperties = new Properties();
					connectionProperties.load(parent.getAssets().open(
							"connection.properties"));
					final String serverUrl = connectionProperties
							.getProperty("server_url")
							+ connectionProperties.getProperty("add_user_url");
					final HttpPost httpPost2 = new HttpPost(serverUrl);
					final MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					reqEntity.addPart("login", new StringBody(params[0]));
					reqEntity.addPart("password", new StringBody(params[1]));
					reqEntity.addPart("enabled", new StringBody("true"));
					Log.i(TAG, "Request rdy");
					httpPost2.setEntity(reqEntity);
					Log.i(TAG, "Get responce");
					final HttpResponse response2 = httpClient.execute(httpPost2);
					final InputStream is2 = response2.getEntity().getContent();
					final StringWriter writer2 = new StringWriter();
					IOUtils.copy(is2, writer2, "UTF-8");
					final String serverAnswer2 = writer2.toString();
					if (serverAnswer2 == "done") {

					final HttpPost httpPost3 = new HttpPost(params[2]);
					nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("j_username", params[0]));
					nameValuePairs.add(new BasicNameValuePair("j_password", params[1]));
					httpPost3.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					final HttpResponse response3 = httpClient.execute(httpPost3);
					final InputStream is3 = response3.getEntity().getContent();
					final StringWriter writer3 = new StringWriter();
					IOUtils.copy(is3, writer3, "UTF-8");
					final String serverAnswer3 = writer3.toString();
					if (serverAnswer3.equals("false")) {
						return ExecutionResult.FAIL;
					}
					final List<Cookie> cookies = httpClient.getCookieStore()
							.getCookies();
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals("JSESSIONID")) {
							Registry.register("JSESSIONID", cookie.getValue());
						}
					}
					return ExecutionResult.SUCCESS;
					} else {
						Log.e(TAG, "WTF?!!");
					}
				}*/
		} catch (Exception ex) {
			Log.v(TAG, "Could not send login request", ex);
		}
		return ExecutionResult.FAIL;
	}

	@Override
	protected void onPostExecute(ExecutionResult result) {
		switch (result) {
		case SUCCESS:
			DataBaseLoginUtil sqh = new DataBaseLoginUtil(parent);
			SQLiteDatabase sqdb = sqh.getWritableDatabase();
			sqdb.close();
			sqh.close();
			new GetOrdersListTask(parent).execute();
			break;
		case FAIL:
			// do something horrible here
			Toast.makeText(parent.getApplicationContext(),
					R.string.auth_data_warning, Toast.LENGTH_LONG).show();
			break;
		}
	}
}
