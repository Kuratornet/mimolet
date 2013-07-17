package com.mimolet.android;

import com.mimolet.android.task.GetOrdersListTask;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class BottomMenu {
  public void openMyOrders(Activity activity) {
    new GetOrdersListTask(activity).execute();
  }
  
  public void openAddBook(Activity activity) {
    final Intent intent = new Intent(activity.getApplicationContext(),
        AddBookActivity.class);
    activity.startActivity(intent);
  }
  
  public void openMyPaidOrders(Activity activity) {
    Toast.makeText(activity.getApplicationContext(), R.string.page_under_work, Toast.LENGTH_LONG).show();
  }
}
