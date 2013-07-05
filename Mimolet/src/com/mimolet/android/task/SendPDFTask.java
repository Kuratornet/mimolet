package com.mimolet.android.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mimolet.android.util.Registry;

public class SendPDFTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = "SendPDFTask";
	private final ProgressDialog dialog;
	private String filePath;
	private String previewPath;
	private Context context;

	public SendPDFTask(Context context, String filePath, String previePath) {
		dialog = new ProgressDialog(context);
		this.filePath = filePath;
		this.context = context;
		this.previewPath = previewPath;
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Uploading...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		final HttpClient httpClient = new DefaultHttpClient();
		final Properties connectionProperties = new Properties();
		try {
			connectionProperties.load(context.getAssets().open(
					"connection.properties"));
			final String serverUrl = connectionProperties
					.getProperty("server_url")
					+ connectionProperties.getProperty("pdf_upload_path");
			final HttpPost httpPost = new HttpPost(serverUrl);
			final byte[] bytes = FileUtils.readFileToByteArray(new File(
					filePath));
			final byte[] previewBytes = FileUtils.readFileToByteArray(new File(
					previewPath)); 
			httpPost.addHeader("Cookie",
					"JSESSIONID=" + Registry.<String> get("JSESSIONID"));
			final MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", new ByteArrayBody(bytes, "img.pdf"));
			reqEntity.addPart("preview", new ByteArrayBody(previewBytes, "img.png"));
			reqEntity.addPart("description", new StringBody("Pizdatij albom"));
			reqEntity.addPart("binding", new StringBody("1"));
			reqEntity.addPart("paper", new StringBody("1"));
			reqEntity.addPart("print", new StringBody("1"));
			reqEntity.addPart("blockSize", new StringBody("1"));
			reqEntity.addPart("pages", new StringBody("20"));
			httpPost.setEntity(reqEntity);
			final HttpResponse response = httpClient.execute(httpPost);
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				Log.v(TAG, line);
			}
		} catch (Exception ex) {
			Log.v(TAG, "Could not upload pdf file");
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}
}