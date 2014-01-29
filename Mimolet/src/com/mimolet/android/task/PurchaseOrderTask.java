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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mimolet.android.R;

public class PurchaseOrderTask extends AsyncTask<String, Void, PurchaseOrderTask.ExecutionResult> {
	private static final String TAG = "PurchaseOrderTask";
	private final ProgressDialog dialog;
	private Activity parent;
	
	public PurchaseOrderTask(Activity activity) {
		this.parent = activity;  
		dialog = new ProgressDialog(parent);
	}
	
	@Override
	protected ExecutionResult doInBackground(String... params) {
		try {
			
			final HttpClient httpClient = new DefaultHttpClient();
			final HttpPost httpPost = new HttpPost(params[0]);
			final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("id", new StringBody(params[1]));
			reqEntity.addPart("email", new StringBody(params[2]));
			Log.i(TAG, "Request rdy for order id " + params[1] + " and email " + params[2] + 
					" to " + params[0]);
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
	protected void onPreExecute() {
		this.dialog.setMessage("Purchase in progress...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}
	
	@Override
	protected void onPostExecute(ExecutionResult result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		switch (result) {
		case SUCCESS:
			new AlertDialog.Builder(parent)
	           .setMessage(R.string.purchase_orderPurchaseSuccesfull)
	           .setCancelable(false)
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    parent.finish();
	               }
	           }).show();
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
