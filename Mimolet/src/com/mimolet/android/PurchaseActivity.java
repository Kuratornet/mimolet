package com.mimolet.android;

import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.task.PreviwImageShowTask;
import com.mimolet.android.task.PurchaseOrderTask;
import com.mimolet.android.util.Registry;

public class PurchaseActivity extends Activity {
	private static final String TAG = "PurchaseActivity";
	private Activity thisActivity;

	private static final int BOOK_PRICE = 14;
	private static final int PAGE_PRICE = 1;

	private int orderID;

	private ImageView orderCover;
	private TextView orderName;
	private TextView bindingType;
	private TextView coverType;
	private TextView paperType;
	private TextView printType;
	private TextView blockSize;
	private TextView pagesCount;
	private TextView amountValue;
	private TextView additionalPagesValue;
	private TextView singleBookPrice;
	private TextView additionalPagesPrice;
	private TextView overalPrice;
	private Button purchaseButton;

	public void setCoverImage(Bitmap image) {
		if (image != null) {
			orderCover.setImageBitmap(image);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);
		thisActivity = this;
		Intent itemIntent = getIntent();
		orderID = itemIntent.getIntExtra("id", 0);
		orderCover = (ImageView) findViewById(R.id.purchaseOrderCover);
		final String imageURL = itemIntent.getStringExtra("image");
		Log.i(TAG, "Try to get image");
		new PreviwImageShowTask(this).execute(imageURL);
		orderName = (TextView) findViewById(R.id.purchaseOrderName);
		orderName.setText(itemIntent.getStringExtra("orderName"));
		bindingType = (TextView) findViewById(R.id.purchaseBindingValueText);
		bindingType.setText(GlobalVariables.bindingType[Integer
				.valueOf(itemIntent.getStringExtra("binding"))]);
		coverType = (TextView) findViewById(R.id.purchaseCoverValueText);
		coverType.setText(GlobalVariables.coverType[Integer.valueOf(itemIntent
				.getStringExtra("cover"))]);
		paperType = (TextView) findViewById(R.id.purchasePaperValueText);
		paperType.setText(GlobalVariables.paperType[Integer.valueOf(itemIntent
				.getStringExtra("paper"))]);
		printType = (TextView) findViewById(R.id.purchasePrintValueText);
		printType.setText(GlobalVariables.printType[Integer.valueOf(itemIntent
				.getStringExtra("print"))]);
		blockSize = (TextView) findViewById(R.id.purchaseBlockSizeValueText);
		blockSize.setText(GlobalVariables.blockSizeType[Integer
				.valueOf(itemIntent.getStringExtra("blockSize"))]);
		pagesCount = (TextView) findViewById(R.id.purchasePagesValueTextText);
		pagesCount.setText(itemIntent.getStringExtra("pages"));
		amountValue = (TextView) findViewById(R.id.purchaseAmountValueText);
		additionalPagesValue = (TextView) findViewById(R.id.purchaseAdditionalPagesValueText);
		singleBookPrice = (TextView) findViewById(R.id.purchaseSingleBookPriceValueText);
		additionalPagesPrice = (TextView) findViewById(R.id.purchaseAdditionalPagesPriceText);
		overalPrice = (TextView) findViewById(R.id.purchaseOveralPriceText);
		int additionalPagesValueFromIntent = 0; // Need to resolve!!!
		amountValue.setText("1 шт");
		singleBookPrice.setText(BOOK_PRICE + ".00 $");
		additionalPagesValue.setText(additionalPagesValueFromIntent + " шт");
		additionalPagesPrice.setText(additionalPagesValueFromIntent
				* PAGE_PRICE + ".00 $");
		overalPrice.setText(BOOK_PRICE
				+ (additionalPagesValueFromIntent * PAGE_PRICE) + ".00 $");
		purchaseButton = (Button) findViewById(R.id.purchasePurchaseButton);
		purchaseButton.setOnClickListener(new PurchaseOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.purchase, menu);
		return true;
	}

	private class PurchaseOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				final Properties connectionProperties = new Properties();
				connectionProperties.load(getAssets().open(
						"connection.properties"));
				String serverUrl = connectionProperties
						.getProperty("server_url")
						+ connectionProperties.getProperty("purchaseorder");
				new PurchaseOrderTask(thisActivity).execute(serverUrl,
						String.valueOf(orderID), (String) Registry.get("email"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
