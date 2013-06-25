package com.mimolet.android.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mimolet.android.AuthorizationActivity;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.task.AuthorizationTask.ExecutionResult;
import com.mimolet.android.util.Registry;

import android.os.AsyncTask;
import android.util.Log;

public class GetOrdersListTask extends AsyncTask<String, Void, AuthorizationTask.ExecutionResult> {
	
	private AuthorizationActivity parent;
	final Properties connectionProperties;
	
	public GetOrdersListTask(AuthorizationActivity parent) {
		this.parent = parent;
		connectionProperties = new Properties();
	}
	@Override
	protected ExecutionResult doInBackground(String... params) {
		try {
			final DefaultHttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(connectionProperties.getProperty("server_url") + connectionProperties
					.getProperty("getbyowerid_path"));
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					1);
			nameValuePairs.add(new BasicNameValuePair("ownerID", params[0]));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse responce = httpClient.execute(httpPost);
			
			return ExecutionResult.SUCCESS;
		} catch (Exception ex) {
			Log.v("GetOrdersList", "Could not send login request", ex);
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
