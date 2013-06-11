package com.mimolet.android.task;

import java.io.File;

import org.apache.commons.io.FileUtils;
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

public class SendPDFTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = "SendPDFTask";
	private final ProgressDialog dialog;
	private String filePath;

	public SendPDFTask(Context context, String filePath) {
		dialog = new ProgressDialog(context);
		this.filePath = filePath;
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Uploading...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		final HttpClient httpClient = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(
				"http://93.126.85.253:8080/server/upload"); //FIXME
		try {
			final byte[] bytes = FileUtils.readFileToByteArray(new File(
					filePath));

			final MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", new ByteArrayBody(bytes, "img.pdf"));
			reqEntity.addPart("description", new StringBody("Pizdatij albom"));
			reqEntity.addPart("binding", new StringBody("Na skoba"));
			reqEntity.addPart("paper", new StringBody("Tyaletnaja"));
			reqEntity.addPart("print", new StringBody("Digital-Surround-Mega-Bass"));
			reqEntity.addPart("blockSize", new StringBody("20*20"));
			reqEntity.addPart("pages", new StringBody("20"));
			httpPost.setEntity(reqEntity);
			httpClient.execute(httpPost);
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