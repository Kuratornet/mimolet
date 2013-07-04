package com.mimolet.android.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.mimolet.android.AuthorizationActivity;
import com.mimolet.android.task.AuthorizationTask.ExecutionResult;

import entity.Order;

public class GetOrdersListTask extends AsyncTask<String, Void, List<Order>> {
  
  private AuthorizationActivity parent;
  final Properties connectionProperties;
  
  public GetOrdersListTask(AuthorizationActivity parent) {
    this.parent = parent;
    connectionProperties = new Properties();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected List<Order> doInBackground(String... params) {
    try {
      final DefaultHttpClient httpClient = new DefaultHttpClient();
      final HttpPost httpPost = new HttpPost(connectionProperties.getProperty("server_url") + connectionProperties
          .getProperty("getbyownerid_path"));
      HttpResponse responce = httpClient.execute(httpPost);
      List<Order> list = (ArrayList<Order>) responce.getEntity();
      return list;
    } catch (Exception ex) {
      Log.v("GetOrdersList", "Could not get order list", ex);
    }
    return null;
  }
  
  @Override
  protected void onPostExecute(List<Order> result) {
    if (result != null) {
      parent.goToOrderList(null, result);
    } else {
      //Do something horrible here
    }
  }

}
