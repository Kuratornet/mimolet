package com.mimolet.android;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class BottomMenu {
  public void openMyOrders(Activity activity) {
    final Intent intent = new Intent(activity.getApplicationContext(),
        OrdersListActivity.class);
    activity.startActivity(intent);
  }
  
  public void openAddBook(Activity activity) {
    final Intent intent = new Intent(activity.getApplicationContext(),
        AddBookActivity.class);
    activity.startActivity(intent);
  }
  
  public void openMyPaidOrders(Activity activity) {
    Toast.makeText(activity.getApplicationContext(), "This page is under work now", Toast.LENGTH_LONG).show();
  }
}
