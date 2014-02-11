package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mimolet.android.R;
import com.mimolet.android.util.Registry;

public class DeleteOrderTask extends AsyncTask<String, Void, DeleteOrderTask.ExecutionResult> {
	private static final String TAG = "DeleteOrderTask";
	private Activity parent;
	
	public DeleteOrderTask(Activity activity) {
		this.parent = activity;  
	}
	
	@Override
	protected ExecutionResult doInBackground(String... params) {
		try {
			final HttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(params[0] + params[1]);
			httpPost.addHeader("Cookie",
					"JSESSIONID=" + Registry.<String> get("JSESSIONID"));
			final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			httpPost.setEntity(reqEntity);
			final HttpResponse response = httpClient.execute(httpPost);
			Log.i(TAG, "Get responce");
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			final String line = rd.readLine();
			Log.v(TAG, "Server answer line value = " + line);
			if (line.equals("true")) {
				return ExecutionResult.SUCCESS;
			} else {
				return ExecutionResult.FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ExecutionResult.FAIL;
		}
	}
	
	@Override
	protected void onPostExecute(ExecutionResult result) {
		switch (result) {
		case SUCCESS:
			new GetOrdersListTask(parent).execute();
			parent.finish();
			break;
		case FAIL:
			Toast.makeText(parent.getApplicationContext(),
					R.string.unidentified_error, Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	public enum ExecutionResult {
		SUCCESS, FAIL;
	}
}
