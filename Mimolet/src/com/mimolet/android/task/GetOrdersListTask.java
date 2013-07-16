package com.mimolet.android.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mimolet.android.global.GlobalMethods;
import com.mimolet.android.util.Registry;

import entity.Order;

public class GetOrdersListTask extends AsyncTask<String, Void, List<Order>> {

  private Activity parent;
  final Properties connectionProperties;

  public GetOrdersListTask(Activity parent) {
    this.parent = parent;
    connectionProperties = new Properties();
  }

  @Override
  protected List<Order> doInBackground(String... params) {
    try {
      connectionProperties.load(parent.getAssets().open("connection.properties"));
      final DefaultHttpClient httpClient = new DefaultHttpClient();
      final HttpPost httpPost =
          new HttpPost(connectionProperties.getProperty("server_url")
              + connectionProperties.getProperty("getbyownerid_path"));
      httpPost.addHeader("Cookie", "JSESSIONID=" + Registry.<String> get("JSESSIONID"));
      HttpResponse responce = httpClient.execute(httpPost);
      Type listType = new TypeToken<ArrayList<Order>>() {
      }.getType();
      final GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
          return new Date(json.getAsJsonPrimitive().getAsLong());
        }
      });
      final Gson gson = builder.create();
      List<Order> list = gson.fromJson(EntityUtils.toString(responce.getEntity()), listType);
      return list;
    } catch (Exception ex) {
      Log.v("GetOrdersList", "Could not get order list", ex);
    }
    return null;
  }

  @Override
  protected void onPostExecute(List<Order> result) {
    if (result != null) {
      GlobalMethods.goToOrderList(parent, null, result);
    } else {
      // Do something horrible here
    }
  }

}
