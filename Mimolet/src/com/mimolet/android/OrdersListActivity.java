package com.mimolet.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mimolet.android.adapter.OrderArrayAdapter;

public class OrdersListActivity extends ListActivity {
  
  private final Activity activity = this;

  final BottomMenu bottomMenu = new BottomMenu();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_orders_list);

    Intent intent = getIntent();
    String[] orders = intent.getStringArrayExtra("orders");
    String[] images = intent.getStringArrayExtra("imageSources");
    String[] createData = intent.getStringArrayExtra("createData");
    // in call activity use intent.putExtra("")
    getListView().setAdapter(new OrderArrayAdapter(this, R.id.undobar, orders, images, createData));
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {

    // get selected items
    String selectedValue = "Unimplimented function";
    Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

  }

  @Override
  public final boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
      new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              Intent intent = new Intent(activity, AuthorizationActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              intent.putExtra("Exit me", true);
              startActivity(intent);
              finish();
            }
          }).setNegativeButton("No", null).show();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  public void myOrders(View view) {
    // already here, do nothing
  }

  public void createBook(View view) {
    bottomMenu.openAddBook(this);
  }

  public void paidOrders(View view) {
    bottomMenu.openMyPaidOrders(this);
  }

}
