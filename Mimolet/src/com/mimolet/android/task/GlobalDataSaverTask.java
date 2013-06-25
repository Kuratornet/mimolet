package com.mimolet.android.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class GlobalDataSaverTask {
	
	public static Integer getOwnerId(String name) {
		final  Properties connectionProperties = new Properties();
		try {
			final DefaultHttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(connectionProperties.getProperty("server_url") + connectionProperties
					.getProperty("ownerid_path"));
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("username", name));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse responce = httpClient.execute(httpPost);
			return Integer.valueOf(responce.getEntity().toString());
		} catch (Exception ex) {
			Log.v("Error", "Could not send login request", ex);
		}
		return 0;
	}
}
