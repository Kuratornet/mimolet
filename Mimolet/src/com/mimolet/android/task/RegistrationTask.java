package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mimolet.android.R;
import com.mimolet.android.util.DataBaseLoginUtil;
import com.mimolet.android.util.Registry;

public class RegistrationTask extends AsyncTask<String, Void, RegistrationTask.ExecutionResult> {
	private static final String TAG = "RegistrationTask";
	private final ProgressDialog dialog;
	Activity parent;
	
	
	public RegistrationTask(Activity parent) {
		this.parent = parent;
		dialog = new ProgressDialog(this.parent);
	}
	
	@Override
	protected ExecutionResult doInBackground(String... params) {
		try {
			final HttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(params[2]);
			final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("email", new StringBody(params[0]));
			reqEntity.addPart("password", new StringBody(params[1]));
			Log.i(TAG, "Request rdy");
			httpPost.setEntity(reqEntity);
			final HttpResponse response = httpClient.execute(httpPost);
			Log.i(TAG, "Get responce");
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			final String line = rd.readLine();
			Log.v(TAG, "Server answer line value = " + line);
			if (line.equals("true")) {
				final List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore()
						.getCookies();
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("JSESSIONID")) {
						Registry.register("JSESSIONID", cookie.getValue());
					}
				}
				return ExecutionResult.SUCCESS;
			} else if (line.equals("wronglogin")) {
				return ExecutionResult.WRONG_LOGIN;
			} else {
				return ExecutionResult.FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ExecutionResult.FAIL;
		}
	}
	
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Logging in...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}
	
	@Override
	protected void onPostExecute(RegistrationTask.ExecutionResult result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		switch (result) {
		case SUCCESS:
			DataBaseLoginUtil sqh = new DataBaseLoginUtil(parent);
			SQLiteDatabase sqdb = sqh.getWritableDatabase();
			sqdb.close();
			sqh.close();
			new GetOrdersListTask(parent).execute();
			break;
		case WRONG_LOGIN:
			Toast.makeText(parent.getApplicationContext(),
					R.string.registration_wrongLoginToast, Toast.LENGTH_LONG).show();
			break;
		case FAIL:
			Toast.makeText(parent.getApplicationContext(),
					R.string.unidentified_error, Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	public enum ExecutionResult {
		SUCCESS, FAIL, WRONG_LOGIN;
	}
}
