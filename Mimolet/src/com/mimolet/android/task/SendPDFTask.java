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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.util.Registry;

import entity.Order;

public class SendPDFTask extends AsyncTask<Void, Void, Void> {
  private static final String TAG = "SendPDFTask";
  private final ProgressDialog dialog;
  private String filePath;
  private String previewPath;
  private Order order;
  private Activity context;

  public SendPDFTask(Activity context, String filePath, String previewPath, Order order) {
    dialog = new ProgressDialog(context);
    this.filePath = filePath;
    this.context = context;
    this.previewPath = previewPath;
    this.order = order;
    Log.i(TAG, "Send PDF created");
  }

  protected void onPreExecute() {
    this.dialog.setMessage("Uploading...");
    this.dialog.setCancelable(false);
    this.dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    Log.i(TAG, "Start on backgraund");
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
      reqEntity.addPart("description", new StringBody(order.getDescription()));
      reqEntity.addPart("binding", new StringBody("1"));
      reqEntity.addPart("paper", new StringBody("1"));
      reqEntity.addPart("print", new StringBody("1"));
      reqEntity.addPart("blockSize", new StringBody("1"));
      reqEntity.addPart("pages", new StringBody("20"));
      Log.i(TAG, "Request rdy");
      httpPost.setEntity(reqEntity);
      final HttpResponse response = httpClient.execute(httpPost);
      Log.i(TAG, "Get responce");
      final BufferedReader rd = new BufferedReader(new InputStreamReader(
          response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
        Log.v(TAG, line);
        File pdfFile = new File(GlobalVariables.MIMOLET_FOLDER + "Images.pdf");
        pdfFile.delete();
        
        File previewFile = new File(GlobalVariables.MIMOLET_FOLDER + "preview.png");
        previewFile.delete();
      }
      (new GetOrdersListTask(context)).execute();
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
