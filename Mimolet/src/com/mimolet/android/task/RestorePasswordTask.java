package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mimolet.android.AuthorizationActivity;
import com.mimolet.android.R;

public class RestorePasswordTask extends AsyncTask<String, Void, RestorePasswordTask.ExecutionResult> {
	
	private static final String TAG = "RestorePasswordTask";
	final Activity parent;
	
	public RestorePasswordTask(Activity parent) {
		this.parent = parent;
	}
	
	@Override
	protected RestorePasswordTask.ExecutionResult doInBackground(String... params) {
		try {
			final HttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(params[1]);
			final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("email", new StringBody(params[0]));
			Log.i(TAG, "Request rdy " + params[1]);
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
				return ExecutionResult.WRONG_EMAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ExecutionResult.FAIL;
		}
	}
	
	@Override
	protected void onPostExecute(RestorePasswordTask.ExecutionResult result) {
		switch (result) {
		case SUCCESS:
			new AlertDialog.Builder(parent)
	           .setMessage(R.string.restore_passwordSended)
	           .setCancelable(false)
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    Intent intent = new Intent(parent, AuthorizationActivity.class);
	                    parent.startActivity(intent);
	                    parent.finish();
	               }
	           }).show();
			break;
		case FAIL:
			Toast.makeText(parent.getApplicationContext(),
					R.string.unidentified_error, Toast.LENGTH_LONG).show();
			break;
		case WRONG_EMAIL:
			Toast.makeText(parent.getApplicationContext(),
					R.string.restore_wrongEmail, Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	public enum ExecutionResult {
		SUCCESS, FAIL, WRONG_EMAIL;
	}
}
