package com.mimolet.android;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mimolet.android.task.GetOrdersListTask;

public class BottomMenu {
  public void openMyOrders(Activity activity) {
    new GetOrdersListTask(activity).execute();
  }
  
  public void openAddBook(Activity activity) {
    final Intent intent = new Intent(activity.getApplicationContext(),
        AddBookActivity.class);
    activity.startActivity(intent);
    activity.finish();
  }
  
  public void openMyPaidOrders(Activity activity) {
    Toast.makeText(activity.getApplicationContext(), R.string.page_under_work, Toast.LENGTH_LONG).show();
  }
}
