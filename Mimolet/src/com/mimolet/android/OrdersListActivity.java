package com.mimolet.android;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.mimolet.android.adapter.OrderArrayAdapter;
import com.mimolet.android.global.GlobalVariables;

public class OrdersListActivity extends ListActivity {

	private final Activity activity = this;
	int[] ordersId;
	String[] orders;
	String[] images;
	String[] createData;
	String[] bindingsDate;
	String[] paperData;
	String[] printData;
	String[] coverData;
	String[] blockSizeData;
	String[] pagesData;

	final BottomMenu bottomMenu = new BottomMenu();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders_list);

		Intent intent = getIntent();
		ordersId = intent.getIntArrayExtra("ids");
		orders = intent.getStringArrayExtra("orders");
		images = intent.getStringArrayExtra("imageSources");
		createData = intent.getStringArrayExtra("createData");
		bindingsDate = intent.getStringArrayExtra("bindingsDate");
		paperData = intent.getStringArrayExtra("paperData");
		printData = intent.getStringArrayExtra("printData");
		coverData = intent.getStringArrayExtra("coverData");
		blockSizeData = intent.getStringArrayExtra("blockSizeData");
		pagesData = intent.getStringArrayExtra("pagesData");
		// in call activity use intent.putExtra("")
		getListView().setAdapter(new OrderArrayAdapter(this, R.id.undobar, 
				orders, images,	createData));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent itemIntent = new Intent(OrdersListActivity.this,
				PurchaseActivity.class);
		itemIntent.putExtra("id", ordersId[position]);
		itemIntent.putExtra("orderName", orders[position]);
		itemIntent.putExtra("image", images[position]);
		itemIntent.putExtra("createData", createData[position]);
		itemIntent.putExtra("binding", bindingsDate[position]);
		itemIntent.putExtra("paper", paperData[position]);
		itemIntent.putExtra("print", printData[position]);
		itemIntent.putExtra("cover", coverData[position]);
		itemIntent.putExtra("blockSize", blockSizeData[position]);
		itemIntent.putExtra("pages", pagesData[position]);
		startActivity(itemIntent);
		finish();
	}

	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new AlertDialog.Builder(this)
					.setMessage("Are you sure you want to exit?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									final File imageFolder = new File(
											GlobalVariables.IMAGE_FOLDER);
									imageFolder.mkdirs();
									if (imageFolder.exists()) {
										File[] listOfFiles = imageFolder
												.listFiles();
										if (listOfFiles.length != 0) {
											for (int i = 0; i < listOfFiles.length; i++) {
												listOfFiles[i].delete();
											}
										}
									}
									Intent intent = new Intent(activity,
											AuthorizationActivity.class);
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
