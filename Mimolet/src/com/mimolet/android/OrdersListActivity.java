package com.mimolet.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mimolet.android.adapter.OrderArrayAdapter;

public class OrdersListActivity extends ListActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_orders_list);
    
    Intent intent = getIntent();
    String[] orders = intent.getStringArrayExtra("orders");
    String[] images = intent.getStringArrayExtra("imageSources");
    // in call activity use intent.putExtra("")
    final ListView listview = (ListView) findViewById(R.id.list);
    listview.setAdapter(new OrderArrayAdapter(this, R.id.undobar, orders, images));
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
 
    //get selected items
    String selectedValue = (String) getListAdapter().getItem(position);
    Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
 
  }

}
