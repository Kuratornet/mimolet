package com.mimolet.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mimolet.android.adapter.OrderArrayAdapter;

public class OrdersListActivity extends ListActivity {
  
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
    getListView().setAdapter(
        new OrderArrayAdapter(this, R.id.undobar, orders, images, createData));
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {

    // get selected items
    String selectedValue = (String) getListAdapter().getItem(position);
    Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

  }
  
  public void myOrders(View view) {
    //already here, do nothing
  }

  public void createBook(View view) {
    bottomMenu.openAddBook(this);
  }

  public void paidOrders(View view) {
    bottomMenu.openMyPaidOrders(this);
  }

}
