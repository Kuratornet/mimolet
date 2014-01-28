package com.mimolet.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.task.PreviwImageShowTask;

public class PurchaseActivity extends Activity {
	  private static final String TAG = "PurchaseActivity";

	
	private static final int BOOK_PRICE = 14;
	private static final int PAGE_PRICE = 1;
	
	ImageView orderCover;
	TextView orderName;
	TextView bindingType;
	TextView coverType;
	TextView paperType;
	TextView printType;
	TextView blockSize;
	TextView pagesCount;
	TextView amountValue;
	TextView additionalPagesValue;
	TextView singleBookPrice;
	TextView additionalPagesPrice;
	TextView overalPrice;
	
	boolean checker = true;
	
	public void setCoverImage(Bitmap image) {
		if (image != null) {
			orderCover.setImageBitmap(image);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);
	    Intent itemIntent = getIntent();
	    orderCover = (ImageView) findViewById(R.id.purchaseOrderCover);
	    final String imageURL = itemIntent.getStringExtra("image");
	    Log.i(TAG, "Try to get image");
	    new PreviwImageShowTask(this).execute(imageURL);
        orderName = (TextView) findViewById(R.id.purchaseOrderName);
		orderName.setText(itemIntent.getStringExtra("orderName"));
		bindingType = (TextView) findViewById(R.id.purchaseBindingValueText);
		bindingType.setText(GlobalVariables.bindingType[Integer.valueOf(itemIntent.getStringExtra("binding"))]);
		coverType = (TextView) findViewById(R.id.purchaseCoverValueText);
		coverType.setText(GlobalVariables.coverType[Integer.valueOf(itemIntent.getStringExtra("cover"))]);
		paperType = (TextView) findViewById(R.id.purchasePaperValueText);
		paperType.setText(GlobalVariables.paperType[Integer.valueOf(itemIntent.getStringExtra("paper"))]);
		printType = (TextView) findViewById(R.id.purchasePrintValueText);
		printType.setText(GlobalVariables.printType[Integer.valueOf(itemIntent.getStringExtra("print"))]);
		blockSize = (TextView) findViewById(R.id.purchaseBlockSizeValueText);
		blockSize.setText(GlobalVariables.blockSizeType[Integer.valueOf(itemIntent.getStringExtra("blockSize"))]);
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
		additionalPagesPrice.setText(additionalPagesValueFromIntent * PAGE_PRICE + ".00 $");
		overalPrice.setText(BOOK_PRICE + (additionalPagesValueFromIntent * PAGE_PRICE) + ".00 $"); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.purchase, menu);
		return true;
	}
}
